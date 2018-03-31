/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.QuestionEntity;
import exception.NoSuchEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import sessionBean.AuthorSessionBeanLocal;
import sessionBean.QuestionSessionBeanLocal;
import utility.Constants;

/**
 *
 * @author hanfengwei
 */
@Named(value = "authorViewAllQuestionsManagedBean")
@RequestScoped
public class AuthorViewAllQuestionsManagedBean {
    
    @EJB
    private QuestionSessionBeanLocal questionSessionBeanLocal;
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    
    private Double questionPrice = 0.0;
    private Long authorId = null;
    private QuestionEntity question;
    private String reply;
    private Long questionId;
    
    /**
     * Creates a new instance of AuthorViewAllQuestionsManagedBean
     */
    public AuthorViewAllQuestionsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        authorId = (Long) ec.getSessionMap().get("authorId");
        questionPrice = authorSessionBeanLocal.retrieveAuthorById(authorId).getQtnPrice();
    }
    
    public void updateQuestionPrice (ActionEvent event) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        authorId = (Long) ec.getSessionMap().get("authorId");
        questionSessionBeanLocal.updateQuestionPrice(authorId, questionPrice);
    }
    
    public void rejectQuestion () {
        reply = "";
        questionId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionId"));
        questionSessionBeanLocal.rejectQuestion(questionId);
    }
    
    public void replyToQuestion () {
        questionId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionId"));
        questionSessionBeanLocal.replyToQuestion(questionId, reply);
        reply = "";
    }
    
    public List<QuestionEntity> getPendingQuestions() {
        try {
            return questionSessionBeanLocal.getQuestionsByAuthorId(Long.parseLong("5"), Constants.STATUS_PENDING);
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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        System.out.println("set question id " + questionId);
        this.questionId = questionId;
    }

    public Double getQuestionPrice() {
        return questionPrice;
    }

    public void setQuestionPrice(Double questionPrice) {
        this.questionPrice = questionPrice;
    }
}
