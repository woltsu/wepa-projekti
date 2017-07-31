package wepa.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDatabase {

    @Autowired
    private BasicDataSource dataSource;

    public ArrayList<String> getUsers() throws SQLException {
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Account;");
        ArrayList<String> users = new ArrayList();
        while (rs.next()) {
            users.add(rs.getString("name"));
        }
        return users;
    }

    public void create(String name) throws SQLException {
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO Account (name) VALUES ('" + name + "');");
    }

}
