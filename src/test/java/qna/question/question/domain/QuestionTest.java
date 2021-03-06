package qna.question.question.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.ForbiddenException;
import qna.user.UserFixture;
import qna.question.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    private final Question Q1 = QuestionFixture.createQ1();
    private final Question Q2 = QuestionFixture.createQ2();

    @DisplayName("삭제 상태가 변경되는지 확인")
    @Test
    void deleteBy() throws CannotDeleteException {
        //given
        //when
        Q1.deleteBy(UserFixture.createJAVAJIGI());
        Q2.deleteBy(UserFixture.createSANJIGI());

        //then
        assertThat(Q1.isDeleted()).isTrue();
        assertThat(Q2.isDeleted()).isTrue();
    }

    @DisplayName("[예외] 다른 유저가 삭제하려고 할 때")
    @Test
    void whenDeleteAnotherUser() {
        //given
        //when
        //then
        assertThatThrownBy(() -> Q1.deleteBy(UserFixture.createSANJIGI()))
                .isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> Q2.deleteBy(UserFixture.createJAVAJIGI()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("[예외] 삭제되지 않았을 때 delete history")
    @Test
    void whenCallDeleteHistoryFromAliveAnswer() {
        //given
        //when
        //then
        assertThatThrownBy(Q1::deleteHistory)
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(Q2::deleteHistory)
                .isInstanceOf(ForbiddenException.class);
    }

    public static class QuestionFixture {
        public static Question createQ1() {
            return new Question("title1", "contents1", UserFixture.createJAVAJIGI());
        }

        public static Question createQ2() {
            return new Question("title2", "contents2", UserFixture.createSANJIGI());
        }
    }
}
