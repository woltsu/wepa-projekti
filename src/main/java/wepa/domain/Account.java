package wepa.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Profile("production")
public class Account {

    private String username;
    private String password;
    private String salt;

    public Account() {
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
