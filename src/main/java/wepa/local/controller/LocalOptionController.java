package wepa.local.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
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
    @Transactional
    public String postOption(Model model, @PathVariable Long id, LocalOption option, @PathVariable String user) {
        if ((optionService.hasAlreadyACorrectOption(id) && option.isCorrect()) || (optionService.hasMaxFalseOptions(id) && !option.isCorrect())) {
            return "redirect:/" + user + "/questions/" + id;
        }
        LocalQuestion q = questionRepository.findOne(id);
        q.getOptions().add(option);
        option.setLocalQuestion(questionRepository.findOne(id));
        optionRepository.save(option);
        return "redirect:/" + user + "/questions/" + id;
    }

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.DELETE)
    @Transactional
    public String deleteOption(Model model, @PathVariable Long id, @RequestParam Long option_id, @PathVariable String user) {
        LocalQuestion q = questionRepository.findOne(id);
        LocalOption o = optionRepository.findOne(option_id);
        q.getOptions().remove(o);
        optionRepository.delete(o);
        return "redirect:/" + user + "/questions/" + id;
    }

}
