package com.domain;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "stadion.all", query = "select s from Stadion s"),
        @NamedQuery(name = "stadion.kraj", query = "select s from Stadion s where s.stadionKraj like :stadionKraj")
})
public class Stadion {
    private Long stadionId;
    private String stadionNazwa;
    private String stadionKraj;

    private List<Mecz> mecze = new ArrayList<Mecz>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getStadionId() {
        return stadionId;
    }
    public void setStadionId(Long stadionId) {
        this.stadionId = stadionId;
    }
    
    @OneToMany(mappedBy = "stadion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Mecz> getMecze() {
        return mecze;
    }
    public void setMecze(List<Mecz> mecze) {
        this.mecze = mecze;
    }
    
    @Column(nullable = false)
    public String getStadionNazwa() {
        return stadionNazwa;
    }
    public void setStadionNazwa(String stadionNazwa) {
        this.stadionNazwa = stadionNazwa;
    }

    public String getStadionKraj() {
        return stadionKraj;
    }
    public void setStadionKraj(String stadionKraj) {
        this.stadionKraj = stadionKraj;
    }
}