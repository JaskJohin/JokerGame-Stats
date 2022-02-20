/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Thanos Theodoropoulos
 */
@Embeddable
public class DrawsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "GAMEID")
    private int gameid;
    @Basic(optional = false)
    @Column(name = "DRAWID")
    private int drawid;

    public DrawsPK() {
    }

    public DrawsPK(int gameid, int drawid) {
        this.gameid = gameid;
        this.drawid = drawid;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getDrawid() {
        return drawid;
    }

    public void setDrawid(int drawid) {
        this.drawid = drawid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gameid;
        hash += (int) drawid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DrawsPK)) {
            return false;
        }
        DrawsPK other = (DrawsPK) object;
        if (this.gameid != other.gameid) {
            return false;
        }
        if (this.drawid != other.drawid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.DrawsPK[ gameid=" + gameid + ", drawid=" + drawid + " ]";
    }
    
}
