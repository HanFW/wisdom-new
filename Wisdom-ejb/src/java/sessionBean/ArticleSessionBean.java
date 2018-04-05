/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.ReaderEntity;
import entity.RewardEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import exception.RepeatActionException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utility.Constants;

/**
 *
 * @author Yongxue
 */
@Stateless
public class ArticleSessionBean implements ArticleSessionBeanLocal {

    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER
            = Logger.getLogger(ArticleSessionBean.class.getName());
    private static ConsoleHandler handler = null;

    public ArticleSessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        LOGGER.setLevel(Level.FINEST);
        LOGGER.addHandler(handler);
    }

    @Override
    public Long addNewArticle(String topic, String title, String description,
            String context, LocalDateTime created, String picPath, Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        ArticleEntity article = new ArticleEntity(topic, title, description,
                context, created, picPath);
        article.setAuthor(author);

        entityManager.persist(article);
        entityManager.flush();

        return article.getId();
    }

    @Override
    public List<ArticleEntity> getArticlesByAuthorId(Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        if (author.getId() == null) {
            return null;
        }
        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.author=:author");
            query.setParameter("author", author);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return null;
        }
    }

    @Override
    public ArticleEntity getArticleById(Long articleId)
            throws NoSuchEntityException {
        if (articleId == null) {
            return null;
        }

        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (article == null) {
            LOGGER.log(Level.FINEST, "0. article w ID: {0} not found.", articleId);
            throw new NoSuchEntityException("article " + articleId + " not found.");
        }

        return article;
    }

    /**
     *
     * @param readerId
     * @return list of articles created by authors followed by the reader in
     * DESC order (newest articles to oldest)
     */
    @Override
    public List<ArticleEntity> getNewestArticlesOfFollowedAuthors(Long readerId)
            throws NoSuchEntityException {
        if (readerId == null) {
            return null;
        }

        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new NoSuchEntityException("reader " + readerId + " not found");
        }

        Query q = entityManager.createQuery("select a from ArticleEntity a, FollowEntity f "
                + "where f.reader.id = :readerId "
                + "and f.author.id = a.author.id "
                + "order by a.created DESC") // TODO: newest articles to oldest
                .setParameter("readerId", readerId);
        List<ArticleEntity> articles = new ArrayList<>();
        try {
            articles = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return articles;
    }

    /**
     *
     * @param topic
     * @return list of articles of a certain topic, created in the past 3 days,
     * from most upvotes to fewest
     */
    @Override
    public List<ArticleEntity> getMostLikedArticlesOfTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            return null;
        }

        Query q = entityManager.createQuery("select a from ArticleEntity a "
                + "where a.topic = :topic "
                + "and a.created > :threeDaysAgo "
                + "order by a.numOfUpvotes DESC") // most upvotes to fewest
                .setParameter("topic", topic)
                .setParameter("threeDaysAgo", LocalDateTime.now().minusDays(3));
        List<ArticleEntity> articles = new ArrayList<>();
        try {
            articles = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return articles;
    }

    @Override
    public ArticleEntity likeArticle(Long articleId) {
        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (article == null) {
            return null;
        }

        article.setNumOfUpvotes(article.getNumOfUpvotes() + 1);
        return article;
    }

    @Override
    public ReaderEntity saveArticle(Long readerId, Long articleId) throws RepeatActionException {
        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (reader == null || article == null) {
            return null;
        }

        if (reader.getSaved().contains(article)) {
            throw new RepeatActionException("Error! Article is already saved by this reader");
        }

        reader.getSaved().add(article);
        entityManager.merge(reader);

        return reader;
    }

    @Override
    public List<ArticleEntity> getAllSavedArticles(Long readerId) {
        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        if (reader == null) {
            return null;
        }
        return reader.getSaved();
    }

    @Override
    public ReaderEntity unsaveArticle(Long readerId, Long articleId) throws RepeatActionException {
        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (reader == null || article == null) {
            return null;
        }

        if (!reader.getSaved().contains(article)) {
            throw new RepeatActionException("Error! Article has not been saved by this reader");
        }

        for (int i = 0; i < reader.getSaved().size(); i++) {
            if (reader.getSaved().get(i).equals(article)) {
                reader.getSaved().remove(i);
            }
        }
        entityManager.merge(reader);

        return reader;
    }

    @Override
    public Boolean checkArticleSaved(Long readerId, Long articleId) {
        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (reader == null || article == null) {
            return null;
        }

        if (reader.getSaved().contains(article)) {
            return true;
        }
        return false;
    }

    @Override
    public ReaderEntity tipArticle(Long readerId, Long articleId, Double amount) throws InsufficientBalanceException {
        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (reader == null || article == null) {
            return null;
        }

        if (amount > reader.getBalance()) {
            throw new InsufficientBalanceException("Insufficient Balance!");
        }
        reader.setBalance(reader.getBalance() - amount);
        AuthorEntity author = article.getAuthor();
        author.setBalance(author.getBalance() + amount);

        //record the transaction
        RewardEntity reward = new RewardEntity(amount);
        reward.setTransactionType(Constants.TRANSACTION_REWARD);

        reward.setArticle(article);
        article.getRewards().add(reward);
        reward.setFrom(reader);
        reward.setTo(author);

        Double rewardIncome = article.getRewardIncomePerArticle();
        Integer numOfRewards = article.getNumOfRewards();

        Double newRewardIncome = rewardIncome + amount;
        Integer newNumOfRewards = numOfRewards + 1;

        article.setRewardIncomePerArticle(newRewardIncome);
        article.setNumOfRewards(newNumOfRewards);

        entityManager.persist(reward);
        entityManager.merge(reader);
        entityManager.merge(author);
        entityManager.merge(article);
        entityManager.flush();
        return reader;
    }

    @Override
    public List<ArticleEntity> getArticlesByTopic(String topic) {

        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.topic=:topic");
            query.setParameter("topic", topic);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<ArticleEntity>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<ArticleEntity>();
        }
    }

    @Override
    public String checkDuplicateArticle(String title, Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.author=:author And a.title=:title");
            query.setParameter("author", author);
            query.setParameter("title", title);

            if (query.getResultList().isEmpty()) {
                return "Unique";
            } else {
                return "Duplicate";
            }
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
            return "Multiple Entries";
        }
    }

    @Override
    public List<ArticleEntity> getArticlesByAuthorIdMonthly(Long authorId, Integer monthValue) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        if (author.getId() == null) {
            return null;
        }
        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.author=:author And a.createdMonthValue=:monthValue");
            query.setParameter("author", author);
            query.setParameter("monthValue", monthValue);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<ArticleEntity>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return null;
        }
    }
}
