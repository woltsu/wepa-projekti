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
import org.openqa.selenium.By;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest extends FluentTest {

    public WebDriver driver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";

    @Override
    public WebDriver getDefaultDriver() {
        return driver;
    }

    @LocalServerPort
    private Integer port;

    private void login() {
        goTo(localHost + port + "/logout");
        goTo(localHost + port);
        fill(find("#username")).with("user");
        fill(find("#password")).with("user");
        submit(find("form").first());
    }

    private void goToQuestions() {
        goTo(localHost + port + "/user/questions");
    }

    @Test
    public void canGoToOwnQuestions() {
        login();
        goToQuestions();
        assertEquals("My questions", title());
    }

    @Test
    public void testCantGoToOthersQuestions() {
        login();
        goTo(localHost + port + "/admin/questions");
        assertEquals("Front page", title());
    }

    @Test
    public void testCanCreateAndDeleteQuestion() {
        login();
        goToQuestions();
        assertFalse(pageSource().contains("aaa"));
        fill(find("#newQuestion")).with("aaa");
        submit(find("form").get(1));
        String questionInputValue = driver.findElement(By.id("newQuestion")).getAttribute("value");
        assertTrue(questionInputValue.isEmpty());
        assertTrue(pageSource().contains("aaa"));
        submit(find("form").get(find("form").size() - 1));
        assertFalse(pageSource().contains("aaa"));
    }

    @Test
    public void testQuestionValidators() {
        login();
        goToQuestions();
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Question's name must be at least 3 characters long!"));
        fill(find("#newQuestion")).with("aa");
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Question's name must be at least 3 characters long!"));
        fill(find("#newQuestion")).with("aaa");
        submit(find("form").get(1));
        assertFalse(pageSource().contains("Question's name must be at least 3 characters long!"));
        submit(find("form").get(find("form").size() - 1));
    }

    @Test
    public void testCanClickAQuestion() {
        login();
        goToQuestions();
        click(find("#link").first());
        assertEquals("Create a question", title());
        assertTrue(pageSource().contains("question0"));
        assertFalse(pageSource().contains("question3"));
    }
    
    @Test
    public void testCantPublishAQuestionWithNoOptions() {
        login();
        goTo(localHost + port + "/user/questions/26");
        submit(find("form").get(2));
        assertTrue(pageSource().contains("Question must have 4 options before you can publish it!"));
    }
}
