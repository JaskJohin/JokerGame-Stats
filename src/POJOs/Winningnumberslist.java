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
@Table(name = "WINNINGNUMBERSLIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Winningnumberslist.findAll", query = "SELECT w FROM Winningnumberslist w")
    , @NamedQuery(name = "Winningnumberslist.findByIndex", query = "SELECT w FROM Winningnumberslist w WHERE w.winningnumberslistPK.index = :index")
    , @NamedQuery(name = "Winningnumberslist.findByGameid", query = "SELECT w FROM Winningnumberslist w WHERE w.winningnumberslistPK.gameid = :gameid")
    , @NamedQuery(name = "Winningnumberslist.findByDrawid", query = "SELECT w FROM Winningnumberslist w WHERE w.winningnumberslistPK.drawid = :drawid")
    , @NamedQuery(name = "Winningnumberslist.findByNumber", query = "SELECT w FROM Winningnumberslist w WHERE w.number = :number")})
public class Winningnumberslist implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WinningnumberslistPK winningnumberslistPK;
    @Basic(optional = false)
    @Column(name = "NUMBER")
    private int number;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID", insertable = false, updatable = false)
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Content content;

    public Winningnumberslist() {
    }

    public Winningnumberslist(WinningnumberslistPK winningnumberslistPK) {
        this.winningnumberslistPK = winningnumberslistPK;
    }

    public Winningnumberslist(WinningnumberslistPK winningnumberslistPK, int number) {
        this.winningnumberslistPK = winningnumberslistPK;
        this.number = number;
    }

    public Winningnumberslist(int index, int gameid, int drawid) {
        this.winningnumberslistPK = new WinningnumberslistPK(index, gameid, drawid);
    }

    public WinningnumberslistPK getWinningnumberslistPK() {
        return winningnumberslistPK;
    }

    public void setWinningnumberslistPK(WinningnumberslistPK winningnumberslistPK) {
        this.winningnumberslistPK = winningnumberslistPK;
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
        hash += (winningnumberslistPK != null ? winningnumberslistPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Winningnumberslist)) {
            return false;
        }
        Winningnumberslist other = (Winningnumberslist) object;
        if ((this.winningnumberslistPK == null && other.winningnumberslistPK != null) || (this.winningnumberslistPK != null && !this.winningnumberslistPK.equals(other.winningnumberslistPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Winningnumberslist[ winningnumberslistPK=" + winningnumberslistPK + " ]";
    }    
}