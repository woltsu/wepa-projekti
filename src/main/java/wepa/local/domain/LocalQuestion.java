package wepa.local.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
//@Entity
public class LocalQuestion extends AbstractPersistable<Long> {

    private String name;
    @ManyToOne
    private LocalAccount localAccount;
    @OneToMany
    private List<LocalOption> options;

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
    
    public void addOption(LocalOption option) {
        if (options == null) {
            options = new ArrayList();
        }
        options.add(option);
    }

}
