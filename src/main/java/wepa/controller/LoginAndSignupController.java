package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;

@Controller
public class LoginAndSignupController {

    @Autowired
    private AccountDatabase accountDatabase;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
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

        if (!password.equals(passwordAgain)) {
            model.addAttribute("error", "Passwords didn't match!");
            model.addAttribute("username", username);
            return "signup";
        }

        accountDatabase.create(username, password);
        model.addAttribute("success", "Account created succesfully!");
        return "login";
    }

}
