package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

@Embeddable
public class WagerstatisticsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "GAMEID")
    private int gameid;
    @Basic(optional = false)
    @Column(name = "DRAWID")
    private int drawid;

    public WagerstatisticsPK() {
    }

    public WagerstatisticsPK(int gameid, int drawid) {
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
        if (!(object instanceof WagerstatisticsPK)) {
            return false;
        }
        WagerstatisticsPK other = (WagerstatisticsPK) object;
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
        return "POJOs.WagerstatisticsPK[ gameid=" + gameid + ", drawid=" + drawid + " ]";
    }    
}