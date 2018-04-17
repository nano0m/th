package es.nitaur;

import es.nitaur.model.Quiz;
import es.nitaur.model.QuizAnswer;
import es.nitaur.model.QuizQuestion;
import es.nitaur.model.QuizSection;
import es.nitaur.service.QuizService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizService quizService;

    @RequestMapping(value = "/api/quiz",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Quiz>> getQuizzes() {
        logger.info("getQuizzes()");

        final Collection<Quiz> quizzes = this.quizService.findAll();

        logger.debug("getQuestions()->found({})", quizzes.size());

        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> getQuiz(@PathVariable final Long id) {
        logger.info("getQuiz({})", id);

        final Quiz quiz = this.quizService.findOne(id);

        if (null == quiz) {
            logger.debug("getQuiz({})->not found", id);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> createQuiz(@RequestBody final Quiz quiz) {
        logger.info("createQuiz({})", null != quiz ? quiz.getName() : "null");

        List<QuizSection> sections = quiz.getSections();

        for (QuizSection section : sections) {
            section.setQuiz(quiz);
            List<QuizQuestion> quiz_questions = section.getQuizQuestions();

            for (QuizQuestion question : quiz_questions) {
                question.setSection(section);
            }
        }

        final Quiz saved_quiz = this.quizService.create(quiz);

        return new ResponseEntity<>(saved_quiz, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/quiz/delete/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable("id") final Long id) {
        logger.info("deleteQuiz({})", id);

        this.quizService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/api/quiz/updateQuestion/{id}",
            method = RequestMethod.POST)
    public ResponseEntity<QuizQuestion> updateQuestion(@PathVariable("id") final Long id, @RequestBody final QuizQuestion quizQuestion) {
        logger.info("updateQuestion({}, {})", id, null != quizQuestion ? quizQuestion.getId() : "null");

        QuizQuestion update_question = this.quizService.updateQuestion(id, quizQuestion);

        return new ResponseEntity<>(update_question, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/updateQuestions",
            method = RequestMethod.POST)
    public ResponseEntity<List<QuizQuestion>> updateQuestions(@RequestBody final List<QuizQuestion> quizQuestions) {
        StringBuffer question_ids = new StringBuffer();

        if (null != quizQuestions) {
            for (QuizQuestion question : quizQuestions) {
                question_ids.append(question.getId()).append(",");
            }

            if (question_ids.length() > 0) {
                question_ids.setLength(question_ids.length() - 1);
            }
        }

        logger.info("updateQuestions({})", question_ids.toString().trim());

        List<QuizQuestion> update_question = this.quizService.updateQuestions(quizQuestions);

        return new ResponseEntity<>(update_question, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/getQuestion/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<QuizQuestion> getQuestion(@PathVariable("id") final Long id) {
        logger.info("getQuestion({})", id);

        QuizQuestion question = this.quizService.getQuestion(id);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/allQuestions",
            method = RequestMethod.GET)
    public ResponseEntity<List<QuizQuestion>> getQuestions(@RequestParam(value = "filterSectionId", required = false) Long filterBySectionId) {
        logger.info("getQuestions({})", filterBySectionId);

        List<QuizQuestion> questions = this.quizService.getQuestions(filterBySectionId);

        logger.debug("getQuestions({})->found({})", filterBySectionId, questions.size());

        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/answerQuestion/{id}",
            method = RequestMethod.POST)
    public ResponseEntity<QuizQuestion> answer(@PathVariable("id") final Long id, @RequestBody final List<QuizAnswer> quizAnswers) {
        StringBuffer answers = new StringBuffer();

        if (null != quizAnswers) {
            for (QuizAnswer question : quizAnswers) {
                answers.append(question.getAnswer()).append(",");
            }

            if (answers.length() > 0) {
                answers.setLength(answers.length() - 1);
            }
        }

        logger.info("answer({}, \"{}\")", id, answers.toString().trim());
        QuizQuestion updated_question = this.quizService.answerQuestion(id, quizAnswers);

        return new ResponseEntity<>(updated_question, HttpStatus.OK);
    }
}