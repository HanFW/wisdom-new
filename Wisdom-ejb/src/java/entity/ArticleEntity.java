/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Yongxue
 */
@Entity
public class ArticleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String description; // short intro text
    private String picPath; // path to article picture
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private Integer numOfUpvotes;
    private LocalDateTime created; // time of creation

    @ManyToOne(cascade = {CascadeType.DETACH})
    private AuthorEntity author;
    @OneToMany(cascade = {CascadeType.DETACH}, mappedBy = "article")
    private List<RewardEntity> rewards = new ArrayList<>();

    public ArticleEntity() {
        this.picPath = null; // default to no pic
        this.numOfUpvotes = 0;
        this.created = LocalDateTime.now();
    }

    public ArticleEntity(String topic, String title, String description,
            String content, LocalDateTime created) {
        this();
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.content = content;
        this.created = created;
        // relationship fields are set using setters
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNumOfUpvotes() {
        return numOfUpvotes;
    }

    public void setNumOfUpvotes(Integer numOfUpvotes) {
        this.numOfUpvotes = numOfUpvotes;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public List<RewardEntity> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardEntity> rewards) {
        this.rewards = rewards;
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
        final ArticleEntity other = (ArticleEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" + "id=" + id + ", topic=" + topic + ", title=" + title + ", description=" + description + ", picPath=" + picPath + ", content=" + content + ", numOfUpvotes=" + numOfUpvotes + ", created=" + created + ", author=" + author + '}';
    }
}
