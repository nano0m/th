package es.nitaur.service;

import com.google.common.collect.Lists;
import es.nitaur.model.*;
import es.nitaur.repository.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import javax.persistence.*;
import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;


    @Override
    public Collection<Quiz> findAll() {
        final Collection<Quiz> quizzes = this.quizRepository.findAll();
        return quizzes;
    }

    @Override
    public Quiz findOne(final Long id) {
        final Quiz quiz = this.quizRepository.findOne(id);
        return quiz;
    }

    public Quiz create(final Quiz quiz) {
        if (quiz.getId() != null) {
            logger.error("Attempted to create a Quiz, but id attribute was not null.");
            throw new EntityExistsException("Cannot create new Quiz with supplied id. The id attribute must be null to create an entity.");
        }

        final Quiz saved_quiz = this.quizRepository.save(quiz);
        return saved_quiz;
    }

    @Override
    public Quiz update(Quiz quiz) {
        return this.quizRepository.save(quiz);
    }

    @Override
    public void delete(final Long id) {
        this.quizRepository.delete(id);
    }

    @Override
    public QuizQuestion updateQuestion(Long id, QuizQuestion question) {
        QuizQuestion question_to_update = this.quizQuestionRepository.findOne(id);

        if (null == question_to_update) {
            logger.error("Attempted to update Question, but Question is not found");

            throw new NoResultException("Cannot update Question with supplied id. The object is not found.");
        }

        question_to_update.setQuestion(question.getQuestion());
        question_to_update.setAnswers(question.getAnswers());

        return this.quizQuestionRepository.save(question_to_update);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    public QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers) {
        QuizQuestion question_to_update = this.quizQuestionRepository.findOne(id);

        if (null == question_to_update) {
            logger.error("Attempted to answer Question, but Question is not found");
            throw new NoResultException("Cannot answer Question with supplied id. The object is not found.");
        }
        question_to_update.setAnswers(quizAnswers);
        question_to_update.setUpdateCount(question_to_update.getUpdateCount() + 1);

        QuizQuestion saved_question = this.quizQuestionRepository.save(question_to_update);

        return saved_question;
    }

    @Override
    public QuizQuestion getQuestion(Long id) {
        QuizQuestion questions = this.quizQuestionRepository.findOne(id);

        if (null == questions) {
            logger.error("Attempted to answer Question, but Question is not found");
            throw new NoResultException("Cannot answer Question with supplied id. The object is not found.");
        }

        return questions;
    }

    @Override
    public Collection<QuizQuestion> getAllQuestions() {
        final Collection<QuizQuestion> qs = this.quizQuestionRepository.findAll();
        return qs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions) {
        List<QuizQuestion> updated_questions = Lists.newArrayList();
        QuizQuestion question_to_update = null;

        for (QuizQuestion quiz_question : quizQuestions) {
            question_to_update = this.quizQuestionRepository.findOne(quiz_question.getId());
            question_to_update.setQuestion(quiz_question.getQuestion());
            updated_questions.add(this.quizQuestionRepository.save(question_to_update));
        }

        return updated_questions;
    }

    @Override
    public List<QuizQuestion> getQuestions(Long filterBySectionId) {
        List<QuizQuestion> all = this.quizQuestionRepository.findAll();

        if (null != filterBySectionId) {
            Iterator<QuizQuestion> iterator = all.iterator();

            while (iterator.hasNext()) {
                QuizQuestion next = iterator.next();
                Long section_id = next.getSection().getId();

                if (!filterBySectionId.equals(section_id)) {
                    iterator.remove();
                }
            }
        }
        return all;
    }
}