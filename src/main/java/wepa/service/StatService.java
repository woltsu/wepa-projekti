package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import wepa.database.StatDatabase;
import wepa.domain.Account;
import wepa.domain.Stat;

@Profile("production")
@Service
public class StatService {

    @Autowired
    private StatDatabase statDatabase;
    
    //Returns the rank of an account
    public int getRank(Account account) {
        List<Stat> orderedByStats = statDatabase.getRanks();
        int i = 1;
        for (Stat orderedByStat : orderedByStats) {
            if (orderedByStat.getAccount_id() == account.getId()) {
                return i;
            }
            i++;
        }
        return i;
    }
    
}
