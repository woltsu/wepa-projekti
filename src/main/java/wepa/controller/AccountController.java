package wepa.controller;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountDatabase accountDatabase;
    
    @RequestMapping(method = RequestMethod.POST)
    public String postAccount(@RequestParam String name) throws SQLException {
        accountDatabase.create(name);
        return "redirect:/";
    }
    
}
