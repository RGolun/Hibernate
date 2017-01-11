package com.service;

import com.domain.Mecz;
import com.domain.Stadion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional

public class ManagerTest {
    @Autowired
    Manager manager;

    private final String stadionNazwa1 = "stadionNazwa1";
    private final String stadionKraj1 = "stadionKraj1";

    private final String stadionNazwa2 = "stadionNazwa2";
    private final String stadionKraj2 = "stadionKraj2";

    private final String meczNazwa1 = "meczNazwa1";
    private final double meczCena1 = 100000.0;

    private final String meczNazwa2 = "meczNazwa2";
    private final double meczCena2 = 200000.0;

    Mecz mecz = new Mecz();
    Stadion stadion = new Stadion();
    Stadion s = new Stadion();

   @Before
    public void before() {
        stadion.setStadionNazwa(stadionNazwa1);
        stadion.setStadionKraj(stadionKraj1);

        s.setStadionNazwa(stadionNazwa1);
        s.setStadionKraj(stadionKraj1);

        mecz.setMeczNazwa(meczNazwa1);
        mecz.setMeczCena(meczCena1);
        mecz.setStadion(stadion);
        mecz.setisDelivered(true);
    }

    @After
    public void after() {
        List<Stadion> stadiony = manager.getAllStadiony();
        List<Mecz> mecze = manager.getAllMecze();

        for(Mecz me : mecze) {
            if(me.getId() == mecz.getId())
                manager.deleteMecz(mecz);
        }

        for(Stadion st : stadiony) {
            if(st.getStadionId() == stadion.getStadionId())
                manager.deleteStadion(stadion);
            if(st.getStadionId() == s.getStadionId())
                manager.deleteStadion(s);
        }
    }

    //Stadion

    @Test
    public void addStadionCheck() {
        Long id = manager.addStadion(stadion);

        Stadion retrievedStadion = manager.getStadionById(id);

        assertEquals(stadionNazwa1, retrievedStadion.getStadionNazwa());
        assertEquals(stadionKraj1, retrievedStadion.getStadionKraj());
    }

   @Test
    public void getAllStadionyCheck() {
        List<Stadion> stadiony = manager.getAllStadiony();
        int count = stadiony.size();
        manager.addStadion(stadion);
        stadiony = manager.getAllStadiony();
        assertEquals(count+1, stadiony.size());
    }

    @Test
    public void deleteStadionCheck() {
        manager.addStadion(stadion);
        int count = manager.getAllStadiony().size();
        manager.deleteStadion(stadion);
        assertEquals(count-1, manager.getAllStadiony().size());
        assertNull(manager.getStadionById(stadion.getStadionId()));
    }  
    
    @Test
    public void getStadionyByIdCheck() {
        Long id = manager.addStadion(stadion);
        Stadion stadion = manager.getStadionById(id);
        assertEquals(id, stadion.getStadionId());
        assertEquals(stadionNazwa1, stadion.getStadionNazwa());
        assertEquals(stadionKraj1, stadion.getStadionKraj());
    }

    @Test
    public void getStadionyByKrajCheck() {
        List<Stadion> stadiony = manager.getAllStadiony();
        manager.addStadion(stadion);
        String kraj = "Kraj";
        int count = 0;

        for(Stadion s : stadiony) {
            if(Pattern.compile(".*" + kraj + ".*").matcher(manager.getStadionById(s.getStadionId()).getStadionKraj()).matches())
                count++;
        }
        stadiony = manager.getStadionByKraj(kraj);
        assertEquals(stadiony.size(), count + 1);

        stadiony = manager.getAllStadiony();
        manager.addStadion(stadion);
        kraj = "kraj";
        count = 0;

        for(Stadion s : stadiony) {
            if(Pattern.compile(".*" + kraj + ".*").matcher(manager.getStadionById(s.getStadionId()).getStadionKraj()).matches())
                count++;
        }
        stadiony = manager.getStadionByKraj(kraj);
        assertEquals(stadiony.size(), count);
    }

    @Test
    public void getStadionByPatternCheck() {
        String kraj = "stadionKraj1";
        manager.addStadion(s);

        assertEquals(manager.getStadionByPattern(kraj), s);
        kraj = "pattern";
        assertNull(manager.getStadionByPattern(kraj));
    }
    
    @Test
    public void updateStadionCheck() {
        List<Mecz> mecze = new ArrayList<Mecz>();
        mecze.add(mecz);
        stadion.setMecze(mecze);
        manager.addStadion(stadion);
        manager.updateStadion(stadion, stadionNazwa2, stadionKraj2, mecze);
        assertEquals(stadionNazwa2, stadion.getStadionNazwa());
        assertEquals(stadionKraj2, stadion.getStadionKraj());
        assertEquals(mecze, stadion.getMecze());

        List<Stadion> stadiony = manager.getAllStadiony();
        for(Stadion s : stadiony) {
            if(!s.equals(stadion)) {
                assertThat(s.getStadionNazwa(), is(not(stadionNazwa2)));
                assertThat(s.getStadionKraj(), is(not(stadionKraj2)));
                assertThat(s.getMecze(), is(not(mecze)));
            }
        }
    }
}
