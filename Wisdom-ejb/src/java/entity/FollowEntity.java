/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Chuck
 */
@NamedQueries({
    @NamedQuery(name = "FollowEntity.findByAuthorAndReader",
            query = "SELECT f FROM FollowEntity f "
            + "WHERE f.author.id = :authorId "
            + "AND f.reader.id = :readerId" + " AND f.status != 'DELETED'"),
    @NamedQuery(name = "FollowEntity.findFollowedAuthorsByReader",
            query = "SELECT a FROM FollowEntity f, AuthorEntity a "
            + "WHERE f.author.id = a.id "
            + "AND f.reader.id = :readerId" + " AND f.status != 'DELETED'"),
    @NamedQuery(name = "FollowEntity.findFollowersByAuthor",
            query = "SELECT f FROM FollowEntity f, ReaderEntity r "
            + "WHERE f.reader.id = r.id "
            + "AND f.author.id = :authorId " + " AND f.status != 'DELETED'"),
    @NamedQuery(name = "FollowEntity.findFollowersByAuthor_PerMonth",
            query = "SELECT f FROM FollowEntity f, ReaderEntity r "
            + "WHERE f.reader.id = r.id "
            + "AND f.author.id = :authorId " + " AND f.status != 'DELETED' "
            + "AND f.createdMonth = :monthValue")
})
@Entity
public class FollowEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @OneToOne
    private AuthorEntity author;
    @OneToOne
    private ReaderEntity reader;
    private final LocalDateTime created;
    private Integer createdMonth;

    public FollowEntity() {
        this.created = LocalDateTime.now();
        this.createdMonth = created.getMonthValue();
    }

    public Long getId() {
        return id;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public ReaderEntity getReader() {
        return reader;
    }

    public void setReader(ReaderEntity reader) {
        this.reader = reader;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedMonth() {
        return createdMonth;
    }

    public void setCreatedMonth(Integer createdMonth) {
        this.createdMonth = createdMonth;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FollowEntity)) {
            return false;
        }
        FollowEntity other = (FollowEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FollowEntity{" + "id=" + id + ", author=" + author + ", reader=" + reader + ", created=" + created + '}';
    }

}
