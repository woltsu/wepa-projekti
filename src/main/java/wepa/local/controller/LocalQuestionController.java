package wepa.local.controller;

import java.sql.Timestamp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalAnswerRepository;
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
    
    @Autowired
    private LocalAnswerRepository answerRepository;
    
    private boolean isCorrectUser(String username) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!username.equals(self.getUsername())) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/{user}/questions", method = RequestMethod.GET)
    public String getQuestions(Model model, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        model.addAttribute("user", self);
        model.addAttribute("questions", questionRepository.findByLocalAccount(accountService.getAuthenticatedAccount()));
        return "questions";
    }
    
    @RequestMapping(value = "/{user}/questions", method = RequestMethod.DELETE)
    @Transactional
    public String deleteQuestion(@RequestParam Long question_id, @PathVariable String user) {
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        LocalQuestion q = questionRepository.findOne(question_id);
        LocalAccount a = accountRepository.findByUsername(user);
        a.getQuestions().remove(q);
        List<LocalOption> options = optionRepository.findByLocalQuestion(q);
        for (LocalOption option : options) {
            q.getOptions().remove(option);
            optionRepository.delete(option);
        }
        questionRepository.delete(q);
        return "redirect:/" + user + "/questions";
    }

    @RequestMapping(value = "/{user}/questions", method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        LocalQuestion q = new LocalQuestion();
        q.setName(name);
        q.setLocalAccount(accountService.getAuthenticatedAccount());
        q.setDate(new Timestamp(System.currentTimeMillis()));
        q = questionRepository.save(q);
        self.addQuestion(q);
        q.setPublished(false);
        accountRepository.save(self);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

    @RequestMapping(value = "/{user}/questions/{id}", method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable Long id, @PathVariable String user) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        model.addAttribute("question", questionRepository.findOne(id));
        model.addAttribute("options", optionRepository.findByLocalQuestion(questionRepository.findOne(id)));
        model.addAttribute("user", self);
        return "question";
    }

    @RequestMapping(value = "/{user}/questions/{id}/toggle", method = RequestMethod.POST)
    public String toggleQuestion(@RequestParam Long question_id, @PathVariable String user) {
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        LocalQuestion q = questionRepository.findOne(question_id);
        q.setPublished(!q.isPublished());
        questionRepository.save(q);
        return "redirect:/" + user + "/questions/" + q.getId();
    }
    
    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public String getAnswer(Model model, @PathVariable Long id) {
        if (answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id) != null) {
            model.addAttribute("account_answer", answerRepository.findByAccountAndQuestionId(accountService.getAuthenticatedAccount(), id));
        }
        
        LocalQuestion q = questionRepository.findOne(id);
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("question", q);
        model.addAttribute("options", optionRepository.findByLocalQuestion(q));
        return "answer";
    }
    
    @RequestMapping(value = "/question/{id}", method = RequestMethod.POST)
    public String postAnswer(Model model, @PathVariable Long id, @RequestParam Long option_id) {
        LocalAnswer a = new LocalAnswer();
        if (optionRepository.findOne(option_id).isCorrect()) {
            a.setCorrect(true);
        } else {
            a.setCorrect(false);
        }
        a.setAccount(accountService.getAuthenticatedAccount());
        a.setOption(optionRepository.findOne(option_id));
        a.setQuestionId(id);
        answerRepository.save(a);
        return "redirect:/question/" + id;
    }
    

}
