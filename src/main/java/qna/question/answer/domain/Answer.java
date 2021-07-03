package qna.question.answer.domain;

import qna.BaseTimeEntity;
import qna.CannotDeleteException;
import qna.ForbiddenException;
import qna.history.domain.DeleteHistory;
import qna.question.domain.ContentType;
import qna.question.domain.Question;
import qna.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private String contents;
    private boolean deleted = false;

    public Answer() {
    }

    public Answer(Question question, User writer, String contents) {
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public Answer(Long id, Question question, User writer, String contents) {
        this.id = id;
        this.question = question;
        this.writer = writer;
        this.contents = contents;
    }

    public void deleteBy(User loginUser) throws CannotDeleteException {
        if (!writer.equals(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.deleted = true;
    }

    public DeleteHistory deleteHistory() {
        if (!this.deleted) {
            throw new ForbiddenException("삭제되지 않은 자료입니다. 히스토리를 생성할 수 없습니다.");
        }
        return new DeleteHistory(ContentType.ANSWER, this.id, writer);
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
