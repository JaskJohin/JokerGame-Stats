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
@Table(name = "PRICEPOINTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pricepoints.findAll", query = "SELECT p FROM Pricepoints p")
    , @NamedQuery(name = "Pricepoints.findByGameid", query = "SELECT p FROM Pricepoints p WHERE p.pricepointsPK.gameid = :gameid")
    , @NamedQuery(name = "Pricepoints.findByDrawid", query = "SELECT p FROM Pricepoints p WHERE p.pricepointsPK.drawid = :drawid")
    , @NamedQuery(name = "Pricepoints.findByAmount", query = "SELECT p FROM Pricepoints p WHERE p.amount = :amount")})
public class Pricepoints implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PricepointsPK pricepointsPK;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private double amount;
    @JoinColumns({
        @JoinColumn(name = "GAMEID", referencedColumnName = "GAMEID", insertable = false, updatable = false)
        , @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Content content;

    public Pricepoints() {
    }

    public Pricepoints(PricepointsPK pricepointsPK) {
        this.pricepointsPK = pricepointsPK;
    }

    public Pricepoints(PricepointsPK pricepointsPK, double amount) {
        this.pricepointsPK = pricepointsPK;
        this.amount = amount;
    }

    public Pricepoints(int gameid, int drawid) {
        this.pricepointsPK = new PricepointsPK(gameid, drawid);
    }

    public PricepointsPK getPricepointsPK() {
        return pricepointsPK;
    }

    public void setPricepointsPK(PricepointsPK pricepointsPK) {
        this.pricepointsPK = pricepointsPK;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
        hash += (pricepointsPK != null ? pricepointsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pricepoints)) {
            return false;
        }
        Pricepoints other = (Pricepoints) object;
        if ((this.pricepointsPK == null && other.pricepointsPK != null) || (this.pricepointsPK != null && !this.pricepointsPK.equals(other.pricepointsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "POJOs.Pricepoints[ pricepointsPK=" + pricepointsPK + " ]";
    }
    
}
