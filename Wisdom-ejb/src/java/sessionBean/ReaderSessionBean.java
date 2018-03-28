/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.FollowEntity;
import entity.ReaderEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author sherry
 */
@Stateless
public class ReaderSessionBean implements ReaderSessionBeanLocal {

    private static final Logger LOGGER = Logger.getLogger(ReaderSessionBean.class.getName()); // used to output info
    private static ConsoleHandler handler = null; // set logger's output to console

    @PersistenceContext(unitName = "Wisdom-ejbPU")
    private EntityManager em;

    public ReaderSessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
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
    public ReaderEntity followAuthor(Long authorId, Long readerId) throws Exception{
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (reader == null || author == null) {
            throw new EntityNotFoundException();
        }

        Query q = em.createNamedQuery("FollowEntity.findByAuthorAndReader")
                .setParameter("authorId", authorId)
                .setParameter("readerId", readerId);
        FollowEntity relationship;
        try {
            relationship = (FollowEntity) q.getSingleResult();
            throw new EntityExistsException("Error! Author is already followed by this reader");
        } catch (NoResultException e) {
            relationship = new FollowEntity();
            relationship.setAuthor(author);
            relationship.setReader(reader);
            em.persist(relationship);
            em.flush();
        } catch (Exception e) {
            // TODO: handle all other unexpected exceptions
        } 
        return reader;
    }

    @Override
    public List<AuthorEntity> getAllFollowingAuthors(Long readerId) {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) {
            return null;
        }
        
        Query q = em.createNamedQuery("AuthorEntity.findFollowedAuthorsByReader")
                .setParameter("readerId", reader.getId());
        List<AuthorEntity> followedAuthors = null;
        try {
            followedAuthors = q.getResultList();
        } catch (Exception e) {
            //
        }
        
        return followedAuthors;
    }

    @Override
    public ReaderEntity topUpWallet(Long readerId, Double amount) {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) {
            return null;
        }
        
        Double newBalance = reader.getBalance() + amount;
        LOGGER.log(Level.FINEST, "the new balance is calculated as {0}", newBalance);
        reader.setBalance(newBalance);
        em.merge(reader);
        return reader;
    }
    
}
