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
public class PrizecategoriesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "GAMEID")
    private int gameid;
    @Basic(optional = false)
    @Column(name = "DRAWID")
    private int drawid;
    @Basic(optional = false)
    @Column(name = "CATEGORYID")
    private int categoryid;

    public PrizecategoriesPK() {
    }

    public PrizecategoriesPK(int gameid, int drawid, int categoryid) {
        this.gameid = gameid;
        this.drawid = drawid;
        this.categoryid = categoryid;
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

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gameid;
        hash += (int) drawid;
        hash += (int) categoryid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrizecategoriesPK)) {
            return false;
        }
        PrizecategoriesPK other = (PrizecategoriesPK) object;
        if (this.gameid != other.gameid) {
            return false;
        }
        if (this.drawid != other.drawid) {
            return false;
        }
        if (this.categoryid != other.categoryid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.PrizecategoriesPK[ gameid=" + gameid + ", drawid=" + drawid + ", categoryid=" + categoryid + " ]";
    }
    
}
