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

@Controller
@RequestMapping("*")
public class DefaultController {
    
    @Autowired
    private BasicDataSource dataSource;
    
    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection() ) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (name varchar(25));");
            st.executeUpdate("INSERT INTO Account (name) VALUES ('testi');");
        } catch (Exception e) {
        }
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) throws SQLException {
        model.addAttribute("name", "olli");
        
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Account;");
        ArrayList<String> users = new ArrayList();
        while (rs.next()) {
            users.add(rs.getString("name"));
        }
        model.addAttribute("users", users);
        
        return "index";
    }
    
}
