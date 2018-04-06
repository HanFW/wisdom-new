/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.IncomeAnalyticsEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
public class IncomeAnalyticsSessionBean implements IncomeAnalyticsSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewIncomeAnalytics(Integer currentYear, Integer monthValue,
            Double monthlyRewardIncome, Double monthlyQuestionIncome, Long authorId) {
        
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        
        IncomeAnalyticsEntity incomeAnalytics = new IncomeAnalyticsEntity(currentYear,
                monthValue, monthlyRewardIncome, monthlyQuestionIncome);
        incomeAnalytics.setAuthor(author);
        
        entityManager.persist(incomeAnalytics);
        entityManager.flush();
        
        return incomeAnalytics.getId();
    }
    
    @Override
    public List<IncomeAnalyticsEntity> getIncomeAnalyticsByAuthorId(Long authorId) {
        
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        
        if (author.getId() == null) {
            return new ArrayList<IncomeAnalyticsEntity>();
        }
        try {
            Query query = entityManager.createQuery("Select i From IncomeAnalyticsEntity i Where i.author=:author");
            query.setParameter("author", author);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<IncomeAnalyticsEntity>();
        }
    }
    
    @Override
    public String checkDuplicateIncomeAnalytics(Integer currentYear, Integer monthValue, Long authorId) {
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        
        try {
            Query query = entityManager.createQuery("Select i From IncomeAnalyticsEntity i Where i.author=:author And i.currentYear=:currentYear And i.monthValue=:monthValue");
            query.setParameter("author", author);
            query.setParameter("currentYear", currentYear);
            query.setParameter("monthValue", monthValue);
            
            if (query.getResultList().isEmpty()) {
                return "Empty";
            } else {
                return "Exist";
            }
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
            return "Multiple Entries";
        }
    }
    
    @Override
    public void updateIncome(Integer currentYear, Integer monthValue,
            Long authorId, Double monthlyRewardIncome, Double monthlyQuestionIncome) {
        
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        IncomeAnalyticsEntity incomeAnalytics = new IncomeAnalyticsEntity();
        
        try {
            Query query = entityManager.createQuery("Select i From IncomeAnalyticsEntity i Where i.author=:author And i.currentYear=:currentYear And i.monthValue=:monthValue");
            query.setParameter("author", author);
            query.setParameter("currentYear", currentYear);
            query.setParameter("monthValue", monthValue);
            
            incomeAnalytics = (IncomeAnalyticsEntity) query.getSingleResult();
            incomeAnalytics.setMonthlyRewardIncome(monthlyRewardIncome);
            incomeAnalytics.setMonthlyQuestionIncome(monthlyQuestionIncome);
            
            entityManager.flush();
            
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }
}
