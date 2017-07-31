package wepa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    
    @Autowired
    private BasicDataSource dataSource;
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) throws SQLException {
        model.addAttribute("name", "olli");
        
        Statement statement = (Statement) dataSource.getConnection().createStatement();
        String insert = "INSERT INTO User VALUES ('Pekka')";
        String get = "SELECT * FROM User";
        
        statement.executeUpdate(insert);
        ResultSet rs = statement.executeQuery(get);
        
        ArrayList<String> users = new ArrayList();
        while (rs.next()) {
            users.add(rs.getString("name"));
        }
        
        model.addAttribute("users", users);
        
        return "index";
    }
    
}
