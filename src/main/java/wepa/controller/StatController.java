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

@Profile("production")
@Controller
@RequestMapping(value = "/{user}/stats")
public class StatController {

    @Autowired
    private StatDatabase statDatabase;
    
    @Autowired
    private AccountDatabase accountDatabase;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getStats(Model model, @PathVariable String user) {
        Account a = accountDatabase.findByUsername(user);
        Stat s = statDatabase.findByAccount(a.getId());
        model.addAttribute("user", a);
        model.addAttribute("stat", s);
        return "stats";
    }
    
}
