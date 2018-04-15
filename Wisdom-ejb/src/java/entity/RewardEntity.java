/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yongxue
 */
@Entity
@XmlRootElement // @XmlRootElement is for Jersey to serialize entity to JSON
@XmlAccessorType(XmlAccessType.FIELD)
public class RewardEntity extends TransactionEntity {

    @XmlTransient
    @ManyToOne(cascade = {CascadeType.DETACH})
    private ArticleEntity article;

    public RewardEntity() {
        super();
    }

    public RewardEntity(Double amount) {
        super(amount);
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }
}
