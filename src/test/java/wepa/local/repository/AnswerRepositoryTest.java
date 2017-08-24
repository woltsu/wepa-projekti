package wepa.local.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnswerRepositoryTest {

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalOptionRepository optionRepository;

    @Autowired
    private LocalAnswerRepository answerRepository;

    @Before
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("answerAccount");
        accountRepository.save(a);
        LocalQuestion q = new LocalQuestion();
        q.setLocalAccount(a);
        q.setName("answerQuestion");
        questionRepository.save(q);
        LocalOption o = new LocalOption();
        o.setLocalQuestion(q);
        optionRepository.save(o);
    }

    @Test
    public void testSaveAnswer() {
        LocalAccount account = accountRepository.findByUsername("answerAccount");
        LocalQuestion question = questionRepository.findByLocalAccount(account).get(0);
        LocalOption option = optionRepository.findByLocalQuestion(question).get(0);
        LocalAnswer answer = new LocalAnswer();
        answer.setAccount(account);
        answer.setOption(option);
        answer.setQuestionId(question.getId());
        assertTrue(answerRepository.findByOption(option).isEmpty());
        assertTrue(answerRepository.findByAccountAndQuestionId(account, question.getId()) == null);
        answerRepository.save(answer);
        assertFalse(answerRepository.findByOption(option).isEmpty());
        assertFalse(answerRepository.findByAccountAndQuestionId(account, question.getId()) == null);
        assertEquals("answerAccount", answerRepository.findByOption(option).get(0).getAccount().getUsername());
        assertEquals("answerQuestion", answerRepository.findByOption(option).get(0).getOption().getLocalQuestion().getName());
        assertNotNull(answerRepository.findByOption(option).get(0).getId());
    }

}
