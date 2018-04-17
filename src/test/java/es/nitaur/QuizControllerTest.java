package es.nitaur;

import es.nitaur.model.*;
import org.junit.Test;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuizControllerTest {
    /*
    Endpoints :
        /api/quiz/updateQuestions
        /api/quiz/allQuestions
        /api/quiz/answerQuestion/{id}
        already covered by MultipleUsersTest and AllQuestionsValidTest
     */
    public static final String GET_QUIZZ_API = "/api/quiz";
    public static final String DELETE_QUIZZ_API = "/api/quiz/delete";
    public static final String GET_QUESTION_API = "/api/quiz/getQuestion";
    public static final String UPDATER_QUESTION_API = "/api/quiz/updateQuestion";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllQuizzes() throws Exception {
        ResponseEntity<List<Quiz>> response = this.restTemplate.exchange(GET_QUIZZ_API, HttpMethod.GET, null, new ParameterizedTypeReference<List<Quiz>>() {
        });
        assertTrue(null != response && HttpStatus.OK.equals(response.getStatusCode()) && response.hasBody());
    }

    @Test
    public void testGetQuiz() throws Exception {
        long quiz_id = 1L;
        ResponseEntity<Quiz> quiz_response = this.restTemplate.exchange(GET_QUIZZ_API + "/" + quiz_id, HttpMethod.GET, null, new ParameterizedTypeReference<Quiz>() {
        });
        assertTrue(null != quiz_response && HttpStatus.OK.equals(quiz_response.getStatusCode()) && quiz_response.hasBody());

        Quiz quiz_body = quiz_response.getBody();


        assertThat("Quiz ID", quiz_body.getId(), is(quiz_id));
    }

    @Test
    public void testCreateQuiz() throws Exception {
        Quiz new_quiz = newDummeQuiz();

        ResponseEntity<Quiz> quiz_response = this.restTemplate.exchange(GET_QUIZZ_API, HttpMethod.POST, new HttpEntity<>(new_quiz), new ParameterizedTypeReference<Quiz>() {
        });

        assertTrue(null != quiz_response && HttpStatus.CREATED.equals(quiz_response.getStatusCode()) && quiz_response.hasBody());

        Quiz quiz_body = quiz_response.getBody();


        assertThat("Quiz name", quiz_body.getName(), is(new_quiz.getName()));
    }


    @Test
    public void testDeleteQuiz() throws Exception {
        Quiz new_quiz = newDummeQuiz();

        ResponseEntity<Quiz> response = this.restTemplate.exchange(GET_QUIZZ_API, HttpMethod.POST, new HttpEntity<>(new_quiz), new ParameterizedTypeReference<Quiz>() {
        });

        assertTrue(null != response && HttpStatus.CREATED.equals(response.getStatusCode()) && response.hasBody());

        Quiz quiz_body = response.getBody();

        response = this.restTemplate.exchange(DELETE_QUIZZ_API + "/" + quiz_body.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<Quiz>() {
        });

        assertTrue(null != response && HttpStatus.NO_CONTENT.equals(response.getStatusCode()) && !response.hasBody());

        response = this.restTemplate.exchange(GET_QUIZZ_API + "/" + quiz_body.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<Quiz>() {
        });

        assertTrue(null != response && HttpStatus.NOT_FOUND.equals(response.getStatusCode()) && !response.hasBody());
    }

    @Test
    public void testGetNotExistingQuestion() throws Exception {
        long question_id = 20;
        ResponseEntity<QuizQuestion> response = this.restTemplate.exchange(GET_QUESTION_API + "/" + question_id, HttpMethod.GET, null, new ParameterizedTypeReference<QuizQuestion>() {
        });

        assertTrue(null != response && HttpStatus.NOT_FOUND.equals(response.getStatusCode()));
    }

    @Test
    public void testUpdateQuestion() throws Exception {
        long question_id = 1;
        ResponseEntity<QuizQuestion> response = this.restTemplate.exchange(GET_QUESTION_API + "/" + question_id, HttpMethod.GET, null, new ParameterizedTypeReference<QuizQuestion>() {
        });

        assertTrue(null != response && HttpStatus.OK.equals(response.getStatusCode()));

        QuizQuestion question = response.getBody();

        question.setQuestion("New Updated Question Text");

        response = this.restTemplate.exchange(UPDATER_QUESTION_API + "/" + question.getId(), HttpMethod.POST, new HttpEntity<>(question), new ParameterizedTypeReference<QuizQuestion>() {
        });

        assertTrue(null != response && HttpStatus.OK.equals(response.getStatusCode()));

        question = response.getBody();

        assertThat(question.getQuestion(), is("New Updated Question Text"));
    }

    private static Quiz newDummeQuiz() {
        Quiz result = new Quiz();
        result.setName("New Test Quiz");


        List<QuizSection> sections = new ArrayList<>();
        result.setSections(sections);

        QuizSection section = new QuizSection();
        sections.add(section);

        {
            List<QuizQuestion> questions = new ArrayList<>();
            section.setQuizQuestions(questions);

            QuizQuestion question = new QuizQuestion();
            questions.add(question);

            question.setQuestion("Quiz Question 1");

            List<QuizAnswer> answers = new ArrayList<>();
            {
                QuizAnswer answer = new QuizAnswer();
                answers.add(answer);

                answer.setAnswer("Quiz Question Answer 1");

                question.setAnswers(answers);
            }
        }
        return result;
    }
}