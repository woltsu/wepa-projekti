package wepa.local.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalTest;

public interface LocalTestRepository extends JpaRepository<LocalTest, Long> {

    List<LocalTest> findByLocalAccount(LocalAccount a);
}
