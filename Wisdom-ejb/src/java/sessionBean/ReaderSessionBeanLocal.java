/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.ReaderEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sherry
 */
@Local
public interface ReaderSessionBeanLocal {
    
    
    ReaderEntity readerSignUp(String name, String email, String pwd);

    ReaderEntity followAuthor(Long authorId, Long readerId) throws Exception;

    List<AuthorEntity> getAllFollowedAuthors(Long readerId);

    ReaderEntity topUpWallet(Long readerId, Double amount);

    ArticleEntity likeArticle(Long articleId);

    ReaderEntity saveArticle(Long readerId, Long articleId) throws Exception;

    List<ArticleEntity> getAllSavedArticles(Long readerId);

    
}
