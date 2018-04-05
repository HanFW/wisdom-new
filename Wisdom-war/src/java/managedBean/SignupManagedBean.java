/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import exception.DuplicateEntityException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named (value = "signupManagedBean")
@ViewScoped
public class SignupManagedBean implements Serializable {
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    private String firstName;
    private String lastName;
    private String email;
    private String selfIntroduction;
    private String password;
    private UploadedFile profileImage;
    private Long authorId;
    private String picPath;

    /**
     * Creates a new instance of SignupManagedBean
     */
    public SignupManagedBean() {
    }
    
    public void profileImageUpload (FileUploadEvent event) throws FileNotFoundException, IOException {
        this.profileImage = event.getFile();
        
        picPath = UUID.randomUUID().toString();

        if (profileImage != null) {
            String filename = "author_" + picPath + ".png";

            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");
            OutputStream output = new FileOutputStream(new File(newFilePath, filename));

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = profileImage.getInputstream();

            while (true) {
                a = inputStream.read(buffer);
                if (a < 0) {
                    break;
                }
                output.write(buffer, 0, a);
                output.flush();
            }

            output.close();
            inputStream.close();
            FacesContext.getCurrentInstance().addMessage("profileImage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile image uploaded successful!", " "));
        }
    }
    
    public void signup(ActionEvent event) {
        try {
            if (profileImage == null || picPath == null) {
                FacesContext.getCurrentInstance().addMessage("profileImage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload your profile image", " "));
            } else {
                authorId = authorSessionBeanLocal.createNewAuthor(firstName + " " + lastName, selfIntroduction, email, password, picPath);
                if (authorId == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter all the fields correctly", " "));
                } else {
                    System.out.println("### sign up: author ID " + authorId);
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('redirectDialog').show();");
                }
            }
        } catch (DuplicateEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "This email has been registered already, please change your email address or login directly.", " "));
        }
    }
    
    public void directLogin(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        ec.getSessionMap().put("authorId", authorId);
        System.out.println("Author " + author.getId() + " - " + author.getName() + " logged in.");
        ec.redirect(ec.getRequestContextPath() + "/web/overview.xhtml?faces-redirect=true");
    }
    
    public void backToHomepage (ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
