package wepa.local.service;

import wepa.local.validator.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalStat;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalStatRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatServiceTest {

    @Autowired
    private LocalStatService statService;
    
    @Autowired
    private LocalAccountRepository accountRepository;
    
    @Autowired
    private LocalStatRepository statRepository;

    @Before
    public void setUp() {
        LocalAccount a = new LocalAccount();
        LocalAccount b = new LocalAccount();
        a.setUsername("a");
        b.setUsername("b");
        accountRepository.save(a);
        accountRepository.save(b);
    }

    @Test
    public void testRankNotSameIfSameScore() {
        LocalAccount a = accountRepository.findByUsername("a");
        LocalAccount b = accountRepository.findByUsername("b");
        LocalStat stat1 = new LocalStat();
        LocalStat stat2 = new LocalStat();
        stat1.setCorrectAnswers(26);
        stat2.setCorrectAnswers(26);
        stat1.setAccount(a);
        stat2.setAccount(b);
        statRepository.save(stat1);
        statRepository.save(stat2);
        assertNotEquals(statService.getRank(a), statService.getRank(b));
        statRepository.delete(statRepository.findByAccount(a));
        statRepository.delete(statRepository.findByAccount(b));
    }
    
    @Test
    public void testRankingWorks() {
        LocalAccount a = accountRepository.findByUsername("a");
        LocalAccount b = accountRepository.findByUsername("b");
        LocalStat stat1 = new LocalStat();
        LocalStat stat2 = new LocalStat();
        stat1.setCorrectAnswers(26);
        stat2.setCorrectAnswers(28);
        stat1.setAccount(a);
        stat2.setAccount(b);
        statRepository.save(stat1);
        statRepository.save(stat2);
        assertEquals(1, statService.getRank(b));
        assertEquals(2, statService.getRank(a));
        stat1 = statRepository.findByAccount(a);
        stat2 = statRepository.findByAccount(b);
        stat1.setCorrectAnswers(100);
        stat2.setCorrectAnswers(99);
        statRepository.save(stat1);
        statRepository.save(stat2);
        assertEquals(1, statService.getRank(a));
        assertEquals(2, statService.getRank(b));
        statRepository.delete(statRepository.findByAccount(a));
        statRepository.delete(statRepository.findByAccount(b));
    }
    
    @Test
    public void testWrongAnswersDontAffectScore() {
        LocalAccount a = accountRepository.findByUsername("a");
        LocalAccount b = accountRepository.findByUsername("b");
        LocalStat stat1 = new LocalStat();
        LocalStat stat2 = new LocalStat();
        stat1.setCorrectAnswers(100);
        stat2.setCorrectAnswers(50);
        stat1.setAccount(a);
        stat2.setAccount(b);
        statRepository.save(stat1);
        statRepository.save(stat2);
        assertEquals(1, statService.getRank(a));
        assertEquals(2, statService.getRank(b));
        stat1 = statRepository.findByAccount(a);
        stat2 = statRepository.findByAccount(b);
        stat1.setWrongAnswers(1000);
        stat2.setWrongAnswers(2000);
        statRepository.save(stat1);
        statRepository.save(stat2);
        assertEquals(1, statService.getRank(a));
        assertEquals(2, statService.getRank(b));
        statRepository.delete(statRepository.findByAccount(a));
        statRepository.delete(statRepository.findByAccount(b));
    }
    

}
