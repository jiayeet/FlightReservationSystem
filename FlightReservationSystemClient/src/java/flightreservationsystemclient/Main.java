/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import entity.Airport;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author 65968
 */
public class Main {

    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //test again
        List<Airport> retrieveAllAirports = airportSessionBeanRemote.retrieveAllAirports();
        
        for(Airport airport: retrieveAllAirports)
        {
            System.out.println(airport.getAirportName() + ", " + airport.getIataAirportCode() + ", " + airport.getCity() + ", " + airport.getCountry() + ", " + airport.getState());
        }
    }
    
}
