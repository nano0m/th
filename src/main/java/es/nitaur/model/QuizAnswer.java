package es.nitaur.model;

import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "QUIZ_ANSWER")
public class QuizAnswer extends GenericEntity {

    private static final long serialVersionUID = -1856042082304619929L;

    @NotNull
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_fk")
    private QuizQuestion question;

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    @JsonIgnore
    public QuizQuestion getQuestion() {
        return this.question;
    }

    public void setQuestion(final QuizQuestion quizQuestion) {
        this.question = quizQuestion;
    }
}
