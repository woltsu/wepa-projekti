package wepa.controller;

import java.util.ArrayList;
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

    //Question answer page. If user tries to answer with an empty answer, they will be notified. Method also checks that 
    //question id is a proper id and the question is published.
    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswerPage(Model model, @PathVariable int id, @RequestParam(defaultValue = "1") int lastPage, 
            @RequestParam(defaultValue = "false") boolean err) {
        if (answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }
        
        if (id < 0) {
            return "redirect:/";
        }
        Question q = questionDatabase.findOne(id);
        if (q == null) {
            return "redirect:/";
        }
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
        if (err) {
            List<String> errors = new ArrayList();
            errors.add("Please choose an option");
            model.addAttribute("errors", errors);
        }
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("question", q);
        model.addAttribute("options", options);
        model.addAttribute("correctOption", correctOption);
        model.addAttribute("lastPage", lastPage);
        return "answer";
    }

    //Used to actually answer a question. Detects if user hasn't chosen any of the available options. Also updated 
    //account's stats with +1 correct answer or +1 wrong answer. Keeps track of the page from which user originally came from
    //so the user can smoothly go back to the correct page and doesn't have to browse f.ex. 5 pages again to get back to the
    //next available question.
    @RequestMapping(value = "/question/{id}", method = RequestMethod.POST)
    public String postAnswer(Model model, @PathVariable int id, @RequestParam(defaultValue = "-1") int option_id, 
            @RequestParam(defaultValue = "1") int lastPage) {
        if (option_id < 0) {
            return "redirect:/question/" + id + "?lastPage=" + lastPage + "&err=true";
        }
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
        return "redirect:/question/" + id + "/?lastPage=" + lastPage;
    }

}
