package POJOs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */
@Entity
@Table(name = "WAGERSTATISTICS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wagerstatistics.findAll", query = "SELECT w FROM Wagerstatistics w")
    , @NamedQuery(name = "Wagerstatistics.findByGameid", query = "SELECT w FROM Wagerstatistics w WHERE w.wagerstatisticsPK.gameid = :gameid")
    , @NamedQuery(name = "Wagerstatistics.findByDrawid", query = "SELECT w FROM Wagerstatistics w WHERE w.wagerstatisticsPK.drawid = :drawid")
    , @NamedQuery(name = "Wagerstatistics.findByColumns", query = "SELECT w FROM Wagerstatistics w WHERE w.columns = :columns")
    , @NamedQuery(name = "Wagerstatistics.findByWagers", query = "SELECT w FROM Wagerstatistics w WHERE w.wagers = :wagers")})
public class Wagerstatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WagerstatisticsPK wagerstatisticsPK;
    @Basic(optional = false)
    @Column(name = "COLUMNS")
    private int columns;
    @Basic(optional = false)
    @Column(name = "WAGERS")
    private int wagers;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID", insertable = false, updatable = false)
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Content content;

    public Wagerstatistics() {
    }

    public Wagerstatistics(WagerstatisticsPK wagerstatisticsPK) {
        this.wagerstatisticsPK = wagerstatisticsPK;
    }

    public Wagerstatistics(WagerstatisticsPK wagerstatisticsPK, int columns, int wagers) {
        this.wagerstatisticsPK = wagerstatisticsPK;
        this.columns = columns;
        this.wagers = wagers;
    }

    public Wagerstatistics(int gameid, int drawid) {
        this.wagerstatisticsPK = new WagerstatisticsPK(gameid, drawid);
    }

    public WagerstatisticsPK getWagerstatisticsPK() {
        return wagerstatisticsPK;
    }

    public void setWagerstatisticsPK(WagerstatisticsPK wagerstatisticsPK) {
        this.wagerstatisticsPK = wagerstatisticsPK;
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
        hash += (wagerstatisticsPK != null ? wagerstatisticsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Wagerstatistics)) {
            return false;
        }
        Wagerstatistics other = (Wagerstatistics) object;
        if ((this.wagerstatisticsPK == null && other.wagerstatisticsPK != null) || (this.wagerstatisticsPK != null && !this.wagerstatisticsPK.equals(other.wagerstatisticsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Wagerstatistics[ wagerstatisticsPK=" + wagerstatisticsPK + " ]";
    }
    
}
