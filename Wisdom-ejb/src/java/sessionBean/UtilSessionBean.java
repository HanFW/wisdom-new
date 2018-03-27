/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.QuestionEntity;
import entity.ReaderEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chuck
 */
@Stateless
public class UtilSessionBean implements UtilSessionBeanLocal {

    private static final Logger LOGGER
            = Logger.getLogger(UtilSessionBean.class.getName());
    private static ConsoleHandler handler = null;

    @PersistenceContext(unitName = "Wisdom-ejbPU")
    private EntityManager em;

    public UtilSessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        LOGGER.setLevel(Level.FINEST);
        LOGGER.addHandler(handler);
    }

    /**
     *
     * @param reader
     * @return the newly created ReaderEntity (inc. id)
     */
    @Override
    public ReaderEntity createReader(ReaderEntity reader) {
        if (reader.getName() == null || reader.getEmail() == null
                || reader.getPwd() == null) {
            return null; // missing fields
        }

        ReaderEntity newReader
                = new ReaderEntity(reader.getName(), reader.getEmail(), reader.getPwd());

        em.persist(newReader);
        em.flush();
        em.refresh(newReader); // retrieve id

        return newReader;
    }

    /**
     *
     * @param email
     * @return false ONLY if email is valid and no duplicate is found
     */
    @Override
    public boolean readerHasEmailConflict(String email) {
        if (email == null || email.isEmpty()) {
            return true; // invalid email as conflict exists
        }

        Query q = em.createQuery("select r from ReaderEntity r "
                + "where r.email = :email")
                .setParameter("email", email);
        try {
            q.getSingleResult();
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                return false; // no conflict
            }
        }

        return true;
    }

    /**
     *
     * @param email
     * @return false ONLY if email is valid and no duplicate is found
     */
    @Override
    public boolean authorHasEmailConflict(String email) {
        if (email == null || email.isEmpty()) {
            return true; // invalid email as conflict exists
        }

        Query q = em.createQuery("select a from AuthorEntity a "
                + "where a.email = :email")
                .setParameter("email", email);
        try {
            q.getSingleResult();
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                return false; // no conflict
            }
        }

        return true;
    }

    /**
     *
     * @param topics
     * @param readerId
     * @return ReaderEntity w updated interested topics list
     */
    @Override
    public ReaderEntity setInterestedTopics(ArrayList<String> topics, Long readerId) {
        if (topics == null || readerId == null) {
            return null;
        }

        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new EntityNotFoundException();
        }
        reader.setTopics(topics);
        em.merge(reader);

        return reader;
    }

    @Override
    public ArticleEntity getArticleById(Long articleId) {
        if (articleId == null) {
            return null;
        }

        ArticleEntity article = em.find(ArticleEntity.class, articleId);
        if (article == null) {
            throw new EntityNotFoundException();
        }

        return article;
    }
    
    /**
     * 
     * @param readerId
     * @return list of questions in DESC order (newest question to oldest)
     */
    @Override
    public List<QuestionEntity> getQuestionsByReader(Long readerId) {
        if (readerId == null) {
            return null;
        }

        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new EntityNotFoundException();
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
    public QuestionEntity getQuestionById(Long questionId) {
        if (questionId == null) {
            return null;
        }

        QuestionEntity question = em.find(QuestionEntity.class, questionId);
        if (question == null) {
            throw new EntityNotFoundException();
        }

        return question;
    }

    /**
     * 
     * @param readerId
     * @return list of articles created by authors followed by the reader
     * in DESC order (newest articles to oldest)
     */
    @Override 
    public List<ArticleEntity> getNewestArticlesOfFollowedAuthors(Long readerId) {
        if (readerId == null) {
            return null;
        }

        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new EntityNotFoundException();
        }
        
        Query q = em.createQuery("select a from ArticleEntity a, FollowEntity f "
                + "where f.reader.id = :readerId "
                + "and f.author.id = a.author.id "
                + "order by a.created DESC") // TODO: newest articles to oldest
                .setParameter("readerId", readerId);
        List<ArticleEntity> articles = null;
        try {
            articles = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        
        return articles;
    }
    
    /**
     * 
     * @param topic
     * @return list of articles of a certain topic, 
     * created in the past 3 days, from most upvotes to fewest
     */
    @Override 
    public List<ArticleEntity> getMostLikedArticlesOfTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            return null;
        }
        
        Query q = em.createQuery("select a from ArticleEntity a "
                + "where a.topic = :topic "
                + "and a.created > :threeDaysAgo "
                + "order by a.numOfUpvotes DESC") // most upvotes to fewest
                .setParameter("topic", topic)
                .setParameter("threeDaysAgo", LocalDateTime.now().minusDays(3));
        List<ArticleEntity> articles = null;
        try {
            articles = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        
        return articles;
    }
}
