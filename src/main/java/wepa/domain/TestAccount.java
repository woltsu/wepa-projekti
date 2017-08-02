package wepa.domain;

import javax.persistence.Entity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Profile("default")
@Entity
public class TestAccount extends AbstractPersistable<Long> {
    
    private String username;
    private String password;
    private String salt;
    
    public TestAccount() {
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getSalt() {
        return salt;
    }
    
}
