package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAnswerRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.service.LocalAccountService;

@Profile("default")
@Controller
public class LocalAnswerController {
    
    @Autowired
    private LocalAnswerRepository answerRepository;
    
    @Autowired
    private LocalQuestionRepository questionRepository;
    
    @Autowired
    private LocalOptionRepository optionRepository;
    
    @Autowired
    private LocalAccountService accountService;

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswer(Model model, @PathVariable Long id) {
        if (answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }

        LocalQuestion q = questionRepository.findOne(id);
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("question", q);
        model.addAttribute("options", optionRepository.findByLocalQuestion(q));
        return "answer";
    }

    @RequestMapping(value = "/question/{id}", method = RequestMethod.POST)
    public String postAnswer(Model model, @PathVariable Long id, @RequestParam Long option_id) {
        LocalAnswer a = new LocalAnswer();
        if (optionRepository.findOne(option_id).isCorrect()) {
            a.setCorrect(true);
        } else {
            a.setCorrect(false);
        }
        a.setAccount(accountService.getAuthenticatedAccount());
        a.setOption(optionRepository.findOne(option_id));
        a.setQuestionId(id);
        answerRepository.save(a);
        LocalOption o = optionRepository.findOne(option_id);
        o.addAnswer(a);
        optionRepository.save(o);
        return "redirect:/question/" + id;
    }

}
