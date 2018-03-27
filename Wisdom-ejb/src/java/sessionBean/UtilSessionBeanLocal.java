/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.ArticleEntity;
import entity.QuestionEntity;
import entity.ReaderEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chuck
 */
@Local
public interface UtilSessionBeanLocal {

    ReaderEntity createReader(ReaderEntity reader);

    boolean readerHasEmailConflict(String email);

    boolean authorHasEmailConflict(String email);

    ReaderEntity setInterestedTopics(ArrayList<String> topics, Long readerId);

    ArticleEntity getArticleById(Long articleId);

    List<QuestionEntity> getQuestionsByReader(Long readerId);

    QuestionEntity getQuestionById(Long questionId);
    
    List<ArticleEntity> getNewestArticlesOfFollowedAuthors(Long readerId);

    List<ArticleEntity> getMostLikedArticlesOfTopic(final String topic);
}
