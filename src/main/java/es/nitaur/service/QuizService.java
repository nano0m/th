package es.nitaur.service;

import es.nitaur.model.*;
import java.util.*;

public interface QuizService {

    Collection<Quiz> findAll();

    Quiz findOne(Long id);

    Quiz create(Quiz quiz);

    Quiz update(Quiz quiz);

    void delete(Long id);

    QuizQuestion updateQuestion(Long id, QuizQuestion quiz);

    QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers);

    QuizQuestion getQuestion(Long id);

    Collection<QuizQuestion> getAllQuestions();

    List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions);

    List<QuizQuestion> getQuestions(Long sectionId);
}