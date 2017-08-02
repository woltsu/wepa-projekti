package wepa.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.TestAccount;

@Profile("default")
public interface AccountRepository extends JpaRepository<TestAccount, Long> {

    TestAccount findByUsername(String username);
}
