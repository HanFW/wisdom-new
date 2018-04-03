/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.FollowEntity;
import entity.ReaderEntity;
import exception.DuplicateEntityException;
import exception.NoSuchEntityException;
import exception.RepeatActionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
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
    public ReaderEntity createReader(ReaderEntity reader) throws DuplicateEntityException {
        if (reader.getName() == null || reader.getEmail() == null
                || reader.getPwd() == null) { // necessary fields for constructor
            return null; // missing fields
        }

        if (readerHasEmailConflict(reader.getEmail())) {
            throw new DuplicateEntityException("reader " + reader.getEmail() + " exists.");
        }

        ReaderEntity newReader
                = new ReaderEntity(reader.getName(), reader.getEmail(), reader.getPwd());

        em.persist(newReader);
        em.flush();
        em.refresh(newReader); // retrieve id

        return newReader;
    }

    @Override
    public ReaderEntity authenticateReader(String email, String pwd) throws NoSuchEntityException {
        if (email == null || pwd == null
                || email.isEmpty() || pwd.isEmpty()) {
            return null;
        }

        Query q = em.createQuery("select r from ReaderEntity r "
                + "where r.email = :email")
                .setParameter("email", email);
        ReaderEntity reader = null;
        try {
            reader = (ReaderEntity) q.getSingleResult();

            if (pwd.equals(reader.getPwd())) { // pwd match
                return reader;
            } else {
                return null;
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) { // email not found
                throw new NoSuchEntityException("reader " + email + " not found");
            }
            LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
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

        return true; // has conflict OR unexpected exceptions 
    }

    /**
     *
     * @param topics
     * @param readerId
     * @return ReaderEntity w updated interested topics list
     */
    @Override
    public ReaderEntity setInterestedTopics(ArrayList<String> topics, Long readerId)
            throws NoSuchEntityException {
        if (topics == null || readerId == null) {
            return null;
        }

        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new NoSuchEntityException("reader " + readerId + " not found");
        }
        reader.setTopics(topics);
        em.merge(reader);

        return reader;
    }

    /**
     *
     * @param authorId
     * @param readerId
     * @return
     * @throws NoSuchEntityException
     * @throws RepeatActionException
     */
    @Override
    public ReaderEntity followAuthor(Long authorId, Long readerId) throws NoSuchEntityException, RepeatActionException{
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (reader == null || author == null) {
            throw new NoSuchEntityException("author " + authorId + " or reader " + readerId + " not found");
        }

        Query q = em.createNamedQuery("FollowEntity.findByAuthorAndReader")
                .setParameter("authorId", authorId)
                .setParameter("readerId", readerId);
        List<FollowEntity> relationships = new ArrayList<>();
        try {
            relationships = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Unexpected exception at getResultList");
            // TODO: handle all other unexpected exceptions
        }
        if(relationships.isEmpty()){// not following
            FollowEntity relationship = new FollowEntity();
            relationship.setAuthor(author);
            relationship.setReader(reader);
            em.persist(relationship);
            em.flush();
        }else{//already following
            throw new RepeatActionException("Error! Author is already followed by this reader");
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

    @Override
    public ReaderEntity unfollowAuthor(Long authorId, Long readerId) throws NoSuchEntityException, RepeatActionException {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (reader == null || author == null) {
            throw new NoSuchEntityException("author " + authorId + " or reader " + readerId + " not found");
        }

        Query q = em.createNamedQuery("FollowEntity.findByAuthorAndReader")
                .setParameter("authorId", authorId)
                .setParameter("readerId", readerId);
        FollowEntity relationship;
        try {
            relationship = (FollowEntity) q.getSingleResult();
            relationship.setStatus("DELETED");
            em.merge(relationship);
            em.flush();
        } catch (NoResultException e) {
            throw new EntityExistsException("Error! Author is not followed by this reader");

        } catch (Exception e) {
            // TODO: handle all other unexpected exceptions
        }
        return reader;
    }

    @Override
    public Boolean checkFollow(Long authorId, Long readerId) throws NoSuchEntityException {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        AuthorEntity author = em.find(AuthorEntity.class, authorId);
        if (reader == null || author == null) {
            throw new NoSuchEntityException("author " + authorId + " or reader " + readerId + " not found");
        }

        Query q = em.createNamedQuery("FollowEntity.findByAuthorAndReader")
                .setParameter("authorId", authorId)
                .setParameter("readerId", readerId);
        FollowEntity relationship;
        try {
            relationship = (FollowEntity) q.getSingleResult();
            if (relationship != null) {
                return true;
            }
        } catch (NoResultException e) {
            return false;

        } catch (Exception e) {
            // TODO: handle all other unexpected exceptions
        }
        return false;
    }

}
