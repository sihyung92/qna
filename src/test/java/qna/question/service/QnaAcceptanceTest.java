package qna.question.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.question.answer.domain.Answer;
import qna.question.answer.domain.AnswerRepository;
import qna.question.domain.Question;
import qna.question.domain.QuestionRepository;
import qna.user.User;
import qna.user.UserFixture;
import qna.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class QnaAcceptanceTest {
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;
    private QnaService qnaService;

    private User user1;
    private User user2;
    private Question question;
    private Answer answer;

    public QnaAcceptanceTest(QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository, QnaService qnaService) throws Exception {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.qnaService = qnaService;
        setUp();
    }

    public void setUp() throws Exception {
        user1 = userRepository.save(UserFixture.createJAVAJIGI());
        user2 = userRepository.save(UserFixture.createSANJIGI());
        question = questionRepository.save(new Question(1L, "title1", "contents1", user1));
        answer = answerRepository.save(new Answer(1L, question, user1, "Answers Contents1"));
        answerRepository.save(new Answer(2L, question, user1, "Answers Contents1"));
        answerRepository.save(new Answer(3L, question, user1, "Answers Contents1"));
    }

    @Transactional
    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        //given
        //when
        assertThat(question.isDeleted()).isFalse();

        //then
        Question questionById = qnaService.findQuestionById(question.getId());
        List<Answer> answerFromQuestion = qnaService.findAnswerByQuestionId(question.getId());

        qnaService.deleteQuestion(user1, question.getId());

        assertThat(questionById.isDeleted()).isTrue();
        answerFromQuestion.forEach(
                answer -> assertThat(answer.isDeleted()).isTrue()
        );
    }

    @Transactional
    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Transactional
    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        answerRepository.save(new Answer(2L, question, user2, "Answers Contents2"));
        assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }
}
