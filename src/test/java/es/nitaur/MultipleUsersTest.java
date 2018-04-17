package es.nitaur;

import es.nitaur.model.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.*;
import java.util.concurrent.*;
import org.springframework.test.context.junit4.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultipleUsersTest {

    public static final String GET_QUESTION_API = "/api/quiz/getQuestion/1";

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void answerQuestions() throws Exception {
        ExecutorService executor_service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            Runnable runnable = new HttpPostRunnable(this.port, i);
            executor_service.submit(runnable);
        }

        executor_service.shutdown();
        executor_service.awaitTermination(60, TimeUnit.SECONDS);

        QuizQuestion question = this.restTemplate.getForObject(GET_QUESTION_API, QuizQuestion.class);
        assertThat("There were 10 updates to the question", 10L, is(question.getUpdateCount()));
    }
}