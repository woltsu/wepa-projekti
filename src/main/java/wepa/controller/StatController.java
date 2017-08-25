package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.database.AccountDatabase;
import wepa.database.StatDatabase;
import wepa.domain.Account;
import wepa.domain.Stat;
import wepa.service.AccountService;
import wepa.service.StatService;

@Profile("production")
@Controller
@RequestMapping(value = "/{user}/stats")
public class StatController {

    @Autowired
    private StatDatabase statDatabase;
    
    @Autowired
    private AccountDatabase accountDatabase;
    
    @Autowired
    private StatService statService;
    
    @Autowired
    private AccountService accountService;
    
    //Account's stats. If the account doesn't exist, they will be redirected to another page.
    //Uses stat service to get the rank of an user.
    @RequestMapping(method = RequestMethod.GET)
    public String getStats(Model model, @PathVariable String user) {
        Account a = accountDatabase.findByUsername(user);
        if (a == null) {
            return "redirect:/";
        }
        Stat s = statDatabase.findByAccount(a.getId());
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("statUser", a);
        model.addAttribute("stat", s);
        model.addAttribute("rank", statService.getRank(a));
        return "stats";
    }
    
}
