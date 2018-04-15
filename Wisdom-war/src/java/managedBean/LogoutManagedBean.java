/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author hanfengwei
 */
@Named(value = "logoutManagedBean")
@RequestScoped
public class LogoutManagedBean {

    /**
     * Creates a new instance of LogoutManagedBean
     */
    public LogoutManagedBean() {
    }
    
    public void doLogout(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("Author " + ec.getSessionMap().get("authorId") + " logged out.");
        ec.getSessionMap().remove("authorId");
        ec.redirect(ec.getRequestContextPath() + "/web/index.xhtml?faces-redirect=true");
    }
}
