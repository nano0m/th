package es.nitaur.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "QUIZ")
public class Quiz extends GenericEntity {
    private static final long serialVersionUID = 8140813951014053993L;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizSection> sections;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<QuizSection> getSections() {
        return this.sections;
    }

    public void setSections(List<QuizSection> quizSections) {
        this.sections = quizSections;
    }
}