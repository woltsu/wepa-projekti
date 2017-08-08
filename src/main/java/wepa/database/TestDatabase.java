package wepa.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import wepa.domain.Test;

@Profile("production")
@Component
public class TestDatabase {

    @Autowired
    private BasicDataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Test (id SERIAL PRIMARY KEY, "
                    + "name varchar(50) NOT NULL, account_id integer NOT NULL, "
                    + "FOREIGN KEY (account_id) REFERENCES Account (id));");
//            create("test", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Test> findByAccount(int account_id) {
        List<Test> tests = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Test WHERE account_id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Test t = new Test();
                t.setAccount(account_id);
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                tests.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tests;
    }

    @Async
    public void create(String name, int account_id) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Test (name, account_id) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setInt(2, account_id);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
