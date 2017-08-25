package wepa.auth;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import wepa.database.AccountDatabase;
import wepa.domain.Account;

//This class is used to authenticate an user
@Profile("production")
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountDatabase accountDatabase;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        String username = a.getPrincipal().toString();
        String password = a.getCredentials().toString();

        Account account = accountDatabase.findByUsername(username);

        if (account == null) {
            throw new AuthenticationException("Unable to authenticate user " + username) {
            };
        }

        if (!BCrypt.hashpw(password, account.getSalt()).equals(account.getPassword())) {
            throw new AuthenticationException("Unable to authenticate user " + username) {
            };
        }

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        
        grantedAuths.add(new SimpleGrantedAuthority("USER"));
        //If the account is an admin's account, it will be granted admin 'rights'
        if (account.isAdmin()) {
            grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
        }

        return new UsernamePasswordAuthenticationToken(account.getUsername(), password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }
}
