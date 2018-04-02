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
import javax.ejb.EJB;
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
    @EJB(name = "FollowSessionBeanLocal")
    private FollowSessionBeanLocal followSessionBeanLocal;

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
    public Long addNewFollowerAnalytics(Integer currentYear) {
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

    @Override
    public void updateFollowersByMonth(Integer monthValue, Long followerAnalyticsId, Long authorId) {

        FollowerAnalyticsEntity followerAnalytics = new FollowerAnalyticsEntity();
        Integer numOfFollowers = 0;

        try {
            followerAnalytics = getFollowAnalyticsById(followerAnalyticsId);
        } catch (NoSuchEntityException e) {
            // TODO:
        }

        numOfFollowers = followSessionBeanLocal.getNumOfFollowers(authorId, monthValue);
        System.out.println("numOfFollowers " + numOfFollowers);

        switch (monthValue) {
            case 1:
                followerAnalytics.setJan(numOfFollowers);
                break;
            case 2:
                followerAnalytics.setFeb(numOfFollowers);
                break;
            case 3:
                followerAnalytics.setMar(numOfFollowers);
                break;
            case 4:
                followerAnalytics.setApr(numOfFollowers);
                break;
            case 5:
                followerAnalytics.setMay(numOfFollowers);
                break;
            case 6:
                followerAnalytics.setJun(numOfFollowers);
                break;
            case 7:
                followerAnalytics.setJul(numOfFollowers);
                break;
            case 8:
                followerAnalytics.setAug(numOfFollowers);
                break;
            case 9:
                followerAnalytics.setSep(numOfFollowers);
                break;
            case 10:
                followerAnalytics.setOct(numOfFollowers);
                break;
            case 11:
                followerAnalytics.setNov(numOfFollowers);
                break;
            case 12:
                followerAnalytics.setDecember(numOfFollowers);
                break;
        }

        entityManager.flush();
    }
}
