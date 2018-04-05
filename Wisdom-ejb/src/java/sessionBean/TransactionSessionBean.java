/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.TransactionEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yongxue
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TransactionEntity> getTransactionByAuthorId(Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        if (author.getId() == null) {
            return new ArrayList<TransactionEntity>();
        }
        try {
            Query query = entityManager.createQuery("Select t From TransactionEntity t Where t.to=:author");
            query.setParameter("author", author);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<TransactionEntity>();
        }
    }

    @Override
    public List<TransactionEntity> getTransactionByTypeMonthly(String transactionType, Integer monthValue, Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        try {
            Query query = entityManager.createQuery("Select t From TransactionEntity t Where t.transactionType=:transactionType And t.createdMonth=:monthValue And t.to=:author");
            query.setParameter("transactionType", transactionType);
            query.setParameter("monthValue", monthValue);
            query.setParameter("author", author);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<TransactionEntity>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<TransactionEntity>();
        }
    }
}
