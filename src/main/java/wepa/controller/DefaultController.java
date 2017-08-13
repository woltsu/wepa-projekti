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
import wepa.database.QuestionDatabase;
import wepa.domain.Question;
import wepa.service.AccountService;

@Profile("production")
@Controller
@RequestMapping("*")
public class DefaultController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private QuestionDatabase questionDatabase;
    
    @Autowired
    private AccountDatabase accountDatabase;
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        List<Question> questions = questionDatabase.getTenPublishedLatest();
        for (Question question : questions) {
            question.setPublisher(accountDatabase.findOne(question.getAccount()));
        }
        model.addAttribute("questions", questions);
        return "index";
    }
    
    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswerPage(Model model, @PathVariable int id) {
        Question q = questionDatabase.findOne(id);
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "answer";
    }

}
