/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.QuestionEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sherry
 */
@Local
public interface QuestionSessionBeanLocal {

    QuestionEntity getQuestionById(Long questionId);

    List<QuestionEntity> getQuestionsByReader(Long readerId);
    
}