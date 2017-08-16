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
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Account;
import wepa.domain.Option;
import wepa.domain.Question;
import wepa.service.AccountService;
import wepa.service.OptionService;
import wepa.validator.OptionValidator;

@Profile("production")
@Controller
public class OptionController {

    @Autowired
    private OptionService optionService;

    @Autowired
    private QuestionDatabase questionDatabase;

    @Autowired
    private OptionDatabase optionDatabase;

    @Autowired
    private OptionValidator optionValidator;
    
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.POST)
    public String postOption(Model model, @PathVariable int id, Option option, @PathVariable String user) {
        Account self = accountService.getAuthenticatedAccount();
        List<String> errors = optionValidator.validateOption(option);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("question", questionDatabase.findOne(id));
            model.addAttribute("user", self);
            model.addAttribute("options", optionDatabase.findByQuestion(id));
            return "question";
        }

        option.setQuestion_id(questionDatabase.findOne(id).getId());
        optionDatabase.create(option.getValue(), option.isCorrect(), option.getQuestion_id());
        return "redirect:/" + user + "/questions/" + id;
    }

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.DELETE)
    public String deleteOption(Model model, @PathVariable int id, @RequestParam int option_id, @PathVariable String user) {
        optionDatabase.delete(option_id);
        Question q = questionDatabase.findOne(id);
        q.setPublished(false);
        questionDatabase.save(q);
        return "redirect:/" + user + "/questions/" + id;
    }

}
