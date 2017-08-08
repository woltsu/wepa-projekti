package wepa.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.TestDatabase;
import wepa.domain.Account;
import wepa.service.AccountService;

@Profile("production")
@Controller
@RequestMapping(value = "/tests")
public class TestController {

    @Autowired
    private TestDatabase testDatabase;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String getTests(Model model) {
        Account self = accountService.getAuthenticatedAccount();
        model.addAttribute("user", self);
        model.addAttribute("tests", testDatabase.findByAccount(self.getId()));
        return "tests";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postTests(@RequestParam String name) {
        Account self = accountService.getAuthenticatedAccount();
        testDatabase.create(name, self.getId());
        return "redirect:/tests";
    }

}
