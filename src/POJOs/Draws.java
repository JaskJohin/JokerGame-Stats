/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thanos Theodoropoulos
 */
@Entity
@Table(name = "DRAWS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Draws.findAll", query = "SELECT d FROM Draws d")
    , @NamedQuery(name = "Draws.findByGameid", query = "SELECT d FROM Draws d WHERE d.drawsPK.gameid = :gameid")
    , @NamedQuery(name = "Draws.findByDrawid", query = "SELECT d FROM Draws d WHERE d.drawsPK.drawid = :drawid")
    , @NamedQuery(name = "Draws.findByDrawtime", query = "SELECT d FROM Draws d WHERE d.drawtime = :drawtime")
    , @NamedQuery(name = "Draws.findByStatus", query = "SELECT d FROM Draws d WHERE d.status = :status")
    , @NamedQuery(name = "Draws.findByDrawbreak", query = "SELECT d FROM Draws d WHERE d.drawbreak = :drawbreak")
    , @NamedQuery(name = "Draws.findByVisualdraw", query = "SELECT d FROM Draws d WHERE d.visualdraw = :visualdraw")})
public class Draws implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DrawsPK drawsPK;
    @Basic(optional = false)
    @Column(name = "DRAWTIME")
    private long drawtime;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "DRAWBREAK")
    private int drawbreak;
    @Basic(optional = false)
    @Column(name = "VISUALDRAW")
    private int visualdraw;

    public Draws() {
    }

    public Draws(DrawsPK drawsPK) {
        this.drawsPK = drawsPK;
    }

    public Draws(DrawsPK drawsPK, long drawtime, String status, int drawbreak, int visualdraw) {
        this.drawsPK = drawsPK;
        this.drawtime = drawtime;
        this.status = status;
        this.drawbreak = drawbreak;
        this.visualdraw = visualdraw;
    }

    public Draws(int gameid, int drawid) {
        this.drawsPK = new DrawsPK(gameid, drawid);
    }

    public DrawsPK getDrawsPK() {
        return drawsPK;
    }

    public void setDrawsPK(DrawsPK drawsPK) {
        this.drawsPK = drawsPK;
    }

    public long getDrawtime() {
        return drawtime;
    }

    public void setDrawtime(long drawtime) {
        this.drawtime = drawtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDrawbreak() {
        return drawbreak;
    }

    public void setDrawbreak(int drawbreak) {
        this.drawbreak = drawbreak;
    }

    public int getVisualdraw() {
        return visualdraw;
    }

    public void setVisualdraw(int visualdraw) {
        this.visualdraw = visualdraw;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (drawsPK != null ? drawsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Draws)) {
            return false;
        }
        Draws other = (Draws) object;
        if ((this.drawsPK == null && other.drawsPK != null) || (this.drawsPK != null && !this.drawsPK.equals(other.drawsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Draws[ drawsPK=" + drawsPK + " ]";
    }
    
}
