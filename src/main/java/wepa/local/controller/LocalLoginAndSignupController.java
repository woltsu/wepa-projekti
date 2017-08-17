package wepa.local.controller;

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
import wepa.local.domain.LocalAccount;
import wepa.local.validator.LocalAccountValidator;
import wepa.local.repository.LocalAccountRepository;

@Profile("default")
@Controller
public class LocalLoginAndSignupController {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalAccountValidator accountValidator;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, @RequestParam Map<String, String> params) {
        if (!params.isEmpty()) {
            List<String> errors = new ArrayList();
            errors.add("Wrong username or password!");
            model.addAttribute("errors", errors);
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

        LocalAccount account = new LocalAccount();
        account.setUsername(username);
        account.setPassword(password);
        accountRepository.save(account);
        model.addAttribute("success", "Account created succesfully!");
        return "login";
    }

}
