package com.domain;
import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "mecz.all", query = "SELECT m FROM Mecz m"),
        @NamedQuery(name = "mecz.delivered", query = "select m from Mecz m where m.isDelivered = true")
})
public class Mecz implements java.io.Serializable{
    private Long id;
    private Stadion stadion;
    private String meczNazwa;
    private double meczCena;
    private boolean isDelivered = true;

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadionId", nullable = false)   
    public Stadion getStadion() {
        return stadion;
    }
    public void setStadion(Stadion stadion) {
        this.stadion = stadion;
    }
    
    @Column(nullable = false)    
    public String getMeczNazwa() {
        return meczNazwa;
    }
    public void setMeczNazwa(String meczNazwa) {
        this.meczNazwa = meczNazwa;
    }
    
    @Column(nullable = false)   
    public double getMeczCena() {
        return meczCena;
    }
    public void setMeczCena(double meczCena) {
        this.meczCena = meczCena;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public void setisDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }
}