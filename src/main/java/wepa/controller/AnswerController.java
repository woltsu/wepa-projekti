package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.AnswerDatabase;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Answer;
import wepa.domain.Question;
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

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswer(Model model, @PathVariable int id) {
        if (answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerDatabase.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }

        Question q = questionDatabase.findOne(id);
        if (!q.isPublished()) {
            return "redirect:/";
        }
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("question", q);
        model.addAttribute("options", optionDatabase.findByQuestion(q.getId()));
        return "answer";
    }

    @RequestMapping(value = "/question/{id}", method = RequestMethod.POST)
    public String postAnswer(Model model, @PathVariable int id, @RequestParam int option_id) {
        boolean isCorrect = false;
        if (optionDatabase.findOne(option_id).isCorrect()) {
            isCorrect = true;
        }
        int accountId = accountService.getAuthenticatedAccount().getId();
        int optionId = optionDatabase.findOne(option_id).getId();
        answerDatabase.create(id, accountId, optionId, isCorrect);
        return "redirect:/question/" + id;
    }

}
