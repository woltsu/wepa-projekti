package wepa.database;

import java.sql.Connection;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class InitDatabase {
    
    @Autowired
    private BasicDataSource dataSource;
    
    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection() ) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Account (username varchar(25), password varchar(25));");
            st.executeUpdate("INSERT INTO Account (username, password) VALUES ('user', 'user');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
