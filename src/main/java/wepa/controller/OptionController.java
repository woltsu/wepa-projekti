package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Option;
import wepa.service.OptionService;

@Profile("production")
@Controller
public class OptionController {

    @Autowired
    private OptionService optionService;
    
    @Autowired
    private QuestionDatabase questionDatabase;
    
    @Autowired
    private OptionDatabase optionDatabase;
    
    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.POST)
    public String postOption(Model model, @PathVariable int id, Option option, @PathVariable String user) {
        if ((optionService.hasAlreadyACorrectOption(id) && option.isCorrect()) || (optionService.hasMaxFalseOptions(id) && !option.isCorrect())) {
            return "redirect:/" + user + "/questions/" + id;
        }
        option.setQuestion_id(questionDatabase.findOne(id).getId());
        optionDatabase.create(option.getValue(), option.isCorrect(), option.getQuestion_id());
        return "redirect:/" + user + "/questions/" + id;
    }

}
