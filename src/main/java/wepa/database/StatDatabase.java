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
import wepa.domain.Stat;

@Profile("production")
@Component
public class StatDatabase {

    @Autowired
    private BasicDataSource dataSource;
    
    @Autowired
    private AccountDatabase accountDatabase;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Stat (id SERIAL PRIMARY KEY, "
                    + "correctAnswers int, wrongAnswers int, "
                    + "account_id integer REFERENCES Account ON DELETE CASCADE);");
            create(accountDatabase.findByUsername("Question bot").getId());
            create(accountDatabase.findByUsername("admin").getId());
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Stat> getRanks() {
        List<Stat> orderedByStats = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Stat ORDER BY correctAnswers DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Stat s = new Stat();
                s.setAccount_id(rs.getInt("account_id"));
                s.setCorrectAnswers(rs.getInt("correctAnswers"));
                s.setWrongAnswers(rs.getInt("wrongAnswers"));
                s.setId(rs.getInt("id"));
                orderedByStats.add(s);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderedByStats;
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
            stat.setId(rs.getInt("id"));
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
