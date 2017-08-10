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
import org.springframework.stereotype.Component;
import wepa.domain.Option;

@Profile("production")
@Component
public class OptionDatabase {

    @Autowired
    private BasicDataSource dataSource;

    @PostConstruct
    private void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Option (id SERIAL PRIMARY KEY, "
                    + "value varchar(100), correct boolean, "
                    + "question_id integer REFERENCES Question ON DELETE CASCADE);");
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Option> findByQuestion(int question_id) {
        List<Option> result = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Option WHERE question_id = ?");
            ps.setInt(1, question_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Option o = new Option();
                o.setId(rs.getInt("id"));
                o.setValue(rs.getString("value"));
                o.setQuestion_id(rs.getInt("question_id"));
                o.setCorrect(rs.getBoolean("correct"));
                result.add(o);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void create(String value, boolean correct, int question_id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Option (value, correct, account_id) VALUES (?, ?, ?)");
            ps.setString(1, value);
            ps.setBoolean(2, correct);
            ps.setInt(3, question_id);
            ps.execute();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
