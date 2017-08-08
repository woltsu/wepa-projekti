package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalQuestion;
import wepa.local.service.LocalAccountService;
import wepa.local.repository.LocalQuestionRepository;

@Profile("default")
@Controller
public class LocalQuestionController {
    
    @Autowired
    private LocalQuestionRepository questionRepository;
    
    @Autowired
    private LocalAccountService accountService;
    
    @RequestMapping(value = "/questions",method = RequestMethod.GET)
    public String getQuestions(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("questions", questionRepository.findByLocalAccount(accountService.getAuthenticatedAccount()));
        return "questions";
    }
    
    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name) {
        LocalQuestion q = new LocalQuestion();
        q.setName(name);
        q.setLocalAccount(accountService.getAuthenticatedAccount());
        q = questionRepository.save(q);
        return "redirect:/questions/" + q.getId();
    }
    
    @RequestMapping(value = "/questions/{id}", method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable Long id) {
        model.addAttribute("question", questionRepository.findOne(id));
        return "question";
    }
    
}
