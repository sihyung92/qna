package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.QuestionFixture.createQ1(), "Answers Contents1");
    public final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.QuestionFixture.createQ2(), "Answers Contents2");

    @DisplayName("삭제 상태가 변경되는지 확인")
    @Test
    void deleteBy() throws CannotDeleteException {
        //given
        //when
        A1.deleteBy(UserTest.JAVAJIGI);
        A2.deleteBy(UserTest.SANJIGI);

        //then
        assertThat(A1.isDeleted()).isTrue();
        assertThat(A2.isDeleted()).isTrue();
    }

    @DisplayName("[예외] 다른 유저가 삭제하려고 할 때")
    @Test
    void whenDeleteAnotherUser() {
        //given
        //when
        //then
        assertThatThrownBy(() -> A1.deleteBy(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> A2.deleteBy(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("[예외] 삭제되지 않았을 때 delete history")
    @Test
    void whenCallDeleteHistoryFromAliveAnswer() {
        //given
        //when
        //then
        assertThatThrownBy(A1::deleteHistory)
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(A2::deleteHistory)
                .isInstanceOf(ForbiddenException.class);
    }

}
