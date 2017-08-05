package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.local.service.LocalAccountService;

@Profile("default")
@Controller
@RequestMapping("*")
public class LocalDefaultController {

    @Autowired
    private LocalAccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "index";
    }

}
