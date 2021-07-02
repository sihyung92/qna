package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private String contents;
    private boolean deleted = false;

    public Answer() {
    }

    public Answer(User user, Question question, String contents) {
        this.user = user;
        this.question = question;
        this.contents = contents;
    }

    public Answer(Long id, Question question, User user, String contents) {
        this.id = id;
        this.question = question;
        this.user = user;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", question=" + question +
                ", user=" + user +
                '}';
    }
}
