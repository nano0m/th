package es.nitaur;

import com.google.common.collect.*;
import es.nitaur.model.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllQuestionsValidTest {

    public static final String UPDATE_QUESTION_API = "/api/quiz/updateQuestions";
    public static final String GET_ALL_QUESTIONS_API = "/api/quiz/allQuestions";

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void questionsAreNotSavedWithEmptyQuestionText() throws Exception {
        QuizQuestion quiz_question_1 = new QuizQuestion();
        quiz_question_1.setId(1L);
        quiz_question_1.setQuestion("<<redacted>>");

        QuizQuestion quiz_question_2 = new QuizQuestion();
        quiz_question_2.setId(2L);
        quiz_question_2.setQuestion(null);

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quiz_question_1, quiz_question_2);

        this.restTemplate.postForLocation(UPDATE_QUESTION_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> exchange = this.restTemplate.exchange(GET_ALL_QUESTIONS_API + "?filterSectionId=1", HttpMethod.GET, null, new ParameterizedTypeReference<List<QuizQuestion>>() {
        });
        List<QuizQuestion> body = exchange.getBody();

        for (QuizQuestion question : body) {
            assertThat("Question text should not be <<redacted>>", "<<redacted>>", not(question.getQuestion()));
        }
    }

    @Test
    public void questionsAreSavedWithQuestionText() throws Exception {
        QuizQuestion quiz_question_3 = new QuizQuestion();
        quiz_question_3.setId(3L);
        quiz_question_3.setQuestion("<<redacted>>");

        QuizQuestion quiz_question_4 = new QuizQuestion();
        quiz_question_4.setId(4L);
        quiz_question_4.setQuestion("<<redacted>>");

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quiz_question_3, quiz_question_4);

        this.restTemplate.postForLocation(UPDATE_QUESTION_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> exchange = this.restTemplate.exchange(GET_ALL_QUESTIONS_API + "?filterSectionId=2", HttpMethod.GET, null, new ParameterizedTypeReference<List<QuizQuestion>>() {
        });
        List<QuizQuestion> body = exchange.getBody();

        for (QuizQuestion question : body) {
            assertThat("Question text is only <<redacted>>", "<<redacted>>", is(question.getQuestion()));
        }
    }
}