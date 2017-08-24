package wepa.local.repository;

import java.util.Calendar;
import java.util.List;
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
import wepa.local.domain.LocalQuestion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionRepositoryTest {

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalAccountRepository accountRepository;

    @Before
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("username");
        accountRepository.save(a);
    }

    @Test
    public void testSaveQuestion() {
        LocalQuestion q = new LocalQuestion();
        LocalAccount b = new LocalAccount();
        b.setUsername("name");
        accountRepository.save(b);
        assertTrue(questionRepository.findByLocalAccount(b).isEmpty());
        q.setName("what");
        q.setLocalAccount(b);
        questionRepository.save(q);
        assertFalse(questionRepository.findByLocalAccount(b).isEmpty());
        q = questionRepository.findByLocalAccount(b).get(0);
        assertEquals("name", q.getLocalAccount().getUsername());
        assertEquals("what", q.getName());
        assertNotNull(q.getId());
    }

    @Test
    public void testGetLatestQuestions() {
        LocalQuestion firstQuestion = new LocalQuestion();
        firstQuestion.setName("first");
        firstQuestion.setPublished(true);
        LocalQuestion secondQuestion = new LocalQuestion();
        secondQuestion.setName("second");
        secondQuestion.setPublished(true);
        firstQuestion.setDate(Calendar.getInstance().getTime());
        questionRepository.save(firstQuestion);
        secondQuestion.setDate(Calendar.getInstance().getTime());
        questionRepository.save(secondQuestion);
        List<LocalQuestion> questions = questionRepository.getLatestQuestions();
        boolean isFirst = false;
        for (LocalQuestion question : questions) {
            if (question.getName().equals("first")) {
                isFirst = true;
                break;
            } else if (question.getName().equals("second")) {
                break;
            }
        }
        assertTrue(isFirst);
    }

}
