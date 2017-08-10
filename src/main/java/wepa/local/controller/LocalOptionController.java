package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalOption;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.service.LocalOptionService;

@Profile("default")
@Controller
public class LocalOptionController {

    @Autowired
    private LocalOptionRepository optionRepository;
    
    @Autowired
    private LocalQuestionRepository questionRepository;
    
    @Autowired
    private LocalOptionService optionService;
    
    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.POST)
    public String postOption(Model model, @PathVariable Long id, LocalOption option, @PathVariable String user) {
        if ((optionService.hasAlreadyACorrectOption(id) && option.isCorrect()) || (optionService.hasMaxFalseOptions(id) && !option.isCorrect())) {
            return "redirect:/" + user + "/questions/" + id;
        }
        option.setLocalQuestion(questionRepository.findOne(id));
        optionRepository.save(option);
        return "redirect:/" + user + "/questions/" + id;
    }
    
    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.DELETE)
    public String deleteOption(Model model, @PathVariable Long id, @RequestParam Long option_id, @PathVariable String user) {
        optionRepository.delete(option_id);
        return "redirect:/" + user + "/questions/" + id;
    }
    
}
