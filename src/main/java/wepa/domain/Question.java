package wepa.domain;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import wepa.TimeCount;
import wepa.database.AccountDatabase;

@Profile("production")
public class Question {

    @Autowired
    private AccountDatabase accountDatabase;

    private int id;
    private String name;
    private int account_id;
    private Timestamp date;
    private boolean published;
    private Account publisher;

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

    public Account getPublisher() {
        return publisher;
    }

    public void setPublisher(Account publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        TimeCount timeCount = new TimeCount();
        Date d = new Date(date.getTime());
        return timeCount.howLongAgo(d);
    }

}
