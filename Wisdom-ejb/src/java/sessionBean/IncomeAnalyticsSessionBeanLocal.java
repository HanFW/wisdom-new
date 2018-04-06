/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.IncomeAnalyticsEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface IncomeAnalyticsSessionBeanLocal {

    public Long addNewIncomeAnalytics(Integer currentYear, Integer monthValue,
            Double monthlyRewardIncome, Double monthlyQuestionIncome);

    public List<IncomeAnalyticsEntity> getIncomeAnalyticsByAuthorId(Long authorId);
}
