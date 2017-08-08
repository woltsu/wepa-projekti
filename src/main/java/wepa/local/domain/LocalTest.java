package wepa.local.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("local")
//@Entity
public class LocalTest extends AbstractPersistable<Long> {

    private String name;
    @ManyToOne
    private LocalAccount localAccount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalAccount getLocalAccount() {
        return localAccount;
    }

    public void setLocalAccount(LocalAccount localAccount) {
        this.localAccount = localAccount;
    }

}
