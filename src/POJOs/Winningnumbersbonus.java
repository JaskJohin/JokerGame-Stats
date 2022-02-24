package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "WINNINGNUMBERSBONUS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Winningnumbersbonus.findAll", query = "SELECT w FROM Winningnumbersbonus w")
    , @NamedQuery(name = "Winningnumbersbonus.findByIndex", query = "SELECT w FROM Winningnumbersbonus w WHERE w.winningnumbersbonusPK.index = :index")
    , @NamedQuery(name = "Winningnumbersbonus.findByGameid", query = "SELECT w FROM Winningnumbersbonus w WHERE w.winningnumbersbonusPK.gameid = :gameid")
    , @NamedQuery(name = "Winningnumbersbonus.findByDrawid", query = "SELECT w FROM Winningnumbersbonus w WHERE w.winningnumbersbonusPK.drawid = :drawid")
    , @NamedQuery(name = "Winningnumbersbonus.findByBonus", query = "SELECT w FROM Winningnumbersbonus w WHERE w.bonus = :bonus")})
public class Winningnumbersbonus implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WinningnumbersbonusPK winningnumbersbonusPK;
    @Basic(optional = false)
    @Column(name = "BONUS")
    private int bonus;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID", insertable = false, updatable = false)
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Content content;

    public Winningnumbersbonus() {
    }

    public Winningnumbersbonus(WinningnumbersbonusPK winningnumbersbonusPK) {
        this.winningnumbersbonusPK = winningnumbersbonusPK;
    }

    public Winningnumbersbonus(WinningnumbersbonusPK winningnumbersbonusPK, int bonus) {
        this.winningnumbersbonusPK = winningnumbersbonusPK;
        this.bonus = bonus;
    }

    public Winningnumbersbonus(int index, int gameid, int drawid) {
        this.winningnumbersbonusPK = new WinningnumbersbonusPK(index, gameid, drawid);
    }

    public WinningnumbersbonusPK getWinningnumbersbonusPK() {
        return winningnumbersbonusPK;
    }

    public void setWinningnumbersbonusPK(WinningnumbersbonusPK winningnumbersbonusPK) {
        this.winningnumbersbonusPK = winningnumbersbonusPK;
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
        hash += (winningnumbersbonusPK != null ? winningnumbersbonusPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Winningnumbersbonus)) {
            return false;
        }
        Winningnumbersbonus other = (Winningnumbersbonus) object;
        if ((this.winningnumbersbonusPK == null && other.winningnumbersbonusPK != null) || (this.winningnumbersbonusPK != null && !this.winningnumbersbonusPK.equals(other.winningnumbersbonusPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Winningnumbersbonus[ winningnumbersbonusPK=" + winningnumbersbonusPK + " ]";
    }
    
}
