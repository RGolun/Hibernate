package com.service;

import com.domain.Mecz;
import com.domain.Stadion;

import java.util.List;

public interface Manager {
	
    //Stadion
    Long addStadion(Stadion stadion);
    void deleteStadion(Stadion stadion);
    List getStadionByKraj(String stadionKraj);
    List<Stadion> getAllStadiony();
    Stadion getStadionById(Long stadionId);
    void updateStadion(Stadion stadion, String stadionNazwa, String stadionKraj, List<Mecz> Mecze);
    public Stadion getStadionByPattern(String Kraj);

    //Mecz
    Long addMecz(Mecz mecz);
    void deleteMecz(Mecz mecz);
    List<Mecz> getAllMecze();
    Mecz getMeczById(Long meczId);
    List<Mecz> getDeliveredMecz();
    void updateMecz(Mecz mecz, String meczNazwa, double meczCena, boolean isDelivered, Stadion stadion);
    List<Mecz> getMeczeByStadion(Stadion stadion);
}
