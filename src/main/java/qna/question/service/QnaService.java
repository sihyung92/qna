package qna.question.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.history.service.DeleteHistoryService;
import qna.question.domain.Question;
import qna.question.domain.QuestionRepository;
import qna.question.answer.domain.Answer;
import qna.question.answer.domain.AnswerRepository;
import qna.history.domain.DeleteHistory;
import qna.user.domain.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        DeleteHistory questionDeleteHistory = deleteQuestionOnly(loginUser, questionId);

        List<DeleteHistory> deleteHistories = deleteAnswer(loginUser, questionId);
        deleteHistories.add(questionDeleteHistory);

        deleteHistoryService.saveAll(deleteHistories);
    }

    @Transactional
    public List<DeleteHistory> deleteAnswer(User loginUser, Long questionId) throws CannotDeleteException {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        for (Answer answer : answers) {
            answer.deleteBy(loginUser);
        }

        return answers.stream()
                .map(Answer::deleteHistory)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Answer> findAnswerByQuestionId(Long questionId) {
        return answerRepository.findByQuestionIdAndDeletedFalse(questionId);
    }

    @Transactional
    public DeleteHistory deleteQuestionOnly(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        question.deleteBy(loginUser);
        return question.deleteHistory();
    }

}

