/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import sessionBean.ArticleSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewAllArticlesManagedBean")
@ViewScoped
public class AuthorViewAllArticlesManagedBean implements Serializable {

    /**
     * Creates a new instance of MyArticlesAuthorManagedBean
     */
    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    List<ArticleEntity> article;

    private Long articleId;

    private ExternalContext ec;

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        article = articleSessionBeanLocal.getArticlesByAuthorId(Long.valueOf(ec.getSessionMap().get("authorId").toString()));
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public List<ArticleEntity> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleEntity> article) {
        this.article = article;
    }

    public AuthorViewAllArticlesManagedBean() {

    }

    public void viewArticle() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        ec.getFlash().put("articleId", articleId);

        ec.redirect(ec.getRequestContextPath() + "/web/authorViewArticle.xhtml?faces-redirect=true");
    }
}
