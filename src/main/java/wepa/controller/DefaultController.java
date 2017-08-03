package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.database.AccountDatabase;

@Controller
@RequestMapping("*")
public class DefaultController {
    
    @Autowired
    private AccountDatabase accountDatabase;
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", accountDatabase.findByUsername("HELLO").getUsername());
        model.addAttribute("users", accountDatabase.getUsers());
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String test() {
        accountDatabase.create("TOIMII", "JES!!");
        return "redirect:/login";
    }
    
  
    
}
