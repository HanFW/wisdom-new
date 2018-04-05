/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.FollowerAnalyticsEntity;
import exception.DuplicateEntityException;
import exception.NoSuchEntityException;
import java.time.LocalDateTime;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yongxue
 */
@Stateless
public class AuthorSessionBean implements AuthorSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB(name = "FollowerAnalyticsSessionBeanLocal")
    private FollowerAnalyticsSessionBeanLocal followerAnalyticsSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AuthorEntity retrieveAuthorById(Long authorId) {
        AuthorEntity author = new AuthorEntity();

        try {
            Query query = entityManager.createQuery("Select a From AuthorEntity a Where a.id=:authorId");
            query.setParameter("authorId", authorId);

            if (query.getResultList().isEmpty()) {
                return null;
            } else {
                author = (AuthorEntity) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return null;
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return author;
    }

    @Override
    public Long createNewAuthor(String username, String description, String email, String password, String picPath) throws DuplicateEntityException{
        if (username == null || email == null || password == null) {
            return null;
        }
        
        if (authorHasEmailConflict(email)) {
            throw new DuplicateEntityException("author " + email + " exists.");
        }
        
        FollowerAnalyticsEntity followerAnalytics = new FollowerAnalyticsEntity();

        LocalDateTime created = LocalDateTime.now();
        Long followerAnalyticsId = followerAnalyticsSessionBeanLocal.addNewFollowerAnalytics(Integer.valueOf(created.getYear()));

        try {
            followerAnalytics = followerAnalyticsSessionBeanLocal.getFollowAnalyticsById(followerAnalyticsId);
        } catch (NoSuchEntityException e) {
            // TODO:
        }

        AuthorEntity author = new AuthorEntity(username, description, email, password, picPath);
        author.setFollowerAnalytics(followerAnalytics);

        entityManager.persist(author);
        entityManager.flush();

        return author.getId();
    }

    @Override
    public AuthorEntity authorLogin(String email, String password) {
        AuthorEntity author = null;

        Query query = entityManager.createQuery("Select a From AuthorEntity a Where a.email=:email");
        query.setParameter("email", email);

        if (query.getResultList().isEmpty()) {
            System.out.println("Author login failed: invalid user.");
            return null;
        } else {
            author = (AuthorEntity) query.getSingleResult();
            if (author.getPwd().equals(password)) {
                return author;
            } else {
                return null;
            }
        }
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

        Query q = entityManager.createQuery("select a from AuthorEntity a "
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
}
