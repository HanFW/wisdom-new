/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.QuestionEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sherry
 */
@Local
public interface QuestionSessionBeanLocal {

    QuestionEntity getQuestionById(Long questionId) throws NoSuchEntityException;

    List<QuestionEntity> getQuestionsByReader(Long readerId) throws NoSuchEntityException;
    
    List<QuestionEntity> getQuestionsByAuthorId(Long authorId, String status) throws NoSuchEntityException;
    
    void rejectQuestion (Long questionId);
    
    void replyToQuestion (Long questionId, String reply);
    
    void updateQuestionPrice (Long authorId, Double newPrice);
    
    void checkExpiredQuestions ();

    ReaderEntity askQuestion(Long readerId, Long authorId, QuestionEntity question) throws NoSuchEntityException, InsufficientBalanceException;
}
