package wepa.local.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
@Entity
public class LocalAnswer extends AbstractPersistable<Long> {

    @ManyToOne
    private LocalAccount account;

    @ManyToOne
    private LocalOption option;

    private Long questionId;

    private boolean correct;

    public LocalAnswer() {
    }

    public LocalAccount getAccount() {
        return account;
    }

    public void setAccount(LocalAccount account) {
        this.account = account;
    }

    public LocalOption getOption() {
        return option;
    }

    public void setOption(LocalOption option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

}
