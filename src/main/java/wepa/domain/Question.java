package wepa.domain;

import java.sql.Timestamp;
import org.springframework.context.annotation.Profile;

@Profile("production")
public class Question {

    private int id;
    private String name;
    private int account_id;
    private Timestamp date;
    private boolean published;

    public Question() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account_id;
    }

    public void setAccount(int account) {
        this.account_id = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

}
