package wepa.local.controller;

import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultControllerTest extends FluentTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

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

    @Test
    public void testClickQuestions() {
        login();
        assertTrue(pageSource().contains("0"));
        assertTrue(pageSource().contains("user"));
        click(find("#link").first());
        assertTrue(pageSource().contains("0"));
        assertTrue(pageSource().contains("1"));
        assertTrue(pageSource().contains("2"));
        assertTrue(pageSource().contains("3"));
        assertTrue(pageSource().contains("10"));
    }

    @Test
    public void testChangePage() throws Exception {
        login();
        click(find(By.name("previousPage")).first());
        assertTrue(pageSource().contains("0"));
        assertTrue(pageSource().contains("9"));
        click(find(By.name("nextPage")).first());
        assertTrue(pageSource().contains("10"));
        assertFalse(pageSource().contains("1"));

    }

    @Test
    public void testLogout() {

    }

    @Test
    public void testFindQuestion() {

    }

}
