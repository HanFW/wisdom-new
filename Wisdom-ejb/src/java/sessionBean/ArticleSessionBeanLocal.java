/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import exception.RepeatActionException;
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

    public List<ArticleEntity> getArticlesByAuthorId(Long authorId);

    public ArticleEntity getArticleById(Long articleId) throws NoSuchEntityException;

    public List<ArticleEntity> getNewestArticlesOfFollowedAuthors(Long readerId) throws NoSuchEntityException;

    public List<ArticleEntity> getMostLikedArticlesOfTopic(final String topic);

    public List<ArticleEntity> getAllSavedArticles(Long readerId);

    public ReaderEntity saveArticle(Long readerId, Long articleId) throws RepeatActionException;

    public ArticleEntity likeArticle(Long articleId);

    public ReaderEntity unsaveArticle(Long readerId, Long articleId) throws RepeatActionException;

    public Boolean checkArticleSaved(Long readerId, Long articleId);

    public ReaderEntity tipArticle(Long readerId, Long articleId, Double amount) throws InsufficientBalanceException;

    public List<ArticleEntity> getArticlesByTopic(String topic);

}
