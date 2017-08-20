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
import wepa.domain.Stat;

@Profile("production")
@Component
public class StatDatabase {

    @Autowired
    private BasicDataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Stat (id SERIAL PRIMARY KEY, "
                    + "correctAnswers int, wrongAnswers int, "
                    + "account_id integer REFERENCES Account ON DELETE CASCADE);");
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stat findByAccount(int account_id) {
        Stat stat = new Stat();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Stat WHERE account_id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            stat.setAccount_id(account_id);
            stat.setCorrectAnswers(rs.getInt("correctAnswers"));
            stat.setWrongAnswers(rs.getInt("wrongAnswers"));
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stat;
    }

    public void create(int account_id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareCall("INSERT INTO Stat (correctAnswers, wrongAnswers, account_id) VALUES (0, 0, ?)");
            ps.setInt(1, account_id);
            ps.execute();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Stat stat) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE Stat SET correctAnswers = ?, wrongAnswers = ? WHERE id = ?");
            ps.setInt(1, stat.getCorrectAnswers());
            ps.setInt(2, stat.getWrongAnswers());
            ps.setInt(3, stat.getId());
            ps.execute();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
