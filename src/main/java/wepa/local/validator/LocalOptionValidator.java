package wepa.local.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.local.domain.LocalOption;
import wepa.local.service.LocalOptionService;

@Profile("default")
@Component
public class LocalOptionValidator {
    
    @Autowired
    private LocalOptionService optionService;

    public List<String> validateOption(LocalOption o) {
        List<String> errors = new ArrayList();
        errors.addAll(validateValue(o.getValue()));
        if (optionService.hasAlreadyACorrectOption(o.getLocalQuestion().getId()) && o.isCorrect()) {
            errors.add("Question already has a correct option!");
        }
        if (optionService.hasMaxFalseOptions(o.getLocalQuestion().getId()) && !o.isCorrect()) {
            errors.add("Question has maximum number of incorrect options!");
        }
        return errors;
    }
    
    private List<String> validateValue(String value) {
        List<String> errors = new ArrayList();
        if (value == null || value.isEmpty()) {
            errors.add("Option value must not be empty!");
        }
        if (value.length() > 100) {
            errors.add("Option value too long!");
        }
        return errors;
    }
    
}
