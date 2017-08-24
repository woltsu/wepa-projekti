package wepa.local.controller;

import java.util.List;
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

    @Test
    public void testAnswerQuestionWrong() throws InterruptedException {
        login();
        click(find("#link").get(1));
        assertEquals("Answer", title());
        assertTrue(pageSource().contains("Answer!"));
        List<WebElement> radioButton = driver.findElements(By.id("option"));
        List<WebElement> forms = driver.findElements(By.id("answer"));
        assertFalse(radioButton.get(0).isSelected());
        radioButton.get(0).click();
        assertTrue(radioButton.get(0).isSelected());
        submit(find("form").get(1));
        Thread.sleep(300);
        assertTrue(pageSource().contains("not correct"));
    }

    @Test
    public void testAnswerQuestionCorrect() throws InterruptedException {
        login();
        click(find("#link").first());
        assertEquals("Answer", title());
        assertTrue(pageSource().contains("Answer!"));
        List<WebElement> radioButton = driver.findElements(By.id("option"));
        List<WebElement> forms = driver.findElements(By.id("answer"));
        assertFalse(radioButton.get(3).isSelected());
        radioButton.get(3).click();
        assertTrue(radioButton.get(3).isSelected());
        submit(find("form").get(1));
        Thread.sleep(300);
        assertTrue(pageSource().contains("correct"));
    }

    @Test
    public void testGoToStats() {
        login();
        click(find("#link").first());
        List<WebElement> links = driver.findElements(By.id("stats"));
        links.get(0).click();
        assertEquals("Stats", title());
    }

    @Test
    public void testAnswerEmpty() {
        login();
        click(find("#link").get(2));
        submit(find("form").get(1));
        assertTrue(pageSource().contains("Please choose an option"));
    }

    @Test
    public void testGoBack() {
        login();
        click(find("#link").get(0));
        assertEquals("Answer", title());
        click(find("#goBack").first());
        assertEquals("Front page", title());
    }

    @Test
    public void testGoBackPageSaves() {
        login();
        goTo(localHost + port + "?page=2");
        assertTrue(pageSource().contains("question9"));
        click(find("#link").get(0));
        click(find("#goBack").first());
        assertTrue(pageSource().contains("question9"));
    }

//    @Test
//    public void testAdminCanDeleteQuestions() {
//        try {
//            click(find("#logout").first());
//        } catch (Exception e) {
//        }
//        goTo(localHost + port);
//        fill(find("#username")).with("admin");
//        fill(find("#password")).with("admin");
//        submit(find("form").first());
//        click(find("#link").get(0));
//        assertTrue(pageSource().contains("delete"));
//        submit(find("form").get(2));
//        assertEquals("Front page", title());
//        assertFalse(pageSource().contains("question0"));
//    }
}
