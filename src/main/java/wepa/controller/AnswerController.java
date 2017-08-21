package wepa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AccountDatabase;
import wepa.database.AnswerDatabase;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.database.StatDatabase;
import wepa.domain.Account;
import wepa.domain.Answer;
import wepa.domain.Option;
import wepa.domain.Question;
import wepa.domain.Stat;
import wepa.service.AccountService;

@Profile("production")
@Controller
public class AnswerController {

    @Autowired
    private AnswerDatabase answerDatabase;

    @Autowired
    private QuestionDatabase questionDatabase;

    @Autowired
    private OptionDatabase optionDatabase;

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private StatDatabase statDatabase;
    
    @Autowired
    private AccountDatabase accountDatabase;

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswer(Model model, @PathVariable int id) {
        if (answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }

        Question q = questionDatabase.findOne(id);
        q.setPublisher(accountDatabase.findOne(q.getAccount()));
        if (!q.isPublished()) {
            return "redirect:/";
        }
        List<Option> options = optionDatabase.findByQuestion(q.getId());
        Option correctOption = null;
        for (Option option : options) {
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
    public String postAnswer(Model model, @PathVariable int id, @RequestParam int option_id) {
        boolean isCorrect = false;
        Account a = accountService.getAuthenticatedAccount();
        Stat stat = statDatabase.findByAccount(a.getId());
        if (optionDatabase.findOne(option_id).isCorrect()) {
            isCorrect = true;
            stat.setCorrectAnswers(stat.getCorrectAnswers() + 1);
        } else {
            stat.setWrongAnswers(stat.getWrongAnswers() + 1);
        }
        int accountId = accountService.getAuthenticatedAccount().getId();
        int optionId = optionDatabase.findOne(option_id).getId();
        answerDatabase.create(id, accountId, optionId, isCorrect);
        statDatabase.save(stat);
        return "redirect:/question/" + id;
    }

}
