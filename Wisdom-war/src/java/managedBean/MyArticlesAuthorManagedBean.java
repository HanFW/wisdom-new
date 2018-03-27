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
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import sessionBean.ArticleSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "myArticlesAuthorManagedBean")
@RequestScoped
public class MyArticlesAuthorManagedBean {
    
    /**
     * Creates a new instance of MyArticlesAuthorManagedBean
     */
    
    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;
    
    public MyArticlesAuthorManagedBean() {
    }
    
    public List<ArticleEntity> getArticle() throws IOException {
        Long authorId = Long.valueOf(0);
        List<ArticleEntity> article = articleSessionBeanLocal.retrieveArticlesByAuthorId(authorId);
        
        return article;
    }
    
}
