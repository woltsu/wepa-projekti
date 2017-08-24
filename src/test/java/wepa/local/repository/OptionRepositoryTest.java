package wepa.local.repository;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionRepositoryTest {

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalAccountRepository accountRepository;
    
    @Autowired
    private LocalOptionRepository optionRepository;

    @Before
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("username");
        accountRepository.save(a);
        LocalQuestion q = new LocalQuestion();
        q.setLocalAccount(a);
        q.setName("question");
        q.setPublished(true);
        questionRepository.save(q);
    }

    @Test
    public void testSaveOption() {
        LocalOption o = new LocalOption();
        LocalQuestion q = questionRepository.findByLocalAccount(accountRepository.findByUsername("username")).get(0);
        o.setValue("aaa");
        o.setLocalQuestion(q);
        assertEquals(0, optionRepository.findByLocalQuestion(q).size());
        optionRepository.save(o);
        List<LocalOption> options = optionRepository.findByLocalQuestion(q);
        assertEquals(1, options.size());
        assertEquals("aaa", options.get(0).getValue());
        assertEquals("question", options.get(0).getLocalQuestion().getName());
        assertNotNull(options.get(0).getId());
    }

}
