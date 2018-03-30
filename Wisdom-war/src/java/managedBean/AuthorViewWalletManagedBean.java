/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.TransactionEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import sessionBean.AuthorSessionBeanLocal;
import sessionBean.TransactionSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewWalletManagedBean")
@RequestScoped
public class AuthorViewWalletManagedBean {

    /**
     * Creates a new instance of AuthorViewWalletManagedBean
     */
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @EJB(name = "TransactionSessionBeanLocal")
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    private Double authorBalance;
    private Long authorId;
    private AuthorEntity author;

    private ExternalContext ec;

    public AuthorViewWalletManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());
        author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        authorBalance = author.getBalance();
    }

    public Double getAuthorBalance() {
        return author.getBalance();
    }

    public void setAuthorBalance(Double authorBalance) {
        this.authorBalance = authorBalance;
    }

    public List<TransactionEntity> getTransaction() throws IOException {
        List<TransactionEntity> transaction = transactionSessionBeanLocal.getTransactionByAuthorId(authorId);

        return transaction;
    }
}
