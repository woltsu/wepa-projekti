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

    //If the user tries to go to a non-existing page, they will be redirected to the front page.
    @RequestMapping(method = RequestMethod.GET)
    public String redirectHome() {
        return "redirect:/?page=1";
    }

    //Front page. 1 page contains 10 questions ordered by date. If page is invalid, it will be changed to 1.
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("page", page);
        List<Question> questions = questionDatabase.getTenPublishedLatest((page - 1) * 10);
        for (Question question : questions) {
            question.setPublisher(accountDatabase.findOne(question.getAccount()));
        }
        if ((questions.isEmpty() && page > 1) || page < 1) {
            return "redirect:/?page=1";
        }
        model.addAttribute("questions", questions);
        return "index";
    }

    //Changes the page with +1
    @RequestMapping(value = "/nextPage", method = RequestMethod.GET)
    public String nextPage(@RequestParam int page) {
        return "redirect:/?page=" + (page + 1);
    }
    
    //Changes the page with -1
    @RequestMapping(value = "/prevPage", method = RequestMethod.GET)
    public String prevPage(@RequestParam int page) {
        return "redirect:/?page=" + (page - 1);
    }

    //Used to search questions. If invalid, the default value will be -1 which notifies future controller of a wrong id.
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchQuestion(@RequestParam(defaultValue = "-1") int question_id) {
        return "redirect:/question/" + question_id;
    }

}
