package wepa.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.domain.Account;
import wepa.domain.Answer;

@Profile("production")
@Component
public class AnswerDatabase {

    @Autowired
    private BasicDataSource dataSource;
    
    @Autowired
    private AccountDatabase accountDatabase;
    
    @Autowired
    private OptionDatabase optionDatabase;

    @PostConstruct
    private void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Answer (id SERIAL PRIMARY KEY, question_id integer, correct boolean, "
                    + "account_id integer REFERENCES Account ON DELETE CASCADE, "
                    + "option_id integer REFERENCES Option ON DELETE CASCADE);");
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(int question_id, int account_id, int option_id, boolean correct) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Answer (question_id, account_id, option_id, correct) VALUES (?, ?, ?, ?)");
            ps.setInt(1, question_id);
            ps.setInt(2, account_id);
            ps.setInt(3, option_id);
            ps.setBoolean(4, correct);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Answer findByAccountAndQuestionId(Account account, int question_id) {
        Answer a = new Answer();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Answer WHERE question_id = ? AND account_id = ?");
            ps.setInt(1, question_id);
            ps.setInt(2, account.getId());
            ResultSet rs = ps.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            a.setAccount(account);
            a.setCorrect(rs.getBoolean("correct"));
            a.setOption(optionDatabase.findOne(rs.getInt("option_id")));
            a.setQuestionId(question_id);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }
}
