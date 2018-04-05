/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.CompensationEntity;
import entity.QuestionEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utility.Constants;

/**
 *
 * @author sherry
 */
@Stateless
public class QuestionSessionBean implements QuestionSessionBeanLocal {

    private static final Logger LOGGER = Logger.getLogger(ReaderSessionBean.class.getName()); // used to output info
    private static ConsoleHandler handler = null; // set logger's output to console

    @PersistenceContext(unitName = "Wisdom-ejbPU")
    private EntityManager em;

    public QuestionSessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
    }
    
    @Override
    public QuestionEntity getQuestionById(Long questionId) throws NoSuchEntityException {
        if (questionId == null) {
            return null;
        }

        QuestionEntity question = em.find(QuestionEntity.class, questionId);
        if (question == null) {
            throw new NoSuchEntityException("question " + questionId + " not found");
        }

        return question;
    }

    /**
     * 
     * @param readerId
     * @return list of questions in DESC order (newest question to oldest)
     */
    @Override
    public List<QuestionEntity> getQuestionsByReader(Long readerId) throws NoSuchEntityException {
        if (readerId == null) {
            return null;
        }

        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            LOGGER.log(Level.SEVERE, "0. reader {0} not found", readerId);
            throw new NoSuchEntityException("reader " + readerId + " not found");
        }

        Query q = em.createQuery("select q from QuestionEntity q "
                + "where q.reader.id = :readerId "
                + "order by q.created DESC") // TODO: newest questions to oldest
                .setParameter("readerId", readerId);
        List<QuestionEntity> questions = null;
        try {
            questions = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return questions;
    }
    
    @Override
    public List<QuestionEntity> getQuestionsByAuthorId(Long authorId, String status) throws NoSuchEntityException{
        if (authorId == null) {
            return null;
        }
        
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (author == null) {
            throw new NoSuchEntityException("author " + authorId + " not found.");
        }
        
        Query q = em.createQuery("select q from QuestionEntity q "
                + "where q.author.id = :authorId and q.status = :status "
                + "order by q.created DESC");
        q.setParameter("authorId", authorId);
        q.setParameter("status", status);
        
        List<QuestionEntity> questions = null;
        try {
            questions = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        
        return questions;
    }
    
    @Override
    public void rejectQuestion (Long questionId) {
        if (questionId == null) {
            LOGGER.log(Level.SEVERE, "invalid question id");
        }
        
        QuestionEntity question = em.find(QuestionEntity.class, questionId);
        if (question == null) {
            LOGGER.log(Level.SEVERE, "question entity not found");
        } else {
            //update question status
            question.setStatus(Constants.STATUS_REJECTED);
            
            //refund question price to reader
            question.getReader().setBalance(question.getReader().getBalance() + question.getPrice());
        }
    }
    
    @Override
    public void replyToQuestion (Long questionId, String reply) {
        if (questionId == null) {
            LOGGER.log(Level.SEVERE, "invalid question id");
        }
        
        QuestionEntity question = em.find(QuestionEntity.class, questionId);
        if (question == null) {
            LOGGER.log(Level.SEVERE, "question entity not found");
        } else {
            //update question
            question.setReply(reply);
            question.setStatus(Constants.STATUS_ANSWERED);
            
            //record transaction
            CompensationEntity compensation = new CompensationEntity(question.getPrice());
            compensation.setTransactionType(Constants.TRANSACTION_COMPENSATION);
            em.persist(compensation);
            
            // compensation - question
            compensation.setQuestion(question);
            question.setCompensation(compensation);
            // compensation - from (Reader)
            compensation.setFrom(question.getReader());
            // compensation - to (Author)
            compensation.setTo(question.getAuthor());            
            
            //credit question price to author account
            question.getAuthor().setBalance(question.getAuthor().getBalance() + question.getPrice());
            em.flush();
        }
    }
    
    @Override
    public void updateQuestionPrice (Long authorId, Double newPrice) {
        if (authorId == null) {
            LOGGER.log(Level.SEVERE, "invalid question id");
        }
        
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (author == null) {
            LOGGER.log(Level.SEVERE, "question entity not found");
        } else {
            author.setQtnPrice(newPrice);
        }
    }
    
    @Override
    public void checkExpiredQuestions () {
        Query q = em.createQuery("select q from QuestionEntity q where q.status = :status order by q.created ASC");
        q.setParameter("status", Constants.STATUS_PENDING);
        List<QuestionEntity> questions = null;
        try {
            questions = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        
        for(QuestionEntity question : questions) {
            if(question.getCreated().isBefore(LocalDateTime.now().minusMinutes(1))) {
                //update question status
                question.setStatus(Constants.STATUS_EXPIRED);
                System.out.println("question expired");
                
                //refund question price to reader
                question.getReader().setBalance(question.getReader().getBalance() + question.getPrice());
            } else {
                break;
            }
        }
    }

    @Override
    public ReaderEntity askQuestion(Long readerId, Long authorId, QuestionEntity question) throws NoSuchEntityException, InsufficientBalanceException {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (reader == null || author == null) {
            throw new NoSuchEntityException("author " + authorId + " or reader " + readerId + " not found");
        }
        
        
        if(author.getQtnPrice() > reader.getBalance()){
            throw new InsufficientBalanceException("Insufficient Balance!");
        }
        //deduct the question price from reader
        reader.setBalance(reader.getBalance() - author.getQtnPrice());
        em.merge(reader);
        
        //create question
        QuestionEntity newQuestion = new QuestionEntity(question.getTitle(), question.getContent());
        newQuestion.setAuthor(author);
        newQuestion.setReader(reader);
        newQuestion.setPrice(author.getQtnPrice());
        em.persist(newQuestion);
        em.flush();
        em.refresh(newQuestion);
        
        return reader;
    }
    
    
}
