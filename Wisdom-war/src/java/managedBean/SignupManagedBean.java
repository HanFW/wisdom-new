/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
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
    
    private String firstName;
    private String lastName;
    private String email;
    private String selfIntroduction;
    private String password;
    private UploadedFile profileImage;
    private Long authorId;

    /**
     * Creates a new instance of SignupManagedBean
     */
    public SignupManagedBean() {
    }
    
    public void profileImageUpload (FileUploadEvent event) throws FileNotFoundException, IOException {
        this.profileImage = event.getFile();
        System.out.println("### upload image: " + profileImage.getContentType() + " " + profileImage.getFileName());
    }
    
    public void signup(ActionEvent event) {
        authorId = authorSessionBeanLocal.createNewAuthor(firstName + " " + lastName, selfIntroduction, email, password);
        if (authorId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have registered already, please login directly.", " "));
        } else {
            System.out.println("### sign up: author ID " + authorId);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("authorId", authorId);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('redirectDialog').show();");
        }
    }
    
    public void directLogin(ActionEvent event) throws IOException {
        System.out.println("Author " + authorId + " - " + firstName + " " + lastName + " logged in.");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/overview.xhtml?faces-redirect=true");
    }
    
    public void backToHomepage (ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().remove("authodId");
        ec.redirect(ec.getRequestContextPath() + "/web/index.xhtml?faces-redirect=true");
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

    public UploadedFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(UploadedFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
}
