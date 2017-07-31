package wepa.database;

import java.sql.Connection;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitDatabase {
    
    @Autowired
    private BasicDataSource dataSource;
    
    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection() ) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (name varchar(25));");
            st.executeUpdate("INSERT INTO Account (name) VALUES ('testi');");
            st.executeUpdate("INSERT INTO Account (name) VALUES ('pekka');");
            st.executeUpdate("INSERT INTO Account (name) VALUES ('juuso');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
