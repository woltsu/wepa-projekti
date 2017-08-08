package wepa.domain;

import org.springframework.context.annotation.Profile;

@Profile("production")
public class Test {

    private int id;
    private String name;
    private int account_id;

    public Test() {
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

}
