package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        try{
            
           Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
            assert(ticket !=null);
            if(ticket !=null){
               ParkingSpot parkingspot = ticket.getParkingSpot();
               assert(!parkingspot.isAvailable());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingLotExit() throws InterruptedException{

        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        TimeUnit.MILLISECONDS.sleep(500);
        parkingService.processExitingVehicle();
            try{
                Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
                assert(ticket.getOutTime() != null);
                // TODO assert price

            }
            catch(Exception e){
                e.printStackTrace();
            }
        
        //TODO: check that the fare generated and out time are populated correctly in the database
    }
    @Test
    public void testParkingrecUser(){
        try{
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
            Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
            TimeUnit.MILLISECONDS.sleep(500);
            assertEquals(false, ticket.getRecuser());
        parkingService.processExitingVehicle();
        parkingService.processIncomingVehicle();
            ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
            assertEquals(true, ticket.getRecuser());
        parkingService.processExitingVehicle();
        }catch( Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParkingtwice(){
        try{
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        int id1 = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getId();
        parkingService.processIncomingVehicle();
        int id2 = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getId();
        assertEquals(id1, id2);
        }catch( Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void TestRecUserComingbackTwoTimes(){
        try{
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        TimeUnit.MILLISECONDS.sleep(500);
        parkingService.processExitingVehicle();
        int id1 = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getId();
        parkingService.processIncomingVehicle();
        int id2 = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getId();
        parkingService.processIncomingVehicle();
        int id3 = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getId();
        assertEquals(id2, id3);
        assertNotEquals(id1, id3);
        }catch( Exception e){
            e.printStackTrace();
        }
    }



}
