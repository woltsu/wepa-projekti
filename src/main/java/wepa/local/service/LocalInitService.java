package wepa.local.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.domain.LocalStat;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;
import wepa.local.repository.LocalStatRepository;

@Profile("default")
@Service
public class LocalInitService {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalOptionRepository optionRepository;

    @Autowired
    private LocalStatRepository statRepository;

    @PostConstruct
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("user");
        a.setPassword("user");
        a.setAdmin(false);
        accountRepository.save(a);

        LocalStat stat1 = new LocalStat();
        stat1.setAccount(a);
        stat1.setCorrectAnswers(0);
        stat1.setWrongAnswers(0);
        statRepository.save(stat1);

        LocalAccount admin = new LocalAccount();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setAdmin(true);
        accountRepository.save(admin);

        LocalStat stat2 = new LocalStat();
        stat2.setAccount(admin);
        stat2.setCorrectAnswers(0);
        stat2.setWrongAnswers(0);
        statRepository.save(stat2);

        for (int i = 0; i < 25; i++) {
            LocalQuestion q = new LocalQuestion();
            q.setDate(Calendar.getInstance().getTime());
            q.setLocalAccount(a);
            q.setName("question" + i);
            q.setPublished(true);
            a.addQuestion(q);

            questionRepository.save(q);
            accountRepository.save(a);
        }

        LocalQuestion q = new LocalQuestion();
        q.setDate(Calendar.getInstance().getTime());
        q.setLocalAccount(a);
        q.setName("emptyQuestion");
        q.setPublished(false);
        a.addQuestion(q);
        questionRepository.save(q);
        accountRepository.save(a);
        
        for (LocalQuestion localQuestion : questionRepository.findAll()) {
            if (localQuestion.getName().equals("emptyQuestion")) {
                continue;
            }
            createOptions(localQuestion);
        }
    }

    private void createOptions(LocalQuestion q) {
        LocalOption first = new LocalOption();
        first.setValue("1");
        first.setCorrect(false);
        first.setLocalQuestion(q);
//        q.addOption(first);

        LocalOption second = new LocalOption();
        second.setValue("2");
        second.setCorrect(false);
        second.setLocalQuestion(q);
//        q.addOption(second);

        LocalOption third = new LocalOption();
        third.setValue("3");
        third.setCorrect(false);
        third.setLocalQuestion(q);
//        q.addOption(third);

        LocalOption fourth = new LocalOption();
        fourth.setValue("10");
        fourth.setCorrect(true);
        fourth.setLocalQuestion(q);
//        q.addOption(fourth);
        
        optionRepository.save(first);
        optionRepository.save(second);
        optionRepository.save(third);
        optionRepository.save(fourth);
    }

}
