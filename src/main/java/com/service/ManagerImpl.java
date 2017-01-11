package com.service;

import com.domain.Mecz;
import com.domain.Stadion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Transactional
public class ManagerImpl implements Manager {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long addStadion(Stadion stadion) {
        Long id = (Long) sessionFactory.getCurrentSession().save(stadion);
        stadion.setStadionId(id);
        return id;
    }

    @Override
    public List<Stadion> getStadionByKraj(String stadionKraj) {
        List<Stadion> stadiony = new ArrayList<Stadion>();
        Pattern pattern = Pattern.compile(".*" + stadionKraj + ".*");
        Matcher matcher;
        for (Stadion s : getAllStadiony()) {
            matcher = pattern.matcher(s.getStadionKraj());
            if(matcher.matches())
                stadiony.add(s);
        }
        return stadiony;
    }

    @Override
    public Stadion getStadionByPattern(String stadionKraj) {
        return (Stadion) sessionFactory.getCurrentSession().getNamedQuery("stadion.kraj").setString("stadionKraj", stadionKraj).uniqueResult();
    }

    @Override
    public List<Stadion> getAllStadiony() {
        return sessionFactory.getCurrentSession().getNamedQuery("stadion.all").list();
    }

    @Override
    public void deleteStadion(Stadion stadion) {
        stadion = (Stadion) sessionFactory.getCurrentSession().get(Stadion.class, stadion.getStadionId());

        for (Mecz mecz : stadion.getMecze()) {
            mecz.setisDelivered(false);
            sessionFactory.getCurrentSession().update(mecz);
        }
        sessionFactory.getCurrentSession().delete(stadion);
    }

    @Override
    public Stadion getStadionById(Long stadionId) {
        return (Stadion) sessionFactory.getCurrentSession().get(Stadion.class, stadionId);
    }

    @Override
    public void updateStadion(Stadion stadion, String stadionNazwa, String stadionKraj, List<Mecz> mecze) {
        stadion = (Stadion) sessionFactory.getCurrentSession().get(Stadion.class, stadion.getStadionId());
        stadion.setStadionNazwa(stadionNazwa);
        stadion.setStadionKraj(stadionKraj);
        stadion.setMecze(mecze);
        sessionFactory.getCurrentSession().update(stadion);
    }

    @Override
    public Long addMecz(Mecz mecz) {
        Long id = (Long) sessionFactory.getCurrentSession().save(mecz);
        mecz.setId(id);
        return id;
    }

    @Override
    public List<Mecz> getAllMecze() {
        return sessionFactory.getCurrentSession().getNamedQuery("mecz.all").list();
    }

    @Override
    public void deleteMecz(Mecz mecz) {
        mecz = (Mecz) sessionFactory.getCurrentSession().get(Mecz.class, mecz.getId());
        Stadion stadion = (Stadion) sessionFactory.getCurrentSession().get(Stadion.class, mecz.getStadion().getStadionId());
        stadion.getMecze().remove(mecz);
        sessionFactory.getCurrentSession().delete(mecz);
    }

    @Override
    public Mecz getMeczById(Long meczId) {
        return (Mecz) sessionFactory.getCurrentSession().get(Mecz.class, meczId);
    }

    @Override
    public List<Mecz> getDeliveredMecz() {
        return sessionFactory.getCurrentSession().getNamedQuery("mecz.delivered").list();
    }

    @Override
    public void updateMecz(Mecz mecz, String meczNazwa, double meczCena, boolean isDelivered, Stadion stadion) {
        mecz = (Mecz) sessionFactory.getCurrentSession().get(Mecz.class, mecz.getId());
        mecz.setMeczNazwa(meczNazwa);
        mecz.setMeczCena(meczCena);
        mecz.setisDelivered(isDelivered);
        mecz.setStadion(stadion);
        sessionFactory.getCurrentSession().update(mecz);
    }

    @Override
    public List<Mecz> getMeczeByStadion(Stadion stadion) {
        stadion = (Stadion) sessionFactory.getCurrentSession().get(Stadion.class, stadion.getStadionId());
        List<Mecz> mecze = new ArrayList<Mecz>(stadion.getMecze());
        return mecze;
    }
}
