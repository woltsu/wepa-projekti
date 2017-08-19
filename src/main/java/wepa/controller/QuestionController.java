package wepa.controller;

import java.util.List;
import javax.annotation.PostConstruct;
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
import wepa.domain.Account;
import wepa.domain.Question;
import wepa.service.AccountService;
import wepa.validator.QuestionValidator;

@Profile("production")
@Controller
@RequestMapping(value = "/{user}/questions")
public class QuestionController {

    @Autowired
    private QuestionDatabase questionDatabase;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OptionDatabase optionDatabase;

    @Autowired
    private AnswerDatabase answerDatabase;

    @Autowired
    private QuestionValidator questionValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String getQuestions(Model model, @PathVariable String user) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        model.addAttribute("user", self);
        model.addAttribute("questions", questionDatabase.findByAccount(self.getId()));
        return "questions";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteQuestion(@PathVariable String user, @RequestParam int id) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername()) && !self.isAdmin()) {
            return "redirect:/";
        }
        questionDatabase.delete(id);
        if (self.isAdmin()) {
            return "redirect:/";
        }
        return "redirect:/" + self.getUsername() + "/questions";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name, @PathVariable String user, Model model) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }

        Question q = new Question();
        q.setName(name);

        List<String> errors = questionValidator.validateQuestion(q);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", self);
            model.addAttribute("questions", questionDatabase.findByAccount(self.getId()));
            return "questions";
        }

        questionDatabase.create(name, self.getId());
        return "redirect:/" + self.getUsername() + "/questions";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable int id, @PathVariable String user) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        model.addAttribute("question", questionDatabase.findOne(id));
        model.addAttribute("user", self);
        model.addAttribute("options", optionDatabase.findByQuestion(id));
        return "question";
    }

    @RequestMapping(value = "/{id}/toggle", method = RequestMethod.POST)
    public String toggleQuestion(@RequestParam int question_id, @PathVariable String user, Model model) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        Question q = questionDatabase.findOne(question_id);
        q.setPublished(!q.isPublished());

        List<String> errors = questionValidator.validateQuestion(q);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("question", questionDatabase.findOne(question_id));
            model.addAttribute("user", self);
            model.addAttribute("options", optionDatabase.findByQuestion(question_id));
            return "question";
        }

        questionDatabase.save(q);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

}
