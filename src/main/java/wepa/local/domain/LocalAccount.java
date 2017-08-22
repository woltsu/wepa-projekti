package wepa.local.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Profile("default")
//@Entity
public class LocalAccount extends AbstractPersistable<Long> {

    private String username;
    private String password;
    private String salt;
    @OneToMany(cascade = CascadeType.ALL)
    private List<LocalQuestion> questions;
    @OneToMany(cascade = CascadeType.ALL)
    private List<LocalAnswer> answers;
    private boolean admin;
    @OneToOne
    private LocalStat stat;
    @Id
    private Long id;

    public LocalAccount() {
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

    public void setOnlyPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void addQuestion(LocalQuestion question) {
        if (this.questions == null) {
            this.questions = new ArrayList();
        }
        this.questions.add(question);
    }

    public List<LocalQuestion> getQuestions() {
        return questions;
    }

    public List<LocalAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<LocalAnswer> answers) {
        this.answers = answers;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public LocalStat getStat() {
        return stat;
    }

    public void setStat(LocalStat stat) {
        this.stat = stat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocalAccount other = (LocalAccount) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
