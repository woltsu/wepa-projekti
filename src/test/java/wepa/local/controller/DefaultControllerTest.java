package wepa.local.controller;

import javax.annotation.PostConstruct;
import org.fluentlenium.adapter.FluentTest;
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
public class DefaultControllerTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();
    private final String localHost = "http://localhost:";
    
    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @LocalServerPort
    private Integer port;
    
    @Test
    public void canSignUp() {
        goTo(localHost + port);
        click(find("#signup"));
        assertTrue(pageSource().contains("Password again"));
    }

}
