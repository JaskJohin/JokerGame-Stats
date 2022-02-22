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
@Table(name = "WINNINGNUMBERSLIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Winningnumberslist.findAll", query = "SELECT w FROM Winningnumberslist w")
    , @NamedQuery(name = "Winningnumberslist.findByIndex", query = "SELECT w FROM Winningnumberslist w WHERE w.index = :index")
    , @NamedQuery(name = "Winningnumberslist.findByNumber", query = "SELECT w FROM Winningnumberslist w WHERE w.number = :number")})
public class Winningnumberslist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDEX")
    private Integer index;
    @Basic(optional = false)
    @Column(name = "NUMBER")
    private int number;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID")
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")})
    @ManyToOne(optional = false)
    private Content content;

    public Winningnumberslist() {
    }

    public Winningnumberslist(Integer index) {
        this.index = index;
    }

    public Winningnumberslist(Integer index, int number) {
        this.index = index;
        this.number = number;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
        if (!(object instanceof Winningnumberslist)) {
            return false;
        }
        Winningnumberslist other = (Winningnumberslist) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Winningnumberslist[ index=" + index + " ]";
    }
    
}
