/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightreservationsystemcustomerclient;

import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightTicketSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author 65968
 */
public class Main {

    @EJB
    private static CreditCardSessionBeanRemote creditCardSessionBeanRemote;  
    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    @EJB
    private static FlightTicketSessionBeanRemote flightTicketSessionBeanRemote;
    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    @EJB
    private static PassengerSessionBeanRemote passengerSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(passengerSessionBeanRemote == null){
            System.out.println("test");
        }
        MainApp mainApp = new MainApp(customerSessionBeanRemote, passengerSessionBeanRemote, flightReservationSessionBeanRemote, flightTicketSessionBeanRemote, flightSchedulePlanSessionBeanRemote, creditCardSessionBeanRemote);
        mainApp.runApp();  
    }
    
}
