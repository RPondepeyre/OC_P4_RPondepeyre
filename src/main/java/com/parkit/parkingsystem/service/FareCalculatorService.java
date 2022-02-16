package com.parkit.parkingsystem.service;

import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double durationinSeconds = ChronoUnit.SECONDS.between(ticket.getInTime(), ticket.getOutTime());
        double duration = durationinSeconds/(60*60);
        double price;

        // Free Time ( in Hours )
        double freeTime = 0.5;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (duration <= freeTime){ 
                    price = 0;
                }else{
                price = (duration * Fare.CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE: {
                if (duration <= freeTime){ 
                price = 0;
                }else{
                price = (duration * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        if (ticket.getRecuser()) {
            price = price - ((price * 5) / 100);
        }
        ticket.setPrice(price);
    }
}