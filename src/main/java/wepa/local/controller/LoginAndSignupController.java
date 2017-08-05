package wepa.local.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.Account;
import wepa.local.repository.AccountRepository;
import wepa.local.validator.AccountValidator;

@Profile("default")
@Controller
public class LoginAndSignupController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountValidator accountValidator;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, @RequestParam Map<String, String> params) {
        if (!params.isEmpty()) {
            model.addAttribute("error", "Wrong username or password!");
        }
        return "login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String getSignup() {
        return "signup";
    }

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

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        accountRepository.save(account);
        model.addAttribute("success", "Account created succesfully!");
        return "login";
    }

}
