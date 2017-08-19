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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalAnswerRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.service.LocalAccountService;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.validator.LocalQuestionValidator;

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

    @Autowired
    private LocalQuestionValidator questionValidator;

    private boolean isCorrectUser(String username) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (self.isAdmin()) {
            return true;
        }
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
            List<LocalAnswer> answers = answerRepository.findByOption(option);
            if (answers != null) {
                for (LocalAnswer answer : answers) {
                    option.getAnswers().remove(answer);
                    answerRepository.delete(answer);
                }
            }
            optionRepository.delete(option);
        }
        questionRepository.delete(q);
        if (accountService.getAuthenticatedAccount().isAdmin()) {
            return "redirect:/";
        }
        return "redirect:/" + user + "/questions";
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

    @RequestMapping(value = "/{user}/questions", method = RequestMethod.POST)
    public String postQuestions(@RequestParam String name, @PathVariable String user, Model model) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        LocalQuestion q = new LocalQuestion();
        q.setName(name);
        q.setLocalAccount(accountService.getAuthenticatedAccount());
        q.setDate(new Timestamp(System.currentTimeMillis()));

        List<String> errors = questionValidator.validateQuestion(q);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", self);
            model.addAttribute("questions", questionRepository.findByLocalAccount(self));
            return "questions";
        }

        q = questionRepository.save(q);
        self.addQuestion(q);
        q.setPublished(false);
        accountRepository.save(self);
        return "redirect:/" + self.getUsername() + "/questions/" + q.getId();
    }

    @RequestMapping(value = "/{user}/questions/{id}/toggle", method = RequestMethod.POST)
    public String toggleQuestion(@RequestParam Long question_id, @PathVariable String user, Model model) {
        LocalAccount self = accountService.getAuthenticatedAccount();
        if (!isCorrectUser(user)) {
            return "redirect:/";
        }
        LocalQuestion q = questionRepository.findOne(question_id);
        q.setPublished(!q.isPublished());

        List<String> errors = questionValidator.validateQuestion(q);
        if (!errors.isEmpty()) {
            q.setPublished(!q.isPublished());
            model.addAttribute("errors", errors);
            model.addAttribute("options", optionRepository.findByLocalQuestion(questionRepository.findOne(question_id)));
            model.addAttribute("user", self);
            model.addAttribute("question", questionRepository.findOne(question_id));
            return "question";
        }

        questionRepository.save(q);
        return "redirect:/" + user + "/questions/" + q.getId();
    }

}
