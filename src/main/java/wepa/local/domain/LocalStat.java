package wepa.local.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("defaul")
@Entity(name = "stat")
@Table(name = "stat")
public class LocalStat extends AbstractPersistable<Long> {

    @Id
    private Long id;

    private int correctAnswers;
    private int wrongAnswers;
    @OneToOne
    private LocalAccount account;

    public LocalStat() {
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public LocalAccount getAccount() {
        return account;
    }

    public void setAccount(LocalAccount account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
