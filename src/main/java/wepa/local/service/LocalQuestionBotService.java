package wepa.local.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wepa.local.domain.LocalAccount;
import wepa.local.domain.LocalOption;
import wepa.local.domain.LocalQuestion;
import wepa.local.repository.LocalAccountRepository;
import wepa.local.repository.LocalOptionRepository;
import wepa.local.repository.LocalQuestionRepository;

@Profile("default")
@Service
public class LocalQuestionBotService {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private LocalQuestionRepository questionRepository;

    @Autowired
    private LocalOptionRepository optionRepository;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LocalAccount bot = new LocalAccount();
        bot.setUsername("Question bot");
        bot.setPassword("very secret password");
        accountRepository.save(bot);
    }

    public LocalQuestionBotService() {
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void getQuestion() {
        JsonNode node = restTemplate.getForObject("https://opentdb.com/api.php?amount=1&type=multiple", JsonNode.class);
        String question = node.get("results").get(0).get("question").asText();
        String correctValue = node.get("results").get(0).get("correct_answer").asText();
        String firstIncorrectValue = node.get("results").get(0).get("incorrect_answers").get(0).asText();
        String secondIncorrectValue = node.get("results").get(0).get("incorrect_answers").get(1).asText();
        String thirdIncorrectValue = node.get("results").get(0).get("incorrect_answers").get(2).asText();
        question = question.replaceAll("&quot;", "'");
        firstIncorrectValue = firstIncorrectValue.replaceAll("&quot;", "'");
        secondIncorrectValue = secondIncorrectValue.replaceAll("&quot;", "'");
        thirdIncorrectValue = thirdIncorrectValue.replaceAll("&quot;", "'");
        question = question.replaceAll("&#039;s", "'");
        firstIncorrectValue = firstIncorrectValue.replaceAll("&#039;s", "'");
        secondIncorrectValue = secondIncorrectValue.replaceAll("&#039;s", "'");
        thirdIncorrectValue = thirdIncorrectValue.replaceAll("&#039;s", "'");
        
        List<String> values = new ArrayList();
        values.add(correctValue);
        values.add(firstIncorrectValue);
        values.add(secondIncorrectValue);
        values.add(thirdIncorrectValue);
        Collections.shuffle(values);

        LocalQuestion q = new LocalQuestion();
        q.setDate(Calendar.getInstance().getTime());
        q.setLocalAccount(accountRepository.findByUsername("bot"));
        q.setName(question);
        q.setPublished(true);

        List<LocalOption> options = new ArrayList();
        for (String value : values) {
            LocalOption o = new LocalOption();
            o.setValue(value);
            o.setLocalQuestion(q);
            if (value.equals(correctValue)) {
                o.setCorrect(true);
            } else {
                o.setCorrect(false);
            }
            options.add(o);
        }
        q.setOptions(options);
        questionRepository.save(q);
        optionRepository.save(q.getOptions());
    }

}
