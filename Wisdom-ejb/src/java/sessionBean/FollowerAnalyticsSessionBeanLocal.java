/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.FollowerAnalyticsEntity;
import exception.NoSuchEntityException;
import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface FollowerAnalyticsSessionBeanLocal {

    public Long addNewFollowerAnalytics(Integer currentYear);

    public FollowerAnalyticsEntity getFollowAnalyticsById(Long followAnalyticsId) throws NoSuchEntityException;

    public void updateFollowersMonthly(Integer currentYear, Integer monthValue,
            Long followerAnalyticsId, Long authorId);

    public void updateAllMonthToZero(Long followerAnalyticsId);
}
