package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;

    @BeforeAll
    private static void setUp() {

        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    private void setUpPerTest() {

        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void NextAvailabeSlotTestCAR() {
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(1, result);

    }

    @Test
    public void NextAvailabeSlotTestBIKE() {
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        assertEquals(4, result);

    }

    @Test
    public void updateParkingTestNotAvailaible() {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(2, result);

    }

    @Test
    public void updateParkingTestAvailaible() {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(1, result);

    }

    @Test
    public void updateParkingTestNotAvailaibleBIKE() {

        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        parkingSpotDAO.updateParking(parkingSpot);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(5, result);

    }

    @Test
    public void updateParkingTestAvailaibleBIKE() {

        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        parkingSpotDAO.updateParking(parkingSpot);
        parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);
        parkingSpotDAO.updateParking(parkingSpot);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(4, result);

    }

}
