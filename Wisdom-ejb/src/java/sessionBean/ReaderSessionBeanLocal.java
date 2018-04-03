/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import entity.AuthorEntity;
import entity.ReaderEntity;
import exception.DuplicateEntityException;
import exception.NoSuchEntityException;
import exception.RepeatActionException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sherry
 */
@Local
public interface ReaderSessionBeanLocal {
    

    ReaderEntity followAuthor(Long authorId, Long readerId) throws NoSuchEntityException, RepeatActionException;

    List<AuthorEntity> getAllFollowingAuthors(Long readerId);

    ReaderEntity topUpWallet(Long readerId, Double amount);

    ReaderEntity createReader(ReaderEntity reader) throws DuplicateEntityException;
    
    ReaderEntity authenticateReader(String email, String pwd) throws NoSuchEntityException;

    boolean readerHasEmailConflict(String email);

    ReaderEntity setInterestedTopics(ArrayList<String> topics, Long readerId) throws NoSuchEntityException;

    ReaderEntity unfollowAuthor(Long authorId, Long readerId)throws NoSuchEntityException, RepeatActionException;

    Boolean checkFollow(Long authorId, Long readerId) throws NoSuchEntityException;

    
}
