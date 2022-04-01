package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import nl.altindag.log.LogCaptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    @Mock
    private static DataBaseConfig dataBaseConfig;
    @Mock
    private static Connection con;
    @Mock
    private static PreparedStatement ps;
    @Mock
    private static ResultSet rs;

    //Class to be tested
    private static ParkingSpotDAO parkingSpotDAO;

    private static LogCaptor logCaptor;

    @BeforeEach
    private void setUpPerTest() throws ClassNotFoundException, SQLException {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseConfig;
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);

        logCaptor = LogCaptor.forName("ParkingSpotDAO");
        logCaptor.setLogLevelToInfo();

    }

    @Test
    public void NextAvailabeSlotTestCAR() throws SQLException {
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(anyInt())).thenReturn(1);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(1, result);

    }

    @Test
    public void NextAvailabeSlotTestBIKE() throws SQLException {
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(anyInt())).thenReturn(4);
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        assertEquals(4, result);

    }
    @Test
    public void NotAvalaibleSlotTest() throws SQLException{
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(-1, result);

    }

    @Test
    public void GetNextAvalaibleLogtest() throws SQLException{
        
        //when(ps.executeQuery()).thenReturn(rs);
        //when(rs.next()).thenReturn(false);
        when(ps.executeQuery()).thenThrow(SQLException.class);
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertThat(logCaptor.getErrorLogs()).containsExactly("Error fetching next available slot");

       
    }

    @Test
    public void updateParkingTestNotAvailaible() throws SQLException {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        verify(ps, Mockito.times(1)).executeUpdate();


    }

    @Test
    public void updateParkingTestAvailaible() throws SQLException {

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot);
        verify(ps, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void updateParkingLogTest() throws SQLException {
        
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot);
        
        assertThat(logCaptor.getErrorLogs()).containsExactly("Error updating parking info");
        

    }

}
