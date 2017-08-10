package wepa.local.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import wepa.local.domain.LocalOption;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;

@Profile("default")
@Service
public class LocalOptionService {

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalOptionRepository optionRepository;

    public boolean hasAlreadyACorrectOption(Long id) {
        List<LocalOption> options = optionRepository.findByLocalQuestion(questionRepository.findOne(id));
        for (LocalOption option : options) {
            if (option.isCorrect()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasMaxFalseOptions(Long id) {
        List<LocalOption> options = optionRepository.findByLocalQuestion(questionRepository.findOne(id));
        int howMany = 0;
        for (LocalOption option : options) {
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
