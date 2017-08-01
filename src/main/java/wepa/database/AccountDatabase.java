package wepa.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

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
