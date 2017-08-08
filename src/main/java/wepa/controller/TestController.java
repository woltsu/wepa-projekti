package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.TestDatabase;
import wepa.service.AccountService;

@Profile("production")
@Controller
@RequestMapping("/tests")
public class TestController {
    
    @Autowired
    private TestDatabase testDatabase;
    
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String getTests(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("tests", testDatabase.findByAccount(accountService.getAuthenticatedAccount().getId()));
        return "tests";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postTests(@RequestParam String name) {
       testDatabase.create(name, accountService.getAuthenticatedAccount().getId());
       return "redirect:/tests";
    }
    
}
