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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yongxue
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private Double amount;
    private LocalDateTime created; // initialised to .now() upon construction
    private String transactionType;
    private Integer createdMonth;

    @ManyToOne(cascade = {CascadeType.DETACH})
    private ReaderEntity from;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private AuthorEntity to;

    public TransactionEntity() {
        this.created = LocalDateTime.now();
        this.createdMonth = created.getMonthValue();
    }

    public TransactionEntity(Double amount) {
        this();
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getCreatedMonth() {
        return createdMonth;
    }

    public void setCreatedMonth(Integer createdMonth) {
        this.createdMonth = createdMonth;
    }

    public ReaderEntity getFrom() {
        return from;
    }

    public void setFrom(ReaderEntity from) {
        this.from = from;
    }

    public AuthorEntity getTo() {
        return to;
    }

    public void setTo(AuthorEntity to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" + "id=" + id + ", amount=" + amount + ", created=" + created + ", from=" + from + ", to=" + to + '}';
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
        final TransactionEntity other = (TransactionEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
