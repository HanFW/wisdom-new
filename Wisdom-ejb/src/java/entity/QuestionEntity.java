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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import utility.Constants;

/**
 *
 * @author Yongxue
 */
@Entity
public class QuestionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content; // reader's qtn
    private String status; // ANSWERED, REJECTED, PENDING, EXPIRED
    private String reply; // author's reply
    private LocalDateTime created;
    
    @OneToOne(cascade = {CascadeType.DETACH})
    private ReaderEntity reader; // raised the qtn
    @OneToOne(cascade = {CascadeType.DETACH})
    private AuthorEntity author; // asked to answer
    @OneToOne(cascade = {CascadeType.DETACH})
    private CompensationEntity compensation; 

    public QuestionEntity() {
        this.status = Constants.STATUS_PENDING;
        this.reply = null;
        this.created = LocalDateTime.now();
    }
    
    public QuestionEntity(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ReaderEntity getReader() {
        return reader;
    }

    public void setReader(ReaderEntity reader) {
        this.reader = reader;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public CompensationEntity getCompensation() {
        return compensation;
    }

    public void setCompensation(CompensationEntity compensation) {
        this.compensation = compensation;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" + "id=" + id + ", title=" + title + ", content=" + content + ", status=" + status + ", reply=" + reply + ", created=" + created + ", reader=" + reader + ", author=" + author + ", compensation=" + compensation + '}';
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
        final QuestionEntity other = (QuestionEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}
