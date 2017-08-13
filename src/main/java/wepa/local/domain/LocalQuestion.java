package wepa.local.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.Cascade;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
@Entity
public class LocalQuestion extends AbstractPersistable<Long> {

    private String name;
    @ManyToOne
    private LocalAccount localAccount;
    @OneToMany(cascade = CascadeType.ALL)
    private List<LocalOption> options;
    @Column(name = "POST_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    private boolean published;

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

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public LocalAccount getPublisher() {
        return this.localAccount;
    }

    public void setOptions(List<LocalOption> options) {
        this.options = options;
    }

    public List<LocalOption> getOptions() {
        return options;
    }

}
