package wepa.local.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.local.domain.LocalQuestion;

@Profile("default")
@Component
public class LocalQuestionValidator {

    public List<String> validateQuestion(LocalQuestion q) {
        List<String> errors = new ArrayList();
        errors.addAll(validateName(q.getName()));
        errors.addAll(validateOptions(q));
        return errors;
    }
    
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
    
    private List<String> validateOptions(LocalQuestion q) {
        List<String> errors = new ArrayList();
        if (q.isPublished()) {
            if (q.getOptions().size() < 4) {
                errors.add("Question must have 4 options before you can publish it!");
            }
        }
        return errors;
    }

}
