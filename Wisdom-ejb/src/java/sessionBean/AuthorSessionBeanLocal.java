/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import javax.ejb.Local;

/**
 *
 * @author Yongxue
 */
@Local
public interface AuthorSessionBeanLocal {
    public AuthorEntity retrieveAuthorById(Long authorId);
    public Long createNewAuthor(String username, String description, String email, String password);
    public AuthorEntity authorLogin(String email, String password);
}
