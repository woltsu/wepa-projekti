package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.service.LocalAccountService;
import wepa.local.repository.LocalQuestionRepository;

@Profile("default")
@Controller
public class LocalQuestionController {

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalAccountService accountService;

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalOptionRepository optionRepository;

    @RequestMapping(value = "/{user}/questions", method = RequestMethod.GET)
    public String getQuestions(Model model, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        model.addAttribute("user", self);
        model.addAttribute("questions", questionRepository.findByLocalAccount(accountService.getAuthenticatedAccount()));
        return "questions";
    }

    @RequestMapping(value = "/{user}/questions", method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        LocalQuestion q = new LocalQuestion();
        q.setName(name);
        q.setLocalAccount(accountService.getAuthenticatedAccount());
        q = questionRepository.save(q);
        self.addQuestion(q);
        q.setPublished(false);
        accountRepository.save(self);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable Long id, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        model.addAttribute("question", questionRepository.findOne(id));
        model.addAttribute("options", optionRepository.findByLocalQuestion(questionRepository.findOne(id)));
        model.addAttribute("user", self);
        return "question";
    }

    @RequestMapping(value = "/{user}/questions/{id}/toggle", method = RequestMethod.POST)
    public String toggleQuestion(@RequestParam Long question_id, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!user.equals(self.getUsername())) {
            return "redirect:/";
        }
        LocalQuestion q = questionRepository.findOne(question_id);
        q.setPublished(!q.isPublished());
        questionRepository.save(q);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

}
