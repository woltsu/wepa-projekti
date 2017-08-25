package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Option;

@Profile("production")
@Service
public class OptionService {

    @Autowired
    private OptionDatabase optionDatabase;
    
    @Autowired
    private QuestionDatabase questionDatabase;
    
    //If a question already has a correct option, returns true. Else returns false.
    public boolean hasAlreadyACorrectOption(int question_id) {
        List<Option> options = optionDatabase.findByQuestion(question_id);
        for (Option option : options) {
            if (option.isCorrect()) {
                return true;
            }
        }
        return false;
    }
    
    //If a question already has 3 false options, returns true. Else returns false.
    public boolean hasMaxFalseOptions(int question_id) {
        List<Option> options = optionDatabase.findByQuestion(question_id);
        int howMany = 0;
        for (Option option : options) {
            if (!option.isCorrect()) {
                howMany++;
            }
        }
        if (howMany >= 3) {
            return true;
        }
        return false;
    }
    
}
