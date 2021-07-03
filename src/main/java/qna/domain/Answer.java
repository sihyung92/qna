package qna.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;

import javax.persistence.*;

@Entity
public class Answer extends BaseEntity {
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

    public Answer(User writer, Question question, String contents) {
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
        if (!isWriter(loginUser)) {
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

    private boolean isWriter(User user) {
        return this.writer.equals(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
