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
 *
 * @author Thanos Theodoropoulos
 */
@Entity
@Table(name = "WINNINGNUMBERSBONUS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Winningnumbersbonus.findAll", query = "SELECT w FROM Winningnumbersbonus w")
    , @NamedQuery(name = "Winningnumbersbonus.findByIndex", query = "SELECT w FROM Winningnumbersbonus w WHERE w.index = :index")
    , @NamedQuery(name = "Winningnumbersbonus.findByBonus", query = "SELECT w FROM Winningnumbersbonus w WHERE w.bonus = :bonus")})
public class Winningnumbersbonus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDEX")
    private Integer index;
    @Basic(optional = false)
    @Column(name = "BONUS")
    private int bonus;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID")
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")})
    @ManyToOne(optional = false)
    private Content content;

    public Winningnumbersbonus() {
    }

    public Winningnumbersbonus(Integer index) {
        this.index = index;
    }

    public Winningnumbersbonus(Integer index, int bonus) {
        this.index = index;
        this.bonus = bonus;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
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
        if (!(object instanceof Winningnumbersbonus)) {
            return false;
        }
        Winningnumbersbonus other = (Winningnumbersbonus) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Winningnumbersbonus[ index=" + index + " ]";
    }
    
}
