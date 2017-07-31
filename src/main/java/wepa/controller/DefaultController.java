package wepa.controller;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("*")
public class DefaultController {
    
//    @Autowired
//    private BasicDataSource dataSource;
//    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("name", "olli");
        return "index";
    }
    
}
