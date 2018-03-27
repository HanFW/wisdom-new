/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "signupManagedBean")
@RequestScoped
public class SignupManagedBean {
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    private String username;
    private String email;
    private String selfIntroduction;
    private String password;
    private String confirmPassword;
    private UploadedFile profileImage;
    private boolean invalidUsername = true;

    /**
     * Creates a new instance of SignupManagedBean
     */
    public SignupManagedBean() {
    }
    
    public void validateUsername () {
        System.out.println("### validate username: " + username);
    }
    
    public void profileImageUpload (FileUploadEvent event) throws FileNotFoundException, IOException {
        this.profileImage = event.getFile();
        System.out.println("### upload image: " + profileImage.getContentType() + " " + profileImage.getFileName());
    }
    
    public void signup(ActionEvent event) {
        Long authorId = authorSessionBeanLocal.createNewAuthor(username, selfIntroduction, email, password);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().put("authorId", authorId);

        System.out.println("### sign up: author ID " + authorId + " saved to session map");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UploadedFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(UploadedFile profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isInvalidUsername() {
        return invalidUsername;
    }

    public void setInvalidUsername(boolean invalidUsername) {
        this.invalidUsername = invalidUsername;
    }
    
}
