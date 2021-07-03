package qna.question.answer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.user.domain.User;
import qna.user.domain.UserRepository;
import qna.user.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    private User user1;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = userRepository.save(UserFixture.createJAVAJIGI());
        answer = answerRepository.save(new Answer(null, user1, "content1"));
    }

    @Test
    public void insert_성공() {
        //given
        //when
        Answer answer = new Answer(null, user1, "content2");
        answerRepository.save(answer);
        Answer expectedAnswer = answerRepository.findById(answer.getId()).get();

        //then
        assertThat(expectedAnswer).isEqualTo(answer);
    }

    @Test
    public void delete_성공() throws CannotDeleteException {
        //given
        //when
        Answer persistedAnswer = answerRepository.findById(answer.getId()).get();
        assertThat(persistedAnswer.isDeleted()).isFalse();
        persistedAnswer.deleteBy(user1);

        //then
        Answer expectedAnswer = answerRepository.findById(this.answer.getId()).get();
        assertThat(expectedAnswer.isDeleted()).isTrue();
    }
}


