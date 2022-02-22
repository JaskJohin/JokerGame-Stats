/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */
@Entity
@Table(name = "PRICEPOINTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pricepoints.findAll", query = "SELECT p FROM Pricepoints p")
    , @NamedQuery(name = "Pricepoints.findByIndex", query = "SELECT p FROM Pricepoints p WHERE p.index = :index")
    , @NamedQuery(name = "Pricepoints.findByAmount", query = "SELECT p FROM Pricepoints p WHERE p.amount = :amount")})
public class Pricepoints implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDEX")
    private Integer index;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private double amount;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID")
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")})
    @ManyToOne(optional = false)
    private Content content;

    public Pricepoints() {
    }

    public Pricepoints(Integer index) {
        this.index = index;
    }

    public Pricepoints(Integer index, double amount) {
        this.index = index;
        this.amount = amount;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (index != null ? index.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pricepoints)) {
            return false;
        }
        Pricepoints other = (Pricepoints) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Pricepoints[ index=" + index + " ]";
    }
    
}
