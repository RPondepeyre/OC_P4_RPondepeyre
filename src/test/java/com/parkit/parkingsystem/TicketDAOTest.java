package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    Ticket enterticket = new Ticket();

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    private void setUpPerTest() {

        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void TicketenterTest() {

        ParkingSpot ps = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setParkingSpot(ps);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);

        Ticket exitticket = ticketDAO.getTicket("ABCDE");

        assertEquals(enterticket.getId(), exitticket.getId());
        assertEquals(enterticket.getInTime(), exitticket.getInTime());
        assertEquals(enterticket.getParkingSpot(), exitticket.getParkingSpot());

    }

    @Test
    public void TicketExitTest() {

        ParkingSpot ps = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setParkingSpot(ps);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);
        enterticket = ticketDAO.getTicket("ABCDE");

        enterticket.setOutTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        enterticket.setPrice(10);

        ticketDAO.updateTicket(enterticket);
        Ticket exitticket = ticketDAO.getTicket("ABCDE");
        assertEquals(enterticket.getOutTime(), exitticket.getOutTime());
        assertEquals(enterticket.getPrice(), exitticket.getPrice());

    }

    @Test
    public void SaveTicketwithOutTimeTest() {

        ParkingSpot ps = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime outTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setOutTime(outTime);
        enterticket.setParkingSpot(ps);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);

        Ticket exitticket = ticketDAO.getTicket("ABCDE");

        assertEquals(enterticket.getOutTime(), exitticket.getOutTime());

    }

    @Test
    public void GetTicketdontexist() {

        Ticket ticket = ticketDAO.getTicket("TICKET");
        assertEquals(null, ticket);

    }

    @Test
    public void searchRecUsertrue() {

        ParkingSpot ps = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime outTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setOutTime(outTime);
        enterticket.setParkingSpot(ps);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);
        ticketDAO.saveTicket(enterticket);

        assertEquals(true, ticketDAO.searchRecUser("ABCDE"));

    }

    @Test
    public void searchRecUserfalse() {

        ParkingSpot ps = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime outTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setOutTime(outTime);
        enterticket.setParkingSpot(ps);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);
        assertEquals(false, ticketDAO.searchRecUser("ABCDE"));
    }
}
