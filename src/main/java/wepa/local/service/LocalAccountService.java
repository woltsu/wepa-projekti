package wepa.local.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wepa.local.domain.LocalAccount;
import wepa.local.repository.LocalAccountRepository;

@Service
@Profile("local")
public class LocalAccountService {

    @Autowired
    private LocalAccountRepository accountRepository;

    public LocalAccount getAuthenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountRepository.findByUsername(authentication.getName());
    }
}
