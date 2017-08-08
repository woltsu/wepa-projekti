package wepa.local.repository;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalQuestion;

@Profile("default")
public interface LocalQuestionRepository extends JpaRepository<LocalQuestion, Long> {

    List<LocalQuestion> findByLocalAccount(LocalAccount a);
}
