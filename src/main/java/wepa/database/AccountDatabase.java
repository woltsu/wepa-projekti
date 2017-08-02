package wepa.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wepa.domain.Account;

@Component
@Profile("production")
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

    public Account findByUsername(String username) {
        Account result = new Account();
        try {
            Connection conn = dataSource.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Account WHERE username = " + username + ";");
            while (rs.next()) {
                result.setUsername(rs.getString("username"));
                result.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList();
        try {
            Connection conn = dataSource.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Account;");
            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void create(String name) {
        try {
            Connection conn = dataSource.getConnection();
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO Account (name) VALUES ('" + name + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
