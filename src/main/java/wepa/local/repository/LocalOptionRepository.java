package wepa.local.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;

@Profile("default")
public interface LocalOptionRepository extends JpaRepository<LocalOption, Long> {

    List<LocalOption> findByLocalQuestion(LocalQuestion q);
}
