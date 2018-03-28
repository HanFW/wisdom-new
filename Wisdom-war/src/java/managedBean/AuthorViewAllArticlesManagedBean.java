/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import sessionBean.ArticleSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewAllArticlesManagedBean")
@RequestScoped
public class AuthorViewAllArticlesManagedBean {

    /**
     * Creates a new instance of MyArticlesAuthorManagedBean
     */
    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    private Long articleId;

    private ExternalContext ec;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public AuthorViewAllArticlesManagedBean() {
    }

    public List<ArticleEntity> getArticle() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        List<ArticleEntity> article = articleSessionBeanLocal.getArticlesByAuthorId(Long.valueOf(ec.getSessionMap().get("authorId").toString()));

        return article;
    }

    public void viewArticle() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        ec.getFlash().put("articleId", articleId);

        ec.redirect(ec.getRequestContextPath() + "/web/authorViewArticle.xhtml?faces-redirect=true");
    }
}
