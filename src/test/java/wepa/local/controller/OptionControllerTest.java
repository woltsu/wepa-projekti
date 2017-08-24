package wepa.local.controller;

import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OptionControllerTest extends FluentTest {

    public WebDriver driver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";

    @Override
    public WebDriver getDefaultDriver() {
        return driver;
    }

    @LocalServerPort
    private Integer port;

    private void login() {
        try {
            click(find("#logout").first());
        } catch (Exception e) {
        }
        goTo(localHost + port);
        fill(find("#username")).with("user");
        fill(find("#password")).with("user");
        submit(find("form").first());
    }

    private void goToAQuestion() {
        goTo(localHost + port + "/user/questions/26");
    }

    @Test
    public void testCanCreateAnOption() {
        login();
        goToAQuestion();
        assertEquals("Create a question", title());
        while (find("form").size() > 3) {
            submit(find("form").get(find("form").size() - 1));
        }
        assertFalse(pageSource().contains("correctOption"));
        fill(find("#optionName")).with("correctOption");
        click(find("#correct"));
        submit(find("form").get(1));
        assertTrue(pageSource().contains("correctOption"));
    }

    @Test
    public void testCantCreateAnEmptyOption() {
        login();
        goToAQuestion();
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Option value must not be empty!"));
    }

    @Test
    public void testCantHaveTwoCorrectOptions() {
        login();
        goToAQuestion();
        while (find("form").size() > 3) {
            submit(find("form").get(find("form").size() - 1));
        }
        fill(find("#optionName")).with("correctOption");
        click(find("#correct"));
        submit(find("form").get(1));
        fill(find("#optionName")).with("correctOption");
        click(find("#correct"));
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Question already has a correct option!"));
    }

    @Test
    public void testCantHaveOver3FalseOptions() {
        login();
        goToAQuestion();
        while (pageSource().contains("correctOption")) {
            submit(find("form").get(find("form").size() - 1));
        }
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        assertFalse(pageSource().contains("Question has maximum number of incorrect options!"));
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Question has maximum number of incorrect options!"));
    }

    @Test
    public void testMustHave4OptionsBeforePublishing() {
        login();
        goToAQuestion();
        while (find("form").size() > 3) {
            submit(find("form").get(find("form").size() - 1));
        }
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        submit(find("form").get(2));
        assertTrue(pageSource().contains("Question must have 4 options before you can publish it!"));
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        submit(find("form").get(2));
        assertTrue(pageSource().contains("Question must have 4 options before you can publish it!"));
        fill(find("#optionName")).with("falseOption");
        click(find("#incorrect"));
        submit(find("form").get(1));
        submit(find("form").get(2));
        assertTrue(pageSource().contains("Question must have 4 options before you can publish it!"));
        fill(find("#optionName")).with("correctOption");
        click(find("#correct"));
        submit(find("form").get(1));
        submit(find("form").get(2));
        assertFalse(pageSource().contains("Question must have 4 options before you can publish it!"));
        assertFalse(pageSource().contains("set public"));
        assertTrue(pageSource().contains("set private"));
        goTo(localHost + port);
        assertTrue(pageSource().contains("emptyQuestion"));
        goToAQuestion();
        submit(find("form").get(2));
        goTo(localHost + port);
        assertFalse(pageSource().contains("emptyQuestion"));
    }
}
