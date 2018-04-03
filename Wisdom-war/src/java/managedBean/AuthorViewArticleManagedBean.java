/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import exception.NoSuchEntityException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import sessionBean.ArticleSessionBeanLocal;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewArticleManagedBean")
@RequestScoped
public class AuthorViewArticleManagedBean implements Serializable {

    /**
     * Creates a new instance of AuthorViewArticleManagedBean
     */
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    private Long articleId;
    private Long authorId;
    private String picPath;
    private String filename;

    private ArticleEntity article;
    private AuthorEntity author;

    private ExternalContext ec;

    public AuthorViewArticleManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        articleId = Long.valueOf(ec.getFlash().get("articleId").toString());
        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());

        try {
            article = articleSessionBeanLocal.getArticleById(articleId);
        } catch (NoSuchEntityException e) {
            // TODO:
        }
        author = authorSessionBeanLocal.retrieveAuthorById(authorId);

        filename = "author_" + authorId + ".png";
        picPath = "http://localhost:8080/" + filename;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }
}
