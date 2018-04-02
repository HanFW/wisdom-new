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
    public Integer getNumOfFollowers(Long authorId, Integer monthValue) {

        Query query = entityManager.createNamedQuery("FollowEntity.findFollowersByAuthor_PerMonth")
                .setParameter("authorId", authorId)
                .setParameter("monthValue", monthValue);
        
        if (query.getResultList().isEmpty()) {
            return 0;
        } else {
            return query.getResultList().size();
        }
    }
}
