/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import entity.QuestionEntity;
import exception.NoSuchEntityException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import sessionBean.AuthorSessionBeanLocal;
import sessionBean.QuestionSessionBeanLocal;
import utility.Constants;

/**
 *
 * @author hanfengwei
 */
@Named(value = "authorViewAllQuestionsManagedBean")
@ViewScoped
public class AuthorViewAllQuestionsManagedBean implements Serializable {
    
    @EJB
    private QuestionSessionBeanLocal questionSessionBeanLocal;
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    private Double questionPrice = 0.0;
    private Long authorId = null;
    private QuestionEntity question;
    private String reply;
    
    /**
     * Creates a new instance of AuthorViewAllQuestionsManagedBean
     */
    public AuthorViewAllQuestionsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        authorId = (Long) ec.getSessionMap().get("authorId");
        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        if(author != null){
            questionPrice = author.getQtnPrice();
        }
    }
    
    public void validateReply (ActionEvent event) {
        if(reply == null || reply.equals("")) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().addMessage("questionReply", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter your reply", " "));
        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('replyConfirmation').show();");
        }
    }
    
    public void updateQuestionPrice (ActionEvent event) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        authorId = (Long) ec.getSessionMap().get("authorId");
        questionSessionBeanLocal.updateQuestionPrice(authorId, questionPrice);
    }
    
    public void rejectQuestion (ActionEvent event) {
        reply = "";
        questionSessionBeanLocal.rejectQuestion(question.getId());
    }
    
    public void replyToQuestion (ActionEvent event) {
        questionSessionBeanLocal.replyToQuestion(question.getId(), reply);
        reply = "";
    }
    
    public List<QuestionEntity> getPendingQuestions() {
        try {
            return questionSessionBeanLocal.getQuestionsByAuthorId(authorId, Constants.STATUS_PENDING);
        } catch (NoSuchEntityException ex) {
            Logger.getLogger(AuthorViewAllQuestionsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<QuestionEntity> getAnsweredQuestions() {
        try {
            return questionSessionBeanLocal.getQuestionsByAuthorId(authorId, Constants.STATUS_ANSWERED);
        } catch (NoSuchEntityException ex) {
            Logger.getLogger(AuthorViewAllQuestionsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<QuestionEntity> getExpiredQuestions() {
        try {
            return questionSessionBeanLocal.getQuestionsByAuthorId(authorId, Constants.STATUS_EXPIRED);
        } catch (NoSuchEntityException ex) {
            Logger.getLogger(AuthorViewAllQuestionsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<QuestionEntity> getRejectedQuestions() {
        try {
            return questionSessionBeanLocal.getQuestionsByAuthorId(authorId, Constants.STATUS_REJECTED);
        } catch (NoSuchEntityException ex) {
            Logger.getLogger(AuthorViewAllQuestionsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Double getQuestionPrice() {
        return questionPrice;
    }

    public void setQuestionPrice(Double questionPrice) {
        this.questionPrice = questionPrice;
    }
}
