/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yongxue
 */
@NamedQueries({
    @NamedQuery(name = "ReaderEntity.findFollowersByAuthor",
            query = "SELECT r FROM FollowEntity f, ReaderEntity r "
                    + "WHERE f.reader.id = r.id "
                    + "AND f.author.id = :authorId ")
})
@Entity
public class ReaderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // format: [firstName lastName]
    private String email; // no setter
    private String pwd;
    private String picPath;
    private Double balance; // topped up credit - usage
    // available topics stored in client app
    private ArrayList<String> topics = new ArrayList<>(); // interested topics
    
    @XmlTransient
    @OneToMany(cascade = CascadeType.DETACH)
    private List<ArticleEntity> saved = new ArrayList<>();
    
    public ReaderEntity() {
        this.picPath = null;
        this.balance = 0.0;
    }
    
    public ReaderEntity(String name, String email, String pwd) {
        this();
        this.name = name;
        this.email = email;
        this.pwd = pwd;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public List<ArticleEntity> getSaved() {
        return saved;
    }

    public void setSaved(List<ArticleEntity> saved) {
        this.saved = saved;
    }

    @Override
    public String toString() {
        return "ReaderEntity{" + "id=" + id + ", name=" + name + ", email=" + email + ", pwd=" + pwd + ", picPath=" + picPath + ", balance=" + balance + ", topics=" + topics + ", saved=" + saved + '}';
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
        final ReaderEntity other = (ReaderEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
