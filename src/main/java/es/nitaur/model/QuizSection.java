package es.nitaur.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "QUIZ_SECTION")
public class QuizSection extends GenericEntity {
    private static final long serialVersionUID = -152967961913880574L;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "section_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizQuestion> quizQuestions;

    @ManyToOne
    @JoinColumn(name = "quiz_fk")
    private Quiz quiz;

    public List<QuizQuestion> getQuizQuestions() {
        return this.quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> questions) {
        this.quizQuestions = questions;
    }

    @JsonIgnore
    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz questionQuiz) {
        this.quiz = questionQuiz;
    }
}