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
    public String redirectHome() {
        return "redirect:/?page=1";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("page", page);
        if (page < 1) {
            return "redirect:/?page=1";
        }
        List<Question> questions = questionDatabase.getTenPublishedLatest((page - 1) * 10);
        for (Question question : questions) {
            question.setPublisher(accountDatabase.findOne(question.getAccount()));
        }
        if (questions.isEmpty() && page > 1) {
            return "redirect:/?page=1";
        }
        model.addAttribute("questions", questions);
        return "index";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/nextPage")
    public String nextPage(@RequestParam int page) {
        return "redirect:/?page=" + (page + 1);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/prevPage")
    public String prevPage(@RequestParam int page) {
        return "redirect:/?page=" + (page - 1);
    }
    
}
