package wepa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;
import wepa.database.StatDatabase;
import wepa.service.AccountService;
import wepa.validator.AccountValidator;

//Account controller
@Profile("production")
@Controller
public class AccountController {

    @Autowired
    private AccountDatabase accountDatabase;

    @Autowired
    private AccountValidator accountValidator;

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private StatDatabase statDatabase;

    //Login page. If the user is already logged in, the controller redirects them to the front page.
    //If the controller detects error params in the url, it will alert of errors.
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, @RequestParam Map<String, String> params) {
        if (accountService.getAuthenticatedAccount() != null) {
            return "redirect:/";
        }
        if (params.containsKey("error")) {
            List<String> errors = new ArrayList();
            errors.add("Wrong username or password!");
            model.addAttribute("errors", errors);
        }
        return "login";
    }

    //Sign up page. If the user is already logged in, they will be redirected to the front page.
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String getSignup() {
        if (accountService.getAuthenticatedAccount() != null) {
            return "redirect:/";
        }
        return "signup";
    }

    //Account creating. If account validator detects problems, the account won't be created and the user will be alerted
    //of what went wrong. If there are no problems with the account, the user will be redirected to the login page with a
    //success message.
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String postSignup(Model model, @RequestParam String username,
            @RequestParam String password,
            @RequestParam String passwordAgain) {

        List<String> errors = accountValidator.validateAccount(username, password, passwordAgain);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("username", username);
            return "signup";
        }
        accountDatabase.create(username, password);
        statDatabase.create(accountDatabase.findByUsername(username).getId());
        model.addAttribute("success", "Account created succesfully!");
        return "login";
    }

}
