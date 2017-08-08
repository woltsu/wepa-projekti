package wepa.local.repository;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalTest;

@Profile("default")
public interface LocalTestRepository extends JpaRepository<LocalTest, Long> {

    List<LocalTest> findByLocalAccount(LocalAccount a);
}
