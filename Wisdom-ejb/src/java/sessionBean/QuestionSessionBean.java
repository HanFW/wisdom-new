/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.QuestionEntity;
import entity.ReaderEntity;
import exception.NoSuchEntityException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
}
