package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalStat;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalStatRepository;
import wepa.local.service.LocalStatService;

@Profile("default")
@Controller
@RequestMapping(value = "/{user}/stats")
public class LocalStatController {

    @Autowired
    private LocalStatRepository statRepository;

    @Autowired
    private LocalAccountRepository accountRepository;
    
    @Autowired
    private LocalStatService statService;

    @RequestMapping(method = RequestMethod.GET)
    public String getStats(Model model, @PathVariable String user) {
        LocalAccount a = accountRepository.findByUsername(user);
        LocalStat s = statRepository.findByAccount(a);
        model.addAttribute("stat", s);
        model.addAttribute("user", a);
        model.addAttribute("rank", statService.getRank(a));
        return "stats";
    }

}
