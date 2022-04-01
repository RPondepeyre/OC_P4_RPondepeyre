package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");


    public Connection getConnection() throws ClassNotFoundException, SQLException {
            String port = "";
            String username ="";
            String password = "";
            String timezone = "";
        
        try{
            Properties prop = new Properties();
            prop.load(new FileInputStream(("src/main/resources/Data.properties")));
            port = prop.getProperty("port");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            timezone = prop.getProperty("timezone");



        }catch(Exception e){
            logger.error("Error while reading database properties", e);
        }
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:"+port+"/prod?serverTimezone="+timezone, username, password);
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
