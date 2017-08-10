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
import wepa.domain.Question;

@Profile("production")
@Component
public class QuestionDatabase {

    @Autowired
    private BasicDataSource dataSource;

    private Connection conn;

    public QuestionDatabase() {
//        this.conn = dataSource.getConnection();
    }

    @PostConstruct
    private void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Question (id SERIAL PRIMARY KEY, "
                    + "name varchar(100) NOT NULL, "
                    + "account_id integer REFERENCES Account ON DELETE CASCADE);");
//            create("test", 1);
            st.close();
            conn.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public List<Question> findByAccount(int account_id) {
        List<Question> tests = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Question WHERE account_id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question t = new Question();
                t.setAccount(account_id);
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                tests.add(t);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tests;
    }

//    @Async
    public void create(String name, int account_id) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Question (name, account_id) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setInt(2, account_id);
            ps.execute();
            conn.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Question findOne(int id) {
        Question q = new Question();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Question WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            int question_id = rs.getInt("id");
            String name = rs.getString("name");
            int account = rs.getInt("account_id");
            q.setId(id);
            q.setName(name);
            q.setAccount(account);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return q;
    }

}
