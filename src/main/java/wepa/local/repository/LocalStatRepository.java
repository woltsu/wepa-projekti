package wepa.local.repository;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalStat;

@Profile("default")
public interface LocalStatRepository extends JpaRepository<LocalStat, Long> {

    LocalStat findByAccount(LocalAccount a);
    
    @Query("SELECT s FROM LocalStat s ORDER BY s.correctAnswers DESC")
    public List<LocalStat> getRankList();
}
