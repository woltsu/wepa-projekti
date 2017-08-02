package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}
