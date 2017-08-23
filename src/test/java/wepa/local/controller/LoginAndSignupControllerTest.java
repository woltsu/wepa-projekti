package wepa.local.controller;

import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAndSignupControllerTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @LocalServerPort
    private Integer port;

    @Test
    public void canSignUpAndLogin() {
        goTo(localHost + port);
        assertEquals("Login", title());
        click(find("#signup"));
        assertTrue(pageSource().contains("Password again"));

        fill(find("#username")).with("test");
        fill(find("#password")).with("test");
        fill(find("#passwordAgain")).with("test");
        submit(find("form").first());
        assertTrue(pageSource().contains("succesfully!"));

        fill(find("#username")).with("test");
        fill(find("#password")).with("test");
        submit(find("form").first());
        assertTrue(pageSource().contains("Hello, "));
    }

    @Test
    public void testSignUpBlank() {
        goTo(localHost + port);
        click(find("#signup"));
        submit(find("form").first());
        assertTrue(pageSource().contains("Username must be at least 3 characters long!"));
        assertTrue(pageSource().contains("Password must be at least 3 characters long!"));
    }

    @Test
    public void testSignUpUsernameTaken() {
        goTo(localHost + port);
        click(find("#signup"));
        fill(find("#username")).with("user");
        submit(find("form").first());
        assertTrue(pageSource().contains("Username taken!"));
    }

    @Test
    public void testSignUpPasswordsDontMatch() {
        goTo(localHost + port);
        click(find("#signup"));
        fill(find("#username")).with("test");
        fill(find("#password")).with("aaa");
        fill(find("#passwordAgain")).with("bbb");
        submit(find("form").first());
        assertTrue(pageSource().contains("Passwords didn't match!"));
    }

    @Test
    public void testLoginBlank() {
        goTo(localHost + port);
        submit(find("form").first());
        assertTrue(pageSource().contains("Wrong username or password!"));
    }

}
