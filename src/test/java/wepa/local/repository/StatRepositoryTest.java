package wepa.local.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import wepa.local.domain.LocalStat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatRepositoryTest {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalStatRepository statRepository;
    
    @Before
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("statAccount");
        accountRepository.save(a);
    }

    @Test
    public void testSaveAnswer() {
        LocalAccount a = accountRepository.findByUsername("statAccount");
        LocalStat s = new LocalStat();
        s.setAccount(a);
        s.setCorrectAnswers(10);
        s.setWrongAnswers(27);
        assertNull(statRepository.findByAccount(a));
        statRepository.save(s);
        LocalStat accountsStat = statRepository.findByAccount(a);
        assertNotNull(accountsStat);
        assertEquals(10, accountsStat.getCorrectAnswers());
        assertEquals(27, accountsStat.getWrongAnswers());
        assertEquals("statAccount", accountsStat.getAccount().getUsername());
    }

}
