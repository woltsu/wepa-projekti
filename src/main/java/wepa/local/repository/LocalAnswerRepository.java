package wepa.local.repository;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalAnswer;
import wepa.local.domain.LocalOption;

@Profile("default")
public interface LocalAnswerRepository extends JpaRepository<LocalAnswer, Long> {

    List<LocalAnswer> findByOption(LocalOption option);
    
    LocalAnswer findByAccountAndQuestionId(LocalAccount account, Long id);
}
