package qna.question.answer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.ForbiddenException;
import qna.question.question.domain.QuestionTest;
import qna.user.domain.User;
import qna.user.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    private final Answer A1 = AnswerFixture.createA1();
    private final Answer A2 = AnswerFixture.createA2();
    private final User JAVAJIGI = UserFixture.createJAVAJIGI();
    private final User SANJIGI = UserFixture.createSANJIGI();

    @DisplayName("삭제 상태가 변경되는지 확인")
    @Test
    void deleteBy() throws CannotDeleteException {
        //given
        //when
        A1.deleteBy(JAVAJIGI);
        A2.deleteBy(SANJIGI);

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
        assertThatThrownBy(() -> A1.deleteBy(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> A2.deleteBy(JAVAJIGI))
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

    public static class AnswerFixture {
        public static Answer createA1(){
            return new Answer(QuestionTest.QuestionFixture.createQ1(), UserFixture.createJAVAJIGI(), "Answers Contents1");
        }

        public static Answer createA2(){
            return new Answer(QuestionTest.QuestionFixture.createQ2(), UserFixture.createSANJIGI(), "Answers Contents2");
        }
    }
}
