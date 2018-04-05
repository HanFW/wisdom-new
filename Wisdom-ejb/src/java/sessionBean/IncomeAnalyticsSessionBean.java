/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.IncomeAnalyticsEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yongxue
 */
@Stateless
public class IncomeAnalyticsSessionBean implements IncomeAnalyticsSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
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
}
