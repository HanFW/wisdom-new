/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loginManagedBean")
@RequestScoped
public class LoginManagedBean {
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    private String email;
    private String password;

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
    }
    
    public void doLogin(ActionEvent event) throws IOException {
        AuthorEntity author = authorSessionBeanLocal.authorLogin(email, password);
        if(author == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email/password", " "));
        } else {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("authorId", author.getId());
            System.out.println("Author " + author.getId() + " - " + author.getName() + " logged in.");
            ec.redirect(ec.getRequestContextPath() + "/web/overview.xhtml?faces-redirect=true");
        }
    }
    
    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
