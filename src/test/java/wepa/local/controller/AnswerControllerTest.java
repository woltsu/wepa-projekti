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
import org.openqa.selenium.WebElement;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnswerControllerTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @LocalServerPort
    private Integer port;

    private void login() {
        goTo(localHost + port);
        fill(find("#username")).with("user");
        fill(find("#password")).with("user");
        submit(find("form").first());
    }

    //admin
    //vastaaminen:
    @Test
    public void testAnswerQuestionWrong() {
        login();
        click(find("#link").first());
        assertEquals("Answer", title());
        assertTrue(pageSource().contains("Answer!"));

//        WebElement radioBtn = (WebElement) find("#option").first();
//        radioBtn.click();
//
//        submit(find("form").get(1));
//        assertTrue(pageSource().contains("not correct"));
//        assertTrue(pageSource().contains("The correct option was 10"));
    }
    //oikein

    //pääsee statseihin
    //ei voi vastata tyhjää
}
