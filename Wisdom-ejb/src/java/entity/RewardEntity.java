/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Yongxue
 */
@Entity
public class RewardEntity extends TransactionEntity {

    @OneToOne(cascade = {CascadeType.DETACH})
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
