package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.TestAccount;

public interface AccountRepository extends JpaRepository<TestAccount, Long> {

    TestAccount findByUsername(String username);
}
