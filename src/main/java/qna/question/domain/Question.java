package qna.question.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;
import qna.BaseTimeEntity;
import qna.user.domain.User;
import qna.history.domain.DeleteHistory;

import javax.persistence.*;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "writer_id")
    private User writer;
    private boolean deleted = false;

    protected Question() {
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public Question(String title, String contents, User writer) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public void deleteBy(User loginUser) throws CannotDeleteException {
        if (!writer.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;
    }

    public DeleteHistory deleteHistory() {
        if (!this.deleted) {
            throw new ForbiddenException("삭제되지 않은 자료입니다. 히스토리를 생성할 수 없습니다.");
        }
        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}
