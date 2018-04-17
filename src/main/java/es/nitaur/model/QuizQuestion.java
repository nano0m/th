package es.nitaur.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "QUIZ_QUESTION")
public class QuizQuestion extends GenericEntity {
    private static final long serialVersionUID = 4600054923903723308L;

    private String question;

    @Column(name = "update_count")
    private Long updateCount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizAnswer> answers;

    @ManyToOne
    @JoinColumn(name = "section_fk")
    private QuizSection section;

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public List<QuizAnswer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(List<QuizAnswer> quizAnswers) {
        this.answers = quizAnswers;
    }

    @JsonIgnore
    public QuizSection getSection() {
        return this.section;
    }

    public void setSection(QuizSection quizSection) {
        this.section = quizSection;
    }

    public Long getUpdateCount() {
        return this.updateCount;
    }

    public void setUpdateCount(final Long updateCount) {
        this.updateCount = updateCount;
    }
}
