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
import wepa.database.QuestionDatabase;
import wepa.domain.Account;
import wepa.service.AccountService;

@Profile("production")
@Controller
@RequestMapping(value = "/{user}/questions")
public class QuestionController {

    @Autowired
    private QuestionDatabase questionDatabase;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String getQuestions(Model model) {
        Account self = accountService.getAuthenticatedAccount();
        model.addAttribute("user", self);
        model.addAttribute("questions", questionDatabase.findByAccount(self.getId()));
        return "questions";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name) {
        Account self = accountService.getAuthenticatedAccount();
        questionDatabase.create(name, self.getId());
        return "redirect:/" + self.getUsername() + "questions";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable int id) {
        model.addAttribute("question", questionDatabase.findOne(id));
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "question";
    }

}
