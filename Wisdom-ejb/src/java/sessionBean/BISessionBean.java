/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.FollowerAnalyticsEntity;
import exception.NoSuchEntityException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yongxue
 */
@Stateless
public class BISessionBean implements BISessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger LOGGER = Logger.getLogger(ReaderSessionBean.class.getName()); // used to output info
    private static ConsoleHandler handler = null; // set logger's output to console

    @PersistenceContext
    private EntityManager entityManager;

    public BISessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
    }

    @Override
    public Long addNewFollowerAnalytics(String currentYear) {
        FollowerAnalyticsEntity followerAnalytics = new FollowerAnalyticsEntity(currentYear);

        entityManager.persist(followerAnalytics);
        entityManager.flush();

        return followerAnalytics.getId();
    }

    @Override
    public FollowerAnalyticsEntity getFollowAnalyticsById(Long followAnalyticsId) throws NoSuchEntityException {

        if (followAnalyticsId == null) {
            return null;
        }

        FollowerAnalyticsEntity followerAnalytics = entityManager.find(FollowerAnalyticsEntity.class, followAnalyticsId);
        if (followerAnalytics == null) {
            LOGGER.log(Level.FINEST, "0. followerAnalytics w ID: {0} not found.", followAnalyticsId);
            throw new NoSuchEntityException("followerAnalytics " + followAnalyticsId + " not found.");
        }

        return followerAnalytics;
    }
}
