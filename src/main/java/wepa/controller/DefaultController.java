package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;
import wepa.database.QuestionDatabase;
import wepa.service.AccountService;

@Profile("production")
@Controller
@RequestMapping("*")
public class DefaultController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private QuestionDatabase questionDatabase;

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("questions", questionDatabase.getTenPublishedLatest());
        return "index";
    }



}
