package wepa.local.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
@Entity
public class LocalOption extends AbstractPersistable<Long> {

    private String optionValue;
    private boolean correct;
    @ManyToOne
    private LocalQuestion localQuestion;

    public LocalOption() {
    }

    public String getValue() {
        return optionValue;
    }

    public void setValue(String value) {
        this.optionValue = value;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setLocalQuestion(LocalQuestion localQuestion) {
        this.localQuestion = localQuestion;
    }

    public LocalQuestion getLocalQuestion() {
        return localQuestion;
    }

}
