package wepa.local.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Profile("default")
@Entity
public class LocalOption extends AbstractPersistable<Long> {

    private String optionValue;
    private boolean correct;
    @ManyToOne
    private LocalQuestion localQuestion;
    @OneToMany(cascade = CascadeType.ALL)
    private List<LocalAnswer> answers;
    @Id
    private Long id;

    public LocalOption() {
        this.answers = new ArrayList();
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

    public List<LocalAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<LocalAnswer> answers) {
        this.answers = answers;
    }
    
    public void addAnswer(LocalAnswer a) {
        if (this.answers == null) {
            this.answers = new ArrayList();
        }
        this.answers.add(a);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
