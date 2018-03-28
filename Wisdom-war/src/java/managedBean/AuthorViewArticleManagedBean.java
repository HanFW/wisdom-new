/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import sessionBean.ArticleSessionBeanLocal;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewArticleManagedBean")
@SessionScoped
public class AuthorViewArticleManagedBean implements Serializable {

    /**
     * Creates a new instance of AuthorViewArticleManagedBean
     */
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    private String articleTitle;
    private String articleTopic;
    private String questionPrice;
    private LocalDateTime created;
    private String articleDescription;
    private String articleContent;
    private Long articleId;

    private ArticleEntity article;
    private AuthorEntity author;

    public AuthorViewArticleManagedBean() {

    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTopic() {
        return articleTopic;
    }

    public void setArticleTopic(String articleTopic) {
        this.articleTopic = articleTopic;
    }

    public String getQuestionPrice() {
        return questionPrice;
    }

    public void setQuestionPrice(String questionPrice) {
        this.questionPrice = questionPrice;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public Long getArticleId() {
        System.out.println("get article id: " + articleId);
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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
