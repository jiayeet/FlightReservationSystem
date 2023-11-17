/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightreservationsystemcustomerclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightTicketSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author 65968
 */
public class Main {

    @EJB(name = "FlightTicketSessionBeanRemote")
    private static FlightTicketSessionBeanRemote flightTicketSessionBeanRemote;
    @EJB(name = "FlightReservationSessionBeanRemote")
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    @EJB(name = "PassengerSessionBeanRemote")
    private static PassengerSessionBeanRemote passengerSessionBeanRemote;
    @EJB(name = "CustomerSessionBeanRemote")
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, passengerSessionBeanRemote, flightReservationSessionBeanRemote, flightTicketSessionBeanRemote);
        mainApp.runApp();  
    }
    
}
