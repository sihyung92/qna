package qna.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;

import javax.persistence.*;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    @ManyToOne
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
        if (!isWriter(loginUser)) {
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

    private boolean isWriter(User user) {
        return this.writer.equals(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
