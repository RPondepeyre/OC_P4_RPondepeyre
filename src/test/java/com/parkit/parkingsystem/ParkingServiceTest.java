package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Contains;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
    }

    @Test
    public void processIncomingCarTest() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any());
    }

    @Test
    public void processIncomingCarAlreadyInTest() {

        try {

            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber(inputReaderUtil.readVehicleRegistrationNumber());
            ticket.setInTime(LocalDateTime.now());
            ticket.setParkingSpot(parkingSpot);
            when(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber())).thenReturn(ticket);

        } catch (Exception e) {
            e.printStackTrace();
        }

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(0)).saveTicket(any());
    }

    @Test
    public void processExitingCarTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(LocalDateTime.now().minusHours(1));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingBikeTest() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any());
    }

    @Test
    public void processIncomingBikeAlreadyInTest() {

        try {

            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber(inputReaderUtil.readVehicleRegistrationNumber());
            ticket.setInTime(LocalDateTime.now());
            ticket.setParkingSpot(parkingSpot);
            when(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber())).thenReturn(ticket);

        } catch (Exception e) {
            e.printStackTrace();
        }

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(0)).saveTicket(any());
    }

    @Test
    public void processExitingBikeTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(LocalDateTime.now().minusHours(1));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleAlreadyOutTest() {

        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void ProcessIncomingFullParking() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();

            verify(parkingSpotDAO, Mockito.times(0)).updateParking(any());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void processincomingreUserTest() {
        try {
            PrintStream standardOut = System.out;
            ByteArrayOutputStream oscaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(oscaptor));

            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
            when(ticketDAO.searchRecUser(inputReaderUtil.readVehicleRegistrationNumber())).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();
            

            assertTrue(oscaptor.toString().contains("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount."));

            System.setOut(standardOut);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void ProcessIncomingElse() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(4);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();

            verify(ticketDAO, Mockito.times(0)).saveTicket(any());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ProcessExitingticketError() {

        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(LocalDateTime.now().minusHours(1));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

}
