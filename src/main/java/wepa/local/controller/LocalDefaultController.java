package wepa.local.controller;

import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.service.LocalAccountService;

@Profile("default")
@Controller
@RequestMapping("*")
public class LocalDefaultController {

    @Autowired
    private LocalAccountService accountService;
    
    @Autowired
    private LocalAccountRepository accountRepository;
    
    @Autowired
    private LocalQuestionRepository questionRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        List<LocalQuestion> questions = questionRepository.getLatestQuestions();
        Collections.reverse(questions);
        model.addAttribute("questions", questions);
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "index";
    }

}
