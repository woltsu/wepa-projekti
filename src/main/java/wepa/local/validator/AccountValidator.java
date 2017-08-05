package wepa.local.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wepa.local.repository.AccountRepository;

@Component
public class AccountValidator {

    @Autowired
    private AccountRepository accountRepository;

    public List<String> validateAccount(String username, String password, String passwordAgain) {
        List<String> errors = new ArrayList();
        errors.addAll(validateUsername(username));
        errors.addAll(validatePassword(password, passwordAgain));
        return errors;
    }

    private List<String> validateUsername(String username) {
        List<String> errors = new ArrayList();
        if (username == null || username.length() < 3) {
            errors.add("Username must be at least 3 characters long!");
        }
        if (accountRepository.findByUsername(username) != null) {
            errors.add("Username taken!");
        }
        return errors;
    }

    private List<String> validatePassword(String password, String passwordAgain) {
        List<String> errors = new ArrayList();
        if (password == null || password.length() < 3) {
            errors.add("Password must be at least 3 characters long!");
        }
        if (!password.equals(passwordAgain)) {
            errors.add("Passwords didn't match!");
        }
        return errors;
    }

}