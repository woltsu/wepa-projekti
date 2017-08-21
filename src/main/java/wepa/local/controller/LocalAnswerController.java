package wepa.local.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.domain.LocalStat;
import wepa.local.repository.LocalAnswerRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.repository.LocalStatRepository;
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
    
    @Autowired
    private LocalStatRepository statRepository;

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswer(Model model, @PathVariable(required = false) Long id) {
        if (answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }
        if (id < 0 || id == null) {
            return "redirect:/";
        }
        LocalQuestion q = questionRepository.findOne(id);
        if (q == null || !q.isPublished()) {
            return "redirect:/";
        }
        List<LocalOption> options = optionRepository.findByLocalQuestion(q);
        LocalOption correctOption = null;
        for (LocalOption option : options) {
            if (option.isCorrect()) {
                correctOption = option;
                break;
            }
        }
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("question", q);
        model.addAttribute("options", options);
        model.addAttribute("correctOption", correctOption);
        return "answer";
    }

    @RequestMapping(value = "/question/{id}", method = RequestMethod.POST)
    public String postAnswer(Model model, @PathVariable Long id, @RequestParam Long option_id) {
        LocalAnswer answer = new LocalAnswer();
        LocalAccount self = accountService.getAuthenticatedAccount();
        LocalStat stat = statRepository.findByAccount(self);
        if (optionRepository.findOne(option_id).isCorrect()) {
            answer.setCorrect(true);
            stat.setCorrectAnswers(stat.getCorrectAnswers() + 1);
        } else {
            answer.setCorrect(false);
            stat.setWrongAnswers(stat.getWrongAnswers() + 1);
        }
        answer.setAccount(self);
        answer.setOption(optionRepository.findOne(option_id));
        answer.setQuestionId(id);
        answerRepository.save(answer);
        LocalOption option = optionRepository.findOne(option_id);
        option.addAnswer(answer);
        optionRepository.save(option);
        statRepository.save(stat);
        return "redirect:/question/" + id;
    }

}
