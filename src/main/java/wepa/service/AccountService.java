package wepa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wepa.database.AccountDatabase;
import wepa.domain.Account;
import wepa.domain.TestAccount;

@Service
@Profile("production")
public class AccountService {

    @Autowired
    private AccountDatabase accountDatabase;
    
    public Account getAuthenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountDatabase.findByUsername(authentication.getName());
    }
    
}
