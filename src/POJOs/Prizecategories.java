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
@Table(name = "PRIZECATEGORIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prizecategories.findAll", query = "SELECT p FROM Prizecategories p")
    , @NamedQuery(name = "Prizecategories.findByGameid", query = "SELECT p FROM Prizecategories p WHERE p.prizecategoriesPK.gameid = :gameid")
    , @NamedQuery(name = "Prizecategories.findByDrawid", query = "SELECT p FROM Prizecategories p WHERE p.prizecategoriesPK.drawid = :drawid")
    , @NamedQuery(name = "Prizecategories.findByCategoryid", query = "SELECT p FROM Prizecategories p WHERE p.prizecategoriesPK.categoryid = :categoryid")
    , @NamedQuery(name = "Prizecategories.findByDivident", query = "SELECT p FROM Prizecategories p WHERE p.divident = :divident")
    , @NamedQuery(name = "Prizecategories.findByWinners", query = "SELECT p FROM Prizecategories p WHERE p.winners = :winners")
    , @NamedQuery(name = "Prizecategories.findByDistributed", query = "SELECT p FROM Prizecategories p WHERE p.distributed = :distributed")
    , @NamedQuery(name = "Prizecategories.findByJackpot", query = "SELECT p FROM Prizecategories p WHERE p.jackpot = :jackpot")
    , @NamedQuery(name = "Prizecategories.findByFixed", query = "SELECT p FROM Prizecategories p WHERE p.fixed = :fixed")
    , @NamedQuery(name = "Prizecategories.findByCategorytype", query = "SELECT p FROM Prizecategories p WHERE p.categorytype = :categorytype")
    , @NamedQuery(name = "Prizecategories.findByGametype", query = "SELECT p FROM Prizecategories p WHERE p.gametype = :gametype")})
public class Prizecategories implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PrizecategoriesPK prizecategoriesPK;
    @Basic(optional = false)
    @Column(name = "DIVIDENT")
    private double divident;
    @Basic(optional = false)
    @Column(name = "WINNERS")
    private int winners;
    @Basic(optional = false)
    @Column(name = "DISTRIBUTED")
    private double distributed;
    @Basic(optional = false)
    @Column(name = "JACKPOT")
    private double jackpot;
    @Basic(optional = false)
    @Column(name = "FIXED")
    private double fixed;
    @Basic(optional = false)
    @Column(name = "CATEGORYTYPE")
    private int categorytype;
    @Basic(optional = false)
    @Column(name = "GAMETYPE")
    private String gametype;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID", insertable = false, updatable = false)
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Content content;

    public Prizecategories() {
    }

    public Prizecategories(PrizecategoriesPK prizecategoriesPK) {
        this.prizecategoriesPK = prizecategoriesPK;
    }

    public Prizecategories(PrizecategoriesPK prizecategoriesPK, double divident, int winners, double distributed, double jackpot, double fixed, int categorytype, String gametype) {
        this.prizecategoriesPK = prizecategoriesPK;
        this.divident = divident;
        this.winners = winners;
        this.distributed = distributed;
        this.jackpot = jackpot;
        this.fixed = fixed;
        this.categorytype = categorytype;
        this.gametype = gametype;
    }

    public Prizecategories(int gameid, int drawid, int categoryid) {
        this.prizecategoriesPK = new PrizecategoriesPK(gameid, drawid, categoryid);
    }

    public PrizecategoriesPK getPrizecategoriesPK() {
        return prizecategoriesPK;
    }

    public void setPrizecategoriesPK(PrizecategoriesPK prizecategoriesPK) {
        this.prizecategoriesPK = prizecategoriesPK;
    }

    public double getDivident() {
        return divident;
    }

    public void setDivident(double divident) {
        this.divident = divident;
    }

    public int getWinners() {
        return winners;
    }

    public void setWinners(int winners) {
        this.winners = winners;
    }

    public double getDistributed() {
        return distributed;
    }

    public void setDistributed(double distributed) {
        this.distributed = distributed;
    }

    public double getJackpot() {
        return jackpot;
    }

    public void setJackpot(double jackpot) {
        this.jackpot = jackpot;
    }

    public double getFixed() {
        return fixed;
    }

    public void setFixed(double fixed) {
        this.fixed = fixed;
    }

    public int getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(int categorytype) {
        this.categorytype = categorytype;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
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
        hash += (prizecategoriesPK != null ? prizecategoriesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prizecategories)) {
            return false;
        }
        Prizecategories other = (Prizecategories) object;
        if ((this.prizecategoriesPK == null && other.prizecategoriesPK != null) || (this.prizecategoriesPK != null && !this.prizecategoriesPK.equals(other.prizecategoriesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Prizecategories[ prizecategoriesPK=" + prizecategoriesPK + " ]";
    }    
}