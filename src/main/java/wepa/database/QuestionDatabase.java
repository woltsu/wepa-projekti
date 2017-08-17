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
                    + "name varchar(1000) NOT NULL, published boolean, date timestamp, "
                    + "account_id integer REFERENCES Account ON DELETE CASCADE);");
//            create("test", 1);
            st.close();
            conn.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public List<Question> findByAccount(int account_id) {
        List<Question> questions = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Question WHERE account_id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            questions = createQuestions(rs);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

//    @Async
    public void create(String name, int account_id) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Question (name, account_id, published, date) "
                    + "VALUES (?, ?, false, CURRENT_TIMESTAMP)");
            ps.setString(1, name);
            ps.setInt(2, account_id);
            ps.execute();
            conn.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void create(String name, int account_id, boolean published) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Question (name, account_id, published, date) "
                    + "VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
            ps.setString(1, name);
            ps.setInt(2, account_id);
            ps.setBoolean(3, published);
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
            q.setDate(rs.getTimestamp("date"));
            q.setPublished(rs.getBoolean("published"));
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return q;
    }

    public void save(Question q) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE Question SET published = ? WHERE id = ?");
            ps.setBoolean(1, q.isPublished());
            ps.setInt(2, q.getId());
            ps.execute();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Question> getTenPublishedLatest() {
        List<Question> questions = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Question WHERE published = true ORDER BY date DESC");
            ResultSet rs = ps.executeQuery();
            questions = createQuestions(rs);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    private List<Question> createQuestions(ResultSet rs) {
        List<Question> questions = new ArrayList();
        try {
            while (rs.next()) {
                Question q = new Question();
                q.setAccount(rs.getInt("account_id"));
                q.setId(rs.getInt("id"));
                q.setName(rs.getString("name"));
                q.setDate(rs.getTimestamp("date"));
                q.setPublished(rs.getBoolean("published"));
                questions.add(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void delete(int question_id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Question WHERE id = ?");
            ps.setInt(1, question_id);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
