package wepa.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wepa.database.AccountDatabase;
import wepa.domain.Account;

//Validates an account. Returns errors if there are any.
@Profile("production")
@Component
public class AccountValidator {

    @Autowired
    private AccountDatabase accountDatabase;

    public List<String> validateAccount(String username, String password, String passwordAgain) {
        List<String> errors = new ArrayList();
        errors.addAll(validateUsername(username));
        errors.addAll(validatePassword(password, passwordAgain));
        return errors;
    }

    //Checks that account's username fills the requirements.
    private List<String> validateUsername(String username) {
        List<String> errors = new ArrayList();
        if (username == null || username.length() < 3) {
            errors.add("Username must be at least 3 characters long!");
        }
        if (username.length() > 200) {
            errors.add("Username too long!");
        }
        if (accountDatabase.findByUsername(username) != null) {
            errors.add("Username taken!");
        }
        return errors;
    }

    //Checks that account's password fills the requirements.
    private List<String> validatePassword(String password, String passwordAgain) {
        List<String> errors = new ArrayList();
        if (password == null || password.length() < 3) {
            errors.add("Password must be at least 3 characters long!");
        }
        if (password.length() > 200) {
            errors.add("Password too long!");
        }
        if (!password.equals(passwordAgain)) {
            errors.add("Passwords didn't match!");
        }
        return errors;
    }

}
