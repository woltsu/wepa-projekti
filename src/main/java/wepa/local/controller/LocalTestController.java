package wepa.local.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wepa.local.domain.LocalTest;
import wepa.local.repository.LocalTestRepository;
import wepa.local.service.LocalAccountService;

@Profile("default")
@Controller
@RequestMapping("/tests")
public class LocalTestController {
    
    @Autowired
    private LocalTestRepository testRepository;
    
    @Autowired
    private LocalAccountService accountService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getTests(Model model) {
        model.addAttribute("user", accountService.getAuthenticatedAccount());
        model.addAttribute("tests", testRepository.findByLocalAccount(accountService.getAuthenticatedAccount()));
        return "tests";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postTest(@RequestParam String name) {
        LocalTest test = new LocalTest();
        test.setName(name);
        test.setLocalAccount(accountService.getAuthenticatedAccount());
        testRepository.save(test);
        return "redirect:/tests";
    }
    
}
