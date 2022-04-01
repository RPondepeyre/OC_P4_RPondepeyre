package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import org.apache.commons.lang.ObjectUtils.Null;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.altindag.log.LogCaptor;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @Mock
    private static DataBaseConfig dataBaseConfig;
    @Mock
    private static Connection con;
    @Mock
    private static PreparedStatement ps;
    @Mock
    private static ResultSet rs;
    @Mock
    private static Timestamp ts;

    private static LogCaptor logCaptor;

    private static TicketDAO ticketDAO;
    Ticket enterticket = new Ticket();


    @BeforeEach
    private void setUpPerTest() throws SQLException, ClassNotFoundException {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);

        logCaptor = LogCaptor.forName("TicketDAO");
        logCaptor.setLogLevelToInfo();
    }

    @Test
    public void TicketenterTest() throws SQLException {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setParkingSpot(parkingSpot);
        enterticket.setVehicleRegNumber("ABCDE");

        ticketDAO.saveTicket(enterticket);
        verify(ps, Mockito.times(1)).execute();

    }

    @Test
    public void TicketEnterWithOutTimeTest() throws SQLException{

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setParkingSpot(parkingSpot);
        enterticket.setVehicleRegNumber("ABCDE");
        enterticket.setOutTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        enterticket.setPrice(10);
        
        ticketDAO.saveTicket(enterticket);
        verify(ps, Mockito.times(1)).execute();
    }

    @Test
    public void TicketEnterLogTest() throws SQLException{
        when (con.prepareStatement(anyString())).thenThrow(SQLException.class);
        ticketDAO.saveTicket(enterticket);
        assertThat(logCaptor.getErrorLogs()).containsExactly("Error fetching next available slot");
    }

    @Test
    public void TicketExitTest() throws SQLException {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        LocalDateTime intime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        enterticket.setId(1);
        enterticket.setInTime(intime);
        enterticket.setParkingSpot(parkingSpot);
        enterticket.setVehicleRegNumber("ABCDE");
        enterticket.setOutTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        enterticket.setPrice(10);

        ticketDAO.updateTicket(enterticket);
        verify(ps, Mockito.times(1)).execute();

    }
    @Test
    public void TicketExitLogTest() throws SQLException{
        when (con.prepareStatement(anyString())).thenThrow(SQLException.class);
        ticketDAO.updateTicket(enterticket);
        assertThat(logCaptor.getErrorLogs()).containsExactly("Error saving ticket info");
    }

    @Test
    public void GetTicket() throws SQLException{
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(6)).thenReturn("CAR");
        when(rs.getDouble(3)).thenReturn(15.00);
        when(rs.getTimestamp(anyInt())).thenReturn(ts);
       Ticket ticket = ticketDAO.getTicket("Ticket");
        assertNotNull(ticket);
        assertEquals("Ticket", ticket.getVehicleRegNumber());
        assertEquals(15.00, ticket.getPrice());
    }

    @Test
    public void GetTicketNoOutTime() throws SQLException{
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(6)).thenReturn("CAR");
        when(rs.getDouble(3)).thenReturn(15.00);
        when(rs.getTimestamp(4)).thenReturn(ts);
       Ticket ticket = ticketDAO.getTicket("Ticket");

        assertEquals(null, ticket.getOutTime());
    }
    @Test
    public void GetTicketdontexist() throws SQLException {
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        Ticket ticket = ticketDAO.getTicket("TICKET");
        assertEquals(null, ticket);

    }

    @Test
    public void GetTicketlogTest() throws SQLException {
        when(ps.executeQuery()).thenThrow(SQLException.class);
        Ticket ticket = ticketDAO.getTicket("TICKET");
        assertThat(logCaptor.getErrorLogs()).containsExactly("Error fetching next available slot");

    }

    @Test
    public void searchRecUsertrue() throws SQLException {

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        boolean result = ticketDAO.searchRecUser("vehicleRegNumber");

        assertEquals(true, result);



    }

    @Test
    public void searchRecUserfalse() throws SQLException {

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        boolean result = ticketDAO.searchRecUser("vehicleRegNumber");

        assertEquals(false, result);
    }

    @Test
    public void searchRecUserlogTest() throws SQLException {

        when(ps.executeQuery()).thenThrow(SQLException.class);
        boolean result = ticketDAO.searchRecUser("vehicleRegNumber");
        assertThat(logCaptor.getErrorLogs()).containsExactly("Error searching recurrent user");
    }


}
