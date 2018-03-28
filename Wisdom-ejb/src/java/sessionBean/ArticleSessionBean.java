/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
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
    public List<ArticleEntity> retrieveArticlesByAuthorId(Long authorId) {

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
    public ArticleEntity retrieveArticleById(Long articleId) {
        ArticleEntity article = new ArticleEntity();

        try {
            Query query = entityManager.createQuery("Select a From ArticleEntity a Where a.id=:articleId");
            query.setParameter("articleId", articleId);

            if (query.getResultList().isEmpty()) {
                return new ArticleEntity();
            } else {
                article = (ArticleEntity) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArticleEntity();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return article;
    }
}
