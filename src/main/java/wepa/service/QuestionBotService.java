package wepa.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Timestamp;
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
import wepa.database.AccountDatabase;
import wepa.database.OptionDatabase;
import wepa.database.QuestionDatabase;
import wepa.domain.Account;
import wepa.domain.Option;
import wepa.domain.Question;

@Profile("production")
@Service
public class QuestionBotService {

    @Autowired
    private AccountDatabase accountDatabase;

    @Autowired
    private QuestionDatabase questionDatabase;

    @Autowired
    private OptionDatabase optionDatabase;

    private RestTemplate restTemplate;

    public QuestionBotService() {
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(cron = "0 0/15 * * * ?")
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
        question = question.replaceAll("&#039;", "'");
        firstIncorrectValue = firstIncorrectValue.replaceAll("&#039;", "'");
        secondIncorrectValue = secondIncorrectValue.replaceAll("&#039;", "'");
        thirdIncorrectValue = thirdIncorrectValue.replaceAll("&#039;", "'");

        List<String> values = new ArrayList();
        values.add(correctValue);
        values.add(firstIncorrectValue);
        values.add(secondIncorrectValue);
        values.add(thirdIncorrectValue);
        Collections.shuffle(values);

        Question q = new Question();
        q.setAccount(accountDatabase.findByUsername("Question bot").getId());
        q.setName(question);
        q.setPublished(true);
        q.setPublisher(accountDatabase.findByUsername("Question bot"));
        questionDatabase.create(q.getName(), q.getAccount(), true);

        for (String value : values) {
            Option o = new Option();
            o.setValue(value);
            List<Question> questions = questionDatabase.findByAccount(accountDatabase.findByUsername("Question bot").getId());
            o.setQuestion_id(questions.get(questions.size() - 1).getId());
            if (value.equals(correctValue)) {
                o.setCorrect(true);
            } else {
                o.setCorrect(false);
            }
            optionDatabase.create(o.getValue(), o.isCorrect(), o.getQuestion_id());
        }
    }

}
