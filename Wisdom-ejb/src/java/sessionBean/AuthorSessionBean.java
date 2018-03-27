/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AuthorEntity retrieveAuthorById(Long authorId) {
        AuthorEntity author = new AuthorEntity();

        try {
            Query query = entityManager.createQuery("Select a From AuthorEntity a Where a.id=:authorId");
            query.setParameter("authorId", authorId);

            if (query.getResultList().isEmpty()) {
                return new AuthorEntity();
            } else {
                author = (AuthorEntity) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new AuthorEntity();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return author;
    }

    @Override
    public Long createNewAuthor(String username, String description, String email, String password) {
        AuthorEntity author = new AuthorEntity(username, description, email, password);
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
            if(author.getPwd().equals(password)) {
                return author;
            } else {
                return null;
            }
        }
    }
    
}
