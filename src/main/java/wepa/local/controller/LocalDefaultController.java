package wepa.local.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.service.LocalAccountService;
import wepa.TimeCount;

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
    public String redirectHome() {
        return "redirect:/?page=1";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
        List<LocalQuestion> questions = questionRepository.getLatestQuestions();
        Collections.reverse(questions);
        model.addAttribute("page", page);

        if (page <= 0) {
            page = 0;
        } else {
            page = page - 1;
        }
        List<LocalQuestion> tenLatestQuestionsByPage = new ArrayList();
        if (!(questions.size() < (page * 10))) {
            int start = page * 10;
            int end = start + 10;
            for (int i = start; (i < end && i < questions.size()); i++) {
                tenLatestQuestionsByPage.add(questions.get(i));
            }
        } else {
            return "redirect:/?page=1";
        }

        model.addAttribute("questions", tenLatestQuestionsByPage);
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/nextPage")
    public String nextPage(@RequestParam int page) {
        return "redirect:/?page=" + (page + 1);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prevPage")
    public String prevPage(@RequestParam int page) {
        if (page <= 1) {
            page = 2;
        }
        return "redirect:/?page=" + (page - 1);
    }

}
