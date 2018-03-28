/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface ArticleSessionBeanLocal {

    public Long addNewArticle(String topic, String title, String description,
            String context, LocalDateTime created, Long authorId);

    public List<ArticleEntity> retrieveArticlesByAuthorId(Long authorId);

    public ArticleEntity retrieveArticleById(Long articleId);

}
