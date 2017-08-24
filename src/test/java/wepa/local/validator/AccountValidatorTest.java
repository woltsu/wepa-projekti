package wepa.local.validator;

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
import wepa.local.repository.LocalAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountValidatorTest {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalAccountValidator accountValidator;

    @Before
    public void setUp() {
        LocalAccount a = new LocalAccount();
        a.setUsername("john");
        accountRepository.save(a);
    }

    @Test
    public void testValidateUsername() {
        List<String> errors = new ArrayList();
        errors = accountValidator.validateAccount("", "password", "password");
        assertEquals("Username must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("a", "password", "password");
        assertEquals("Username must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("aa", "password", "password");
        assertEquals("Username must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("john", "pass", "pass");
        assertEquals("Username taken!", errors.get(0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("a");
        }
        errors = accountValidator.validateAccount(sb.toString(), "pass", "pass");
        assertEquals("Username too long!", errors.get(0));
        errors = accountValidator.validateAccount("jack", "pass", "pass");
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidatePassword() {
        List<String> errors = new ArrayList();
        errors = accountValidator.validateAccount("jonas", "", "");
        assertEquals("Password must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("jonas", "a", "a");
        assertEquals("Password must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("jonas", "aa", "aa");
        assertEquals("Password must be at least 3 characters long!", errors.get(0));
        errors = accountValidator.validateAccount("jonas", "pass", "pas");
        assertEquals("Passwords didn't match!", errors.get(0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("a");
        }
        errors = accountValidator.validateAccount("jonas", sb.toString(), sb.toString());
        assertEquals("Password too long!", errors.get(0));
        errors = accountValidator.validateAccount("jonas", "pass", "pass");
        assertTrue(errors.isEmpty());
    }

}
