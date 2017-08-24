package wepa.local.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
@Entity
public class LocalStat extends AbstractPersistable<Long> {

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

}
