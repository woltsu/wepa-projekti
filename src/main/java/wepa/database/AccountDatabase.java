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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import wepa.domain.Account;

@Profile("production")
@Component
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (id SERIAL PRIMARY KEY, "
                            + "username varchar(255) NOT NULL, password varchar(255) NOT NULL, "
                            + "salt varchar(255));");
            create("user", "user");
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
                result.setOnlyPassword(rs.getString("password"));
                result.setSalt(rs.getString("salt"));
                result.setId(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (result.getUsername() == null) {
            return null;
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
                a.setOnlyPassword(rs.getString("password"));
                a.setSalt(rs.getString("salt"));
                users.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    
    @Async
    public void create(String username, String password) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Account (username, password, salt) VALUES (?, ?, ?)");
            Account a = new Account();
            a.setUsername(username);
            a.setPassword(password);
            ps.setString(1, username);
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getSalt());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
