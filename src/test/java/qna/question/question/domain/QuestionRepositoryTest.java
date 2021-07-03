package qna.question.question.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import qna.question.domain.Question;
import qna.question.domain.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;
import qna.user.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private Question question;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = userRepository.save(UserFixture.createJAVAJIGI());
        question = questionRepository.save(new Question(1L, "title1", "contents1", user1));
    }

    @Transactional
    @Test
    public void insert_성공() {
        //given
        //when
        Question question = new Question("title1", "content", user1);
        questionRepository.save(question);
        Question expectedQuestion = questionRepository.findById(question.getId()).get();

        //then
        assertThat(expectedQuestion).isEqualTo(question);
    }

    @Test
    public void delete_성공() throws Exception {
        //given
        assertThat(question.isDeleted()).isFalse();
        //when
        User writer = userRepository.findById(user1.getId()).get();
        question.deleteBy(writer);
        //then
        Question persistedQuestion  = questionRepository.findById(question.getId()).get();
        assertThat(persistedQuestion.isDeleted()).isTrue();
    }
}
