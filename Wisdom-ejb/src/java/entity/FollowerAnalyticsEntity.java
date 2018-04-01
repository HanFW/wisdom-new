/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Yongxue
 */
@Entity
public class FollowerAnalyticsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer currentYear;
    private Integer jan;
    private Integer feb;
    private Integer mar;
    private Integer apr;
    private Integer may;
    private Integer jun;
    private Integer jul;
    private Integer aug;
    private Integer sep;
    private Integer oct;
    private Integer nov;
    private Integer december;

    public FollowerAnalyticsEntity() {

    }

    public FollowerAnalyticsEntity(Integer currentYear) {
        this.currentYear = currentYear;
        this.jan = 0;
        this.feb = 0;
        this.mar = 0;
        this.apr = 0;
        this.may = 0;
        this.jun = 0;
        this.jul = 0;
        this.aug = 0;
        this.sep = 0;
        this.oct = 0;
        this.nov = 0;
        this.december = 0;
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

    public Integer getJan() {
        return jan;
    }

    public void setJan(Integer jan) {
        this.jan = jan;
    }

    public Integer getFeb() {
        return feb;
    }

    public void setFeb(Integer feb) {
        this.feb = feb;
    }

    public Integer getMar() {
        return mar;
    }

    public void setMar(Integer mar) {
        this.mar = mar;
    }

    public Integer getApr() {
        return apr;
    }

    public void setApr(Integer apr) {
        this.apr = apr;
    }

    public Integer getMay() {
        return may;
    }

    public void setMay(Integer may) {
        this.may = may;
    }

    public Integer getJun() {
        return jun;
    }

    public void setJun(Integer jun) {
        this.jun = jun;
    }

    public Integer getJul() {
        return jul;
    }

    public void setJul(Integer jul) {
        this.jul = jul;
    }

    public Integer getAug() {
        return aug;
    }

    public void setAug(Integer aug) {
        this.aug = aug;
    }

    public Integer getSep() {
        return sep;
    }

    public void setSep(Integer sep) {
        this.sep = sep;
    }

    public Integer getOct() {
        return oct;
    }

    public void setOct(Integer oct) {
        this.oct = oct;
    }

    public Integer getNov() {
        return nov;
    }

    public void setNov(Integer nov) {
        this.nov = nov;
    }

    public Integer getDecember() {
        return december;
    }

    public void setDecember(Integer december) {
        this.december = december;
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
        if (!(object instanceof FollowerAnalyticsEntity)) {
            return false;
        }
        FollowerAnalyticsEntity other = (FollowerAnalyticsEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FollowAnalyticsEntity[ id=" + id + " ]";
    }

}
