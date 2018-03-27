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

    public void setEm(EntityManager em) {
        this.em = em;
    }

    //Testing Version - TBC..
    @Override
    public ReaderEntity readerSignUp(String name, String email, String pwd) {
        ReaderEntity newReader = new ReaderEntity(name, email, pwd);
        em.persist(newReader);
        em.flush();
        em.refresh(newReader);
        return newReader;
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
    public List<AuthorEntity> getAllFollowedAuthors(Long readerId) {
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
    public ArticleEntity likeArticle(Long articleId) {
        ArticleEntity article = em.find(ArticleEntity.class, articleId);
        if (article == null ) {
            return null;
        }

        article.setNumOfUpvotes(article.getNumOfUpvotes()+1);
        return article;
    }

    @Override
    public ReaderEntity saveArticle(Long readerId, Long articleId) throws Exception{
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        ArticleEntity article = em.find(ArticleEntity.class, articleId);
        if (reader == null || article == null) {
            return null;
        }

        if (reader.getSaved().contains(article)) {
            throw new Exception("Error! Author is already followed by this reader");
        }
        
        reader.getSaved().add(article);
        em.merge(reader);
        
        return reader;
    }

    @Override
    public List<ArticleEntity> getAllSavedArticles(Long readerId) {
        ReaderEntity reader = em.find(ReaderEntity.class, readerId);
        if (reader == null) {
            return null;
        }
        
        return reader.getSaved();
    }
    
    
    
    
    
    



    
}
