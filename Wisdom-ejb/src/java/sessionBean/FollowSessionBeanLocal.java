/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface FollowSessionBeanLocal {

    public Integer getNumOfFollowersMonthly(Long authorId, Integer monthValue, Integer currentYear);

    public Integer getNumOfFollowersByAuthorId(Long authorId);
}
