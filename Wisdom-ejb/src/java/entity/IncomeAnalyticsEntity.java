/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yongxue
 */
@Entity
public class IncomeAnalyticsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer currentYear;
    private Integer monthValue;
    private Double monthlyRewardIncome;
    private Double monthlyQuestionIncome;

    @ManyToOne(cascade = {CascadeType.DETACH})
    private AuthorEntity author;

    public IncomeAnalyticsEntity() {
        this.monthlyRewardIncome = 0.0;
        this.monthlyQuestionIncome = 0.0;
    }

    public Long getId() {
        return id;
    }

    public Integer getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(Integer currentYear) {
        this.currentYear = currentYear;
    }

    public Integer getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(Integer monthValue) {
        this.monthValue = monthValue;
    }

    public Double getMonthlyRewardIncome() {
        return monthlyRewardIncome;
    }

    public void setMonthlyRewardIncome(Double monthlyRewardIncome) {
        this.monthlyRewardIncome = monthlyRewardIncome;
    }

    public Double getMonthlyQuestionIncome() {
        return monthlyQuestionIncome;
    }

    public void setMonthlyQuestionIncome(Double monthlyQuestionIncome) {
        this.monthlyQuestionIncome = monthlyQuestionIncome;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IncomeAnalyticsEntity)) {
            return false;
        }
        IncomeAnalyticsEntity other = (IncomeAnalyticsEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.IncomeAnalyticsEntity[ id=" + id + " ]";
    }

}
