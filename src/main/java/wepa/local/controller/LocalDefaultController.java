package wepa.local.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.local.domain.LocalAccount;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.service.LocalAccountService;

@Profile("local")
@Controller
@RequestMapping("*")
public class LocalDefaultController {

    @Autowired
    private LocalAccountService accountService;
    
    @Autowired
    private LocalAccountRepository accountRepository;
    
    @PostConstruct
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("user");
        a.setPassword("user");
        accountRepository.save(a);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "index";
    }

}
