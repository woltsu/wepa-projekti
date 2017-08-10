package wepa.database;

import java.sql.Connection;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("production")
@Component
public class OptionDatabase {

    @Autowired
    private BasicDataSource basicDataSource;
    
    @PostConstruct
    private void init() {
        try (Connection conn = basicDataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Option (id SERIAL PRIMARY KEY, "
                    + "value varchar(100), correct boolean, "
                    + "question_id integer REFERENCES Question ON DELETE CASCADE);");            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
