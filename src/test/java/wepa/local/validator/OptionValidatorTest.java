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
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionValidatorTest {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalOptionValidator optionValidator;

    @Autowired
    private LocalOptionRepository optionRepository;

    @Before
    public void init() {
        LocalAccount a = new LocalAccount();
        a.setUsername("optionValidatorAccount");
        accountRepository.save(a);
        LocalQuestion q = new LocalQuestion();
        q.setName("aaa");
        q.setLocalAccount(a);
        questionRepository.save(q);
    }

    @Test
    public void testValidateValue() {
        LocalOption o = new LocalOption();
        o.setValue("");
        o.setCorrect(false);
        List<String> errors = new ArrayList();
        errors = optionValidator.validateOption(o);
        assertEquals("Option value must not be empty!", errors.get(0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("a");
        }
        o.setValue(sb.toString());
        errors = optionValidator.validateOption(o);
        assertEquals("Option value too long!", errors.get(0));
        o.setValue("ok");
        errors = optionValidator.validateOption(o);
        assertTrue(errors.isEmpty());
    }

//    @Test
//    public void testAlreadyCorrectOption() {
//        LocalAccount a = accountRepository.findByUsername("optionValidatorAccount");
//        LocalQuestion q = questionRepository.findByLocalAccount(a).get(0);
//        LocalOption o = new LocalOption();
//        o.setCorrect(true);
//        q.getOptions().add(o);
//        o.setLocalQuestion(q);
//        optionRepository.save(o);
//        o = new LocalOption();
//        o.setCorrect(true);
//        List<String> errors = optionValidator.validateOption(o);
//        assertEquals("Question already has a correct option!", errors.get(0));
//    }

}
