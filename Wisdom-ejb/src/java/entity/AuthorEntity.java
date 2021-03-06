/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yongxue
 */
@NamedQueries({
    @NamedQuery(name = "AuthorEntity.findFollowedAuthorsByReader",
            query = "SELECT a FROM FollowEntity f, AuthorEntity a "
            + "WHERE f.author.id = a.id "
            + "AND f.reader.id = :readerId " + "AND f.status != 'DELETED'")
})
@Entity
@XmlRootElement // @XmlRootElement is for Jersey to serialize entity to JSON
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // format {firstName LastName}
    private String description; // short intro text
    private String picPath;
    private String email; // no setter
    @XmlTransient
    private String pwd;
    private Double balance; // received credit
    private Double qtnPrice;  // author-defined question price (default to 5)
    private LocalDateTime created;
    private Double todayQuestionIncome;
    private Double todayRewardIncome;
    private Integer today;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private FollowerAnalyticsEntity followerAnalytics;

    public AuthorEntity() {
        this.picPath = null; // default to no pic
        this.balance = 0.0;
        this.qtnPrice = 5.0;
        this.todayQuestionIncome = 0.0;
        this.todayRewardIncome = 0.0;
        this.today = 0;
    }

    public AuthorEntity(String name, String description, String email, String pwd, String picPath) {
        this();
        this.name = name;
        this.description = description;
        this.email = email;
        this.pwd = pwd;
        this.created = LocalDateTime.now();
        this.picPath = picPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getQtnPrice() {
        return qtnPrice;
    }

    public void setQtnPrice(Double qtnPrice) {
        this.qtnPrice = qtnPrice;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Double getTodayQuestionIncome() {
        return todayQuestionIncome;
    }

    public void setTodayQuestionIncome(Double todayQuestionIncome) {
        this.todayQuestionIncome = todayQuestionIncome;
    }

    public Double getTodayRewardIncome() {
        return todayRewardIncome;
    }

    public void setTodayRewardIncome(Double todayRewardIncome) {
        this.todayRewardIncome = todayRewardIncome;
    }

    public Integer getToday() {
        return today;
    }

    public void setToday(Integer today) {
        this.today = today;
    }

    public FollowerAnalyticsEntity getFollowerAnalytics() {
        return followerAnalytics;
    }

    public void setFollowerAnalytics(FollowerAnalyticsEntity followerAnalytics) {
        this.followerAnalytics = followerAnalytics;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthorEntity other = (AuthorEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuthorEntity{" + "id=" + id + ", name=" + name + ", description=" + description + ", picPath=" + picPath + ", email=" + email + ", pwd=" + pwd + ", balance=" + balance + ", qtnPrice=" + qtnPrice + '}';
    }

}
