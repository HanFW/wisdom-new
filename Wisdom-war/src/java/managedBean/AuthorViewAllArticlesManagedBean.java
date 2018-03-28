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
import javax.inject.Named;
import sessionBean.ArticleSessionBeanLocal;

/**
 *
 * @author Yongxue
 */

@Named(value = "myArticlesAuthorManagedBean")
@RequestScoped
public class AuthorViewAllArticlesManagedBean {

    /**
     * Creates a new instance of MyArticlesAuthorManagedBean
     */
    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    public AuthorViewAllArticlesManagedBean() {
    }

    public List<ArticleEntity> getArticle() throws IOException {
        
        List<ArticleEntity> article = articleSessionBeanLocal.getArticlesByAuthorId(Long.valueOf(1));

        return article;
    }
}
