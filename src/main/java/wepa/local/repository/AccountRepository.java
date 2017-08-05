package wepa.local.repository;

import wepa.local.domain.Account;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("default")
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}
