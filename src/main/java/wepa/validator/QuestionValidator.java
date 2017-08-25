package wepa.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.database.OptionDatabase;
import wepa.domain.Question;

//Validates a question and returns errors if there are any.
@Profile("production")
@Component
public class QuestionValidator {
    
    @Autowired
    private OptionDatabase optionDatabase;

    public List<String> validateQuestion(Question q) {
        List<String> errors = new ArrayList();
        errors.addAll(validateName(q.getName()));
        errors.addAll(validateOptions(q));
        return errors;
    }
    
    //Validates questions name
    private List<String> validateName(String name) {
        List<String> errors = new ArrayList();
        if (name == null || name.isEmpty() || name.length() < 3) {
            errors.add("Question's name must be at least 3 characters long!");
        }
        if (name.length() > 900) {
            errors.add("Question name too long!");
        }
        return errors;
    }
    
    //Validates that there are enough options for question to be set public
    private List<String> validateOptions(Question q) {
        List<String> errors = new ArrayList();
        if (q.isPublished()) {
            if (optionDatabase.findByQuestion(q.getId()).size() < 4) {
                errors.add("Question must have 4 options before you can publish it!");
            }
        }
        return errors;
    }
    
}
