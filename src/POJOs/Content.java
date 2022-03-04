package POJOs;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

@Entity
@Table(name = "CONTENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Content.findAll", query = "SELECT c FROM Content c")
    , @NamedQuery(name = "Content.findByGameid", query = "SELECT c FROM Content c WHERE c.contentPK.gameid = :gameid")
    , @NamedQuery(name = "Content.findByDrawid", query = "SELECT c FROM Content c WHERE c.contentPK.drawid = :drawid")
    , @NamedQuery(name = "Content.findByDrawtime", query = "SELECT c FROM Content c WHERE c.drawtime = :drawtime")
    , @NamedQuery(name = "Content.findByStatus", query = "SELECT c FROM Content c WHERE c.status = :status")
    , @NamedQuery(name = "Content.findByDrawbreak", query = "SELECT c FROM Content c WHERE c.drawbreak = :drawbreak")
    , @NamedQuery(name = "Content.findByVisualdraw", query = "SELECT c FROM Content c WHERE c.visualdraw = :visualdraw")})
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ContentPK contentPK;
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
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "content")
    private Pricepoints pricepoints;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "content")
    private Collection<Winningnumbersbonus> winningnumbersbonusCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "content")
    private Collection<Prizecategories> prizecategoriesCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "content")
    private Wagerstatistics wagerstatistics;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "content")
    private Collection<Winningnumberslist> winningnumberslistCollection;

    public Content() {
    }

    public Content(ContentPK contentPK) {
        this.contentPK = contentPK;
    }

    public Content(ContentPK contentPK, long drawtime, String status, int drawbreak, int visualdraw) {
        this.contentPK = contentPK;
        this.drawtime = drawtime;
        this.status = status;
        this.drawbreak = drawbreak;
        this.visualdraw = visualdraw;
    }

    public Content(int gameid, int drawid) {
        this.contentPK = new ContentPK(gameid, drawid);
    }

    public ContentPK getContentPK() {
        return contentPK;
    }

    public void setContentPK(ContentPK contentPK) {
        this.contentPK = contentPK;
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

    public Pricepoints getPricepoints() {
        return pricepoints;
    }

    public void setPricepoints(Pricepoints pricepoints) {
        this.pricepoints = pricepoints;
    }

    @XmlTransient
    public Collection<Winningnumbersbonus> getWinningnumbersbonusCollection() {
        return winningnumbersbonusCollection;
    }

    public void setWinningnumbersbonusCollection(Collection<Winningnumbersbonus> winningnumbersbonusCollection) {
        this.winningnumbersbonusCollection = winningnumbersbonusCollection;
    }

    @XmlTransient
    public Collection<Prizecategories> getPrizecategoriesCollection() {
        return prizecategoriesCollection;
    }

    public void setPrizecategoriesCollection(Collection<Prizecategories> prizecategoriesCollection) {
        this.prizecategoriesCollection = prizecategoriesCollection;
    }

    public Wagerstatistics getWagerstatistics() {
        return wagerstatistics;
    }

    public void setWagerstatistics(Wagerstatistics wagerstatistics) {
        this.wagerstatistics = wagerstatistics;
    }

    @XmlTransient
    public Collection<Winningnumberslist> getWinningnumberslistCollection() {
        return winningnumberslistCollection;
    }

    public void setWinningnumberslistCollection(Collection<Winningnumberslist> winningnumberslistCollection) {
        this.winningnumberslistCollection = winningnumberslistCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contentPK != null ? contentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Content)) {
            return false;
        }
        Content other = (Content) object;
        if ((this.contentPK == null && other.contentPK != null) || (this.contentPK != null && !this.contentPK.equals(other.contentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Content[ contentPK=" + contentPK + " ]";
    }    
}