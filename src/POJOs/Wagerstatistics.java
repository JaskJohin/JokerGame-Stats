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
@Table(name = "WAGERSTATISTICS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wagerstatistics.findAll", query = "SELECT w FROM Wagerstatistics w")
    , @NamedQuery(name = "Wagerstatistics.findByIndex", query = "SELECT w FROM Wagerstatistics w WHERE w.index = :index")
    , @NamedQuery(name = "Wagerstatistics.findByColumns", query = "SELECT w FROM Wagerstatistics w WHERE w.columns = :columns")
    , @NamedQuery(name = "Wagerstatistics.findByWagers", query = "SELECT w FROM Wagerstatistics w WHERE w.wagers = :wagers")})
public class Wagerstatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDEX")
    private Integer index;
    @Basic(optional = false)
    @Column(name = "COLUMNS")
    private int columns;
    @Basic(optional = false)
    @Column(name = "WAGERS")
    private int wagers;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID")
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")})
    @ManyToOne(optional = false)
    private Content content;

    public Wagerstatistics() {
    }

    public Wagerstatistics(Integer index) {
        this.index = index;
    }

    public Wagerstatistics(Integer index, int columns, int wagers) {
        this.index = index;
        this.columns = columns;
        this.wagers = wagers;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getWagers() {
        return wagers;
    }

    public void setWagers(int wagers) {
        this.wagers = wagers;
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
        if (!(object instanceof Wagerstatistics)) {
            return false;
        }
        Wagerstatistics other = (Wagerstatistics) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Wagerstatistics[ index=" + index + " ]";
    }
    
}
