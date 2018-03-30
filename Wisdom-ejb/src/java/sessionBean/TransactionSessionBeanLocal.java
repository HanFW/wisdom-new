/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface TransactionSessionBeanLocal {

    public List<TransactionEntity> getTransactionByAuthorId(Long authorId);
}
