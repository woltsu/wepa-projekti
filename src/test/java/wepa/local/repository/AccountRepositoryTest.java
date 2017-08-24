package wepa.local.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wepa.local.domain.LocalAccount;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {
    
    @Autowired
    private LocalAccountRepository localAccountRepository;
    
    @Test
    public void testSavePerson() {
        LocalAccount a = new LocalAccount();
        a.setUsername("username");
        a.setPassword("password");
        a.setAdmin(false);
        localAccountRepository.save(a);
        LocalAccount retrieved = localAccountRepository.findByUsername("username");
        assertNotNull(retrieved);
        assertEquals("username", retrieved.getUsername());
        assertFalse(retrieved.isAdmin());
        assertNotNull(retrieved.getId());
    }
    
}
