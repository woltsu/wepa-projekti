package wepa.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import wepa.database.AccountDatabase;
import wepa.domain.TestAccount;
import wepa.repository.AccountRepository;

@Controller
@RequestMapping("*")
public class DefaultController {
    
//    @Autowired
//    private AccountRepository accountRepository;
//    
//    @PostConstruct
//    public void init() {
//        Account a = new Account();
//        a.setUsername("user");
//        a.setPassword("user");
//        accountRepository.save(a);
//    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
  
    
}
