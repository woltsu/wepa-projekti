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

//Stores accounts into heroku's database
@Profile("production")
@Component
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

    //On initialization creates an account table. Also creates a test account, account for bot and an admin account.
    @PostConstruct
    private void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (id SERIAL PRIMARY KEY, "
                    + "username varchar(255) NOT NULL, password varchar(255) NOT NULL, "
                    + "salt varchar(255), admin boolean);");
            create("user", "user");
            Account bot = new Account();
            bot.setUsername("Question bot");
            bot.setPassword("very secret password");
            create(bot.getUsername(), bot.getPassword());
            create("admin", "salasana", true);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Finds one account from the database using account_id.
    public Account findOne(int account_id) {
        Account result = new Account();
        result.setUsername("test");
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account WHERE id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            result.setId(account_id);
            result.setUsername(rs.getString("username"));
            result.setAdmin(rs.getBoolean("admin"));
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Finds an account from the database using account's username.
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
                result.setAdmin(rs.getBoolean("admin"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.getUsername() == null) {
            return null;
        }

        return result;
    }

    //Returns all of the accounts.
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
                a.setAdmin(rs.getBoolean("admin"));
                users.add(a);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    //Creates an account.
    public void create(String username, String password, boolean admin) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Account (username, password, salt, admin) VALUES (?, ?, ?, ?)");
            Account a = new Account();
            a.setUsername(username);
            //Password must first be sit in order for BCrypt to work.
            a.setPassword(password);
            a.setAdmin(admin);
            ps.setString(1, username);
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getSalt());
            ps.setBoolean(4, a.isAdmin());
            ps.execute();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Creates an account without the admin parameter (false by default)
    public void create (String username, String password) {
        create(username, password, false);
    }

}
