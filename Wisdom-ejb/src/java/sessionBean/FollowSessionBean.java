/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yongxue
 */
@Stateless
public class FollowSessionBean implements FollowSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer getNumOfFollowersMonthly(Long authorId, Integer monthValue, Integer currentYear) {

        Query query = entityManager.createNamedQuery("FollowEntity.findFollowersByAuthor_PerMonth")
                .setParameter("authorId", authorId)
                .setParameter("monthValue", monthValue)
                .setParameter("currentYear", currentYear);

        if (query.getResultList().isEmpty()) {
            return 0;
        } else {
            return query.getResultList().size();
        }
    }

    @Override
    public Integer getNumOfFollowersByAuthorId(Long authorId) {

        Query query = entityManager.createNamedQuery("FollowEntity.findFollowersByAuthor")
                .setParameter("authorId", authorId);

        if (query.getResultList().isEmpty()) {
            return 0;
        } else {
            return query.getResultList().size();
        }
    }

    @Override
    public Integer getNumOfFollowersDaily(Long authorId, Integer dayValue, Integer currentYear) {

        Query query = entityManager.createNamedQuery("FollowEntity.findFollowersByAuthor_PerDay")
                .setParameter("authorId", authorId)
                .setParameter("dayValue", dayValue)
                .setParameter("currentYear", currentYear);

        if (query.getResultList().isEmpty()) {
            return 0;
        } else {
            return query.getResultList().size();
        }
    }
}
