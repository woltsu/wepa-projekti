package wepa.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Account;
import wepa.domain.Question;
import wepa.service.AccountService;

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

    @RequestMapping(method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name, @PathVariable String user) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
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

    @RequestMapping(value = "/{user}/questions/{id}/toggle", method = RequestMethod.POST)
    public String toggleQuestion(@RequestParam int id, @PathVariable String user) {
        Account self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        Question q = questionDatabase.findOne(id);
        q.setPublished(!q.isPublished());
        questionDatabase.save(q);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

}
