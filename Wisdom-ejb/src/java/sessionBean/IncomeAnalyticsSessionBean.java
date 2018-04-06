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
            Double monthlyRewardIncome, Double monthlyQuestionIncome) {

        IncomeAnalyticsEntity incomeAnalytics = new IncomeAnalyticsEntity();

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
}
