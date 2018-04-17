package es.nitaur;

import es.nitaur.model.*;
import es.nitaur.repository.*;
import es.nitaur.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.invocation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import javax.persistence.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class QuizServiceTest {

    @InjectMocks
    private QuizService quizService = new QuizServiceImpl();

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizQuestionRepository quizQuestionRepository;


    private static QuizAnswer newAnswer(Long id, String answer) {
        QuizAnswer result = new QuizAnswer();
        result.setId(id);
        result.setAnswer(answer);

        return result;
    }

    private static Quiz newQuiz(Long id, String name) {
        Quiz result = new Quiz();
        result.setName(name);
        result.setId(id);

        return result;
    }

    private static QuizQuestion newQuestion(Long id, Long updateCount, String question) {
        QuizQuestion result = new QuizQuestion();
        result.setId(id);
        result.setUpdateCount(updateCount);
        result.setQuestion(question);

        return result;
    }

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);

        when(this.quizQuestionRepository.save(Matchers.any(QuizQuestion.class))).thenAnswer((InvocationOnMock invocationOnMock) -> {
            QuizQuestion result = (QuizQuestion) invocationOnMock.getArguments()[0];
            return result;
        });

        when(this.quizQuestionRepository.findOne(Matchers.any(Long.class))).thenAnswer((InvocationOnMock invocationOnMock) -> {
            QuizQuestion result = null;
            Long id = (Long) invocationOnMock.getArguments()[0];

            if (2 != id) {
                result = newQuestion(id, 3L, "Quiz question with ID:" + id);
            }

            return result;
        });

        when(this.quizQuestionRepository.findAll()).thenAnswer((InvocationOnMock invocationOnMock) -> {
            List<QuizQuestion> result = new ArrayList<>();

            long section_id = 0;
            QuizSection section = null;
            QuizQuestion question = null;

            for (int i = 0; i < 10; i++) {
                question = newQuestion(Long.valueOf(i), 3L, "Quiz question with ID:" + i);
                section = new QuizSection();
                section.setId(section_id);
                question.setSection(section);

                if (i % 2 == 0) {
                    section_id++;
                }

                result.add(question);
            }

            return result;
        });

        when(this.quizService.findAll()).thenAnswer((InvocationOnMock invocationOnMock) -> {
            Collection<Quiz> quiz_result = new ArrayList<>();
            quiz_result.add(newQuiz(1L, "First Quiz"));
            quiz_result.add(newQuiz(2L, "Second Quiz"));
            quiz_result.add(newQuiz(3L, "Third Quiz"));

            return quiz_result;
        });

        when(this.quizService.findOne(1L)).thenAnswer((InvocationOnMock invocationOnMock) -> {
            Quiz quiz_result = newQuiz((Long) invocationOnMock.getArguments()[0], "First Quiz");

            return quiz_result;
        });
    }

    @Test
    public void testFindAllQuiz() {
        Collection<Quiz> quizzes = this.quizService.findAll();

        verify(this.quizRepository, times(1)).findAll();
        assertThat(quizzes, is(notNullValue()));
        assertThat("Retrieved quiz cont", quizzes.size(), is(3));
    }

    @Test
    public void testFindOneQuiz() {
        Long expected_id = 1L;
        Quiz quiz = this.quizService.findOne(expected_id);

        verify(this.quizRepository, times(1)).findOne(expected_id);
        assertThat(quiz, is(notNullValue()));
        assertThat("Quiz name", quiz.getName(), is("First Quiz"));
        assertThat("Quiz ID", quiz.getId(), is(expected_id));
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateFailureQuizWithSameID() throws Exception {
        Quiz q = new Quiz();
        q.setId(1L);
        this.quizService.create(q);
    }

    @Test
    public void testUpdateQuiz() {
        Quiz quiz = newQuiz(1L, "First Quiz");
        this.quizService.update(quiz);

        verify(this.quizRepository, times(1)).save(quiz);
    }

    @Test
    public void testDeleteQuiz() {
        this.quizService.delete(1L);

        verify(this.quizRepository, times(1)).delete(1L);
    }

    @Test
    public void testCreateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setName("Test Name");

        when(this.quizService.create(quiz)).thenAnswer((InvocationOnMock invocationOnMock) -> {
            Quiz quiz_argument = (Quiz) invocationOnMock.getArguments()[0];
            quiz_argument.setId(1L);
            return quiz_argument;
        });

        this.quizService.create(quiz);

        Long actual = quiz.getId();

        verify(this.quizRepository, times(1)).save(quiz);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, greaterThan(0L));
    }

    @Test
    public void testUpdateQuestion() {
        Long question_id = 1L;
        QuizQuestion question = newQuestion(null, 4L, "Quiz question one updated");

        List<QuizAnswer> answers = new ArrayList<>();
        answers.add(newAnswer(1L, "Answer 1"));
        answers.add(newAnswer(2L, "Answer 2"));

        question.setAnswers(answers);


        QuizQuestion db_question = this.quizService.updateQuestion(question_id, question);

        verify(this.quizQuestionRepository, times(1)).findOne(question_id);
        verify(this.quizQuestionRepository, times(1)).save(db_question);

        assertThat(db_question, is(notNullValue()));
        assertThat(db_question.getId(), is(equalTo(question_id)));
        assertThat(db_question.getAnswers(), is(notNullValue()));

        db_question.getAnswers().forEach((QuizAnswer answer) -> {
            QuizAnswer prev_answer = answers.stream().filter((QuizAnswer x) -> answer.getId().equals(x.getId())).findAny().orElse(null);

            assertThat(answer.getAnswer(), is(prev_answer.getAnswer()));
        });
    }

    @Test
    public void testGetQuestion() {
        Long expected_id = 1L;
        QuizQuestion question = this.quizService.getQuestion(expected_id);

        verify(this.quizQuestionRepository, times(1)).findOne(expected_id);
        assertThat(question, is(notNullValue()));
        assertThat("Question", question.getQuestion(), is("Quiz question with ID:" + expected_id));
        assertThat("Question ID", question.getId(), is(expected_id));
    }

    @Test(expected = NoResultException.class)
    public void testGetQuestionNotFound() {
        Long expected_id = 2L;
        this.quizService.getQuestion(expected_id);
    }

    @Test
    public void testGetAllQuestions() {
        this.quizService.getAllQuestions();

        verify(this.quizQuestionRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateQuestions() {
        List<QuizQuestion> questions_to_update = new ArrayList<>();
        questions_to_update.add(newQuestion(1L, 3L, "Quiz question with ID:" + 1L));
        questions_to_update.add(newQuestion(3L, 2L, "Quiz question with ID:" + 3L));

        List<QuizQuestion> updated_questions = this.quizService.updateQuestions(questions_to_update);

        verify(this.quizQuestionRepository, times(2)).findOne(Matchers.any(Long.class));
        verify(this.quizQuestionRepository, times(2)).save(Matchers.any(QuizQuestion.class));

        updated_questions.forEach((QuizQuestion question) -> {
            QuizQuestion prev_question = questions_to_update.stream().filter((QuizQuestion x) -> question.getId().equals(x.getId())).findAny().orElse(null);

            assertThat(question.getQuestion(), is(prev_question.getQuestion()));
        });
    }

    @Test
    public void testGetQuestions() {
        long section_id = 2L;
        Collection<QuizQuestion> question = this.quizService.getQuestions(section_id);

        verify(this.quizQuestionRepository, times(1)).findAll();
        assertThat(question, is(notNullValue()));
        assertThat("Question count in section", question.size(), is(2));
    }
}