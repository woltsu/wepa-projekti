package wepa.local.repository;

import wepa.local.domain.LocalAccount;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

//@Profile("default")
public interface LocalAccountRepository extends JpaRepository<LocalAccount, Long> {

    LocalAccount findByUsername(String username);
}
