/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author 65968
 */
public class Main {

    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;
    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    @EJB
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, airportSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightRouteSessionBeanRemote, flightSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote, flightReservationSessionBeanRemote);
        mainApp.runApp();   
    }
}
