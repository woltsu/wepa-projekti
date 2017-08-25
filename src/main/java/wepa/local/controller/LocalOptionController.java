package wepa.local.controller;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.service.LocalAccountService;
import wepa.local.service.LocalOptionService;
import wepa.local.validator.LocalOptionValidator;

@Profile("default")
@Controller
public class LocalOptionController {

    @Autowired
    private LocalOptionRepository optionRepository;

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalOptionService optionService;

    @Autowired
    private LocalOptionValidator optionValidator;

    @Autowired
    private LocalAccountService accountService;

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.POST)
    public String postOption(Model model, @PathVariable Long id, LocalOption option, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        LocalQuestion q = questionRepository.findOne(id);
        q.getOptions().add(option);
        option.setLocalQuestion(questionRepository.findOne(id));

        List<String> errors = optionValidator.validateOption(option);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", self);
            model.addAttribute("question", questionRepository.findOne(id));
            model.addAttribute("options", optionRepository.findByLocalQuestion(questionRepository.findOne(id)));
            return "question";
        }

        optionRepository.save(option);
        return "redirect:/" + user + "/questions/" + id;
    }

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.DELETE)
    public String deleteOption(Model model, @PathVariable Long id, @RequestParam Long option_id, @PathVariable String user) {
        LocalQuestion q = questionRepository.findOne(id);
        LocalOption o = optionRepository.findOne(option_id);
        q.getOptions().remove(o);
        optionRepository.delete(o);
        q.setPublished(false);
        questionRepository.save(q);
        return "redirect:/" + user + "/questions/" + id;
    }

}
