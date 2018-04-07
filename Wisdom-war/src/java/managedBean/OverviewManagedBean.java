/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import sessionBean.AuthorSessionBeanLocal;
import sessionBean.FollowSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "overviewManagedBean")
@RequestScoped
public class OverviewManagedBean {

    /**
     * Creates a new instance of OverviewManagedBean
     */
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    @EJB(name = "FollowSessionBeanLocal")
    private FollowSessionBeanLocal followSessionBeanLocal;

    private String profileImagePath;
    private String username;
    private String introduction;
    private Double balance;
    private Integer numOfFollowers;
    private Double todayIncome;
    private Double todayRewardIncome;
    private Double todayQuestionIncome;
    private Integer todayNewFollowers;

    private Long authorId;
    private AuthorEntity author;
    private String filename;
    private LocalDateTime currentTime;

    private ExternalContext ec;

    public OverviewManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());
        author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        currentTime = LocalDateTime.now();

        filename = "author_" + author.getPicPath() + ".png";
        profileImagePath = "http://localhost:8080/" + filename;
        username = author.getName();
        introduction = author.getDescription();
        balance = author.getBalance();
        numOfFollowers = followSessionBeanLocal.getNumOfFollowersByAuthorId(authorId);
        todayRewardIncome = author.getTodayRewardIncome();
        todayQuestionIncome = author.getTodayQuestionIncome();
        todayIncome = todayRewardIncome + todayQuestionIncome;
        todayNewFollowers = followSessionBeanLocal.getNumOfFollowersDaily(authorId,
                currentTime.getDayOfYear(), currentTime.getYear());
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(Integer numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public Double getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(Double todayIncome) {
        this.todayIncome = todayIncome;
    }

    public Double getTodayRewardIncome() {
        return todayRewardIncome;
    }

    public void setTodayRewardIncome(Double todayRewardIncome) {
        this.todayRewardIncome = todayRewardIncome;
    }

    public Double getTodayQuestionIncome() {
        return todayQuestionIncome;
    }

    public void setTodayQuestionIncome(Double todayQuestionIncome) {
        this.todayQuestionIncome = todayQuestionIncome;
    }

    public Integer getTodayNewFollowers() {
        return todayNewFollowers;
    }

    public void setTodayNewFollowers(Integer todayNewFollowers) {
        this.todayNewFollowers = todayNewFollowers;
    }
}
