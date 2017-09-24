package com.ajibigad.erazer.model.expense;

import com.ajibigad.erazer.security.model.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ajibigad on 05/08/2017.
 */

@Entity
public class Expense {

    @Id
    @GeneratedValue
    private long id;

    private String title;
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateAdded;

    @Enumerated(EnumType.STRING)
    private PROOF_TYPE proofType;

    private String proof;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    private double cost;

    @Column(name = "state", columnDefinition = "enum('APPROVED','DECLINED','SETTLED','PENDING')")
    @Enumerated(EnumType.STRING)
    private STATE state = STATE.PENDING;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public PROOF_TYPE getProofType() {
        return proofType;
    }

    public void setProofType(PROOF_TYPE proofType) {
        this.proofType = proofType;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
