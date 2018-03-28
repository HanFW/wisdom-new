/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.ReaderEntity;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
            = Logger.getLogger(UtilSessionBean.class.getName());
    private static ConsoleHandler handler = null;

    public ArticleSessionBean() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        LOGGER.setLevel(Level.FINEST);
        LOGGER.addHandler(handler);
    }

    @Override
    public Long addNewArticle(String topic, String title, String description,
            String context, LocalDateTime created, Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        ArticleEntity article = new ArticleEntity(topic, title, description, context, created);
        article.setAuthor(author);

        entityManager.persist(article);
        entityManager.flush();

        return article.getId();
    }

    @Override
    public List<ArticleEntity> getArticlesByAuthorId(Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        if (author.getId() == null) {
            return new ArrayList<ArticleEntity>();
        }
        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.author=:author");
            query.setParameter("author", author);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            return new ArrayList<ArticleEntity>();
        }
    }

    @Override
    public ArticleEntity getArticleById(Long articleId) {
        if (articleId == null) {
            return null;
        }

        ArticleEntity article = entityManager.find(ArticleEntity.class, articleId);
        if (article == null) {
            throw new EntityNotFoundException();
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
    public List<ArticleEntity> getNewestArticlesOfFollowedAuthors(Long readerId) {
        if (readerId == null) {
            return null;
        }

        ReaderEntity reader = entityManager.find(ReaderEntity.class, readerId);
        if (reader == null) { // reader id not found
            throw new EntityNotFoundException();
        }

        Query q = entityManager.createQuery("select a from ArticleEntity a, FollowEntity f "
                + "where f.reader.id = :readerId "
                + "and f.author.id = a.author.id "
                + "order by a.created DESC") // TODO: newest articles to oldest
                .setParameter("readerId", readerId);
        List<ArticleEntity> articles = null;
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
        List<ArticleEntity> articles = null;
        try {
            articles = q.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return articles;
    }
}
