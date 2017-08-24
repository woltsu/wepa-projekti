package wepa.local.controller;

import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertEquals;
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
public class DefaultControllerTest extends FluentTest {

    public WebDriver driver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";

    @Override
    public WebDriver getDefaultDriver() {
        return driver;
    }

    @LocalServerPort
    private Integer port;

    private void login() {
        goTo(localHost + port);
        fill(find("#username")).with("user");
        fill(find("#password")).with("user");
        submit(find("form").first());
    }

    @Test
    public void testClickQuestions() {
        login();
        assertEquals("Front page", title());
        assertTrue(pageSource().contains("question20"));
        assertTrue(pageSource().contains("user"));
        click(find("#link").first());
        assertTrue(pageSource().contains("Answer!"));
    }

    @Test
    public void testChangePage() {
        login();
        assertTrue(pageSource().contains("question20"));
        assertTrue(pageSource().contains("question18"));
        assertFalse(pageSource().contains("question9"));
        goTo(localHost + port + "?page=2");
        assertTrue(pageSource().contains("question9"));
        assertFalse(pageSource().contains("question20"));
        assertEquals("Front page", title());
    }

    @Test
    public void testLogout() {
        login();
        assertTrue(pageSource().contains("question20"));
        assertEquals("Front page", title());
        click(find("#logout").first());
        assertTrue(pageSource().contains("username"));
        assertEquals("Login", title());
    }

    @Test
    public void testFindQuestion() {
        login();
        fill(find("#questionId")).with("2");
        submit(find("#questionSearch").first());
        assertTrue(pageSource().contains("question1"));
        assertEquals("Answer", title());
    }

}
