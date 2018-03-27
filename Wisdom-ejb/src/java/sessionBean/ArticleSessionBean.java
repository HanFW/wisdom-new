/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public Long addNewArticle(String topic, String title, String description,
            String context, Long authorId) {

        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        ArticleEntity article = new ArticleEntity(topic, title, description, context);
        article.setAuthor(author);
        
        entityManager.persist(article);
        entityManager.flush();

        return article.getId();
    }

    @Override
    public List<ArticleEntity> retrieveArticlesByAuthorId(Long authorId) {
        
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        if (author.getId()== null) {
            // TODO: author not found throw exception instead? 
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
}
