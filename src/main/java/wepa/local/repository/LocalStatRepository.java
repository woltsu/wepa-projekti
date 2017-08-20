package wepa.local.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalStat;

@Profile("default")
public interface LocalStatRepository extends JpaRepository<LocalStat, Long> {

    LocalStat findByAccount(LocalAccount a);
}
