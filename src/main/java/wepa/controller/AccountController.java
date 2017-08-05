package wepa.controller;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;

@Controller
@Profile("production")
@RequestMapping("/account")
public class AccountController {

    @RequestMapping(method = RequestMethod.POST)
    public String postAccount(@RequestParam String name) {
//        accountDatabase.create(name);
        return "redirect:/";
    }

}
