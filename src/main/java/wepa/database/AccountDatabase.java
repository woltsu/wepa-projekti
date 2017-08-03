package wepa.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.domain.Account;

@Component
@Profile("production")
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (username varchar(255), password varchar(255));");
            create("user", "user");
            create("HELLO", "passu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account findByUsername(String username) {
        Account result = new Account();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.setUsername(rs.getString("username"));
                result.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<Account> getUsers() {
        ArrayList<Account> users = new ArrayList();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                users.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void create(String username, String password) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Account (username, password) VALUES (?, ?)");
            Account a = new Account();
            a.setUsername(username);
            a.setPassword(password);
            ps.setString(1, username);
            ps.setString(2, a.getPassword());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
