package wepa.local.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalStat;
import wepa.local.repository.LocalStatRepository;

@Profile("default")
@Service
public class LocalStatService {
    
    @Autowired
    private LocalStatRepository statRepository;
    
    public int getRank(LocalAccount account) {
        List<LocalStat> rankList = statRepository.getRankList();
        int i = 1;
        for (LocalStat localStat : rankList) {
            if (localStat.getAccount().getId() == account.getId()) {
                return i;
            }
            i++;
        }
        return i;
    }
    
}
