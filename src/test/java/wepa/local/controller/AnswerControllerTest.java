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
    
    //oikein
    
    //väärin
    
    //pääsee statseihin
    
    //ei voi vastata tyhjää
    
    

}
