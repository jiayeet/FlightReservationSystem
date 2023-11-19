/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.List;
import java.util.Scanner;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author 65968
 */
public class SalesManagerModule {
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    
    SalesManagerModule(FlightSessionBeanRemote flightSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
    }
    
    public void salesManagerMenu() {       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Reservation System :: Route Planner***\n");
            System.out.println("1: View Seats Inventory");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewSeatsInventory();
                }
                else if(response == 2)
                {
                    doViewFlightReservations();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    
    private void doViewSeatsInventory() {
    }
    
    private void doViewFlightReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Reservation System :: Sales Manager :: View Flight Reservations ***\n");
        
        try {
        System.out.println("Please input a flight number> ");
        Flight inputFlight = flightSessionBeanRemote.retrieveFlightByFlightId(Long.valueOf(scanner.nextLine().trim()));
        
        System.out.println("Please input the id of a flight schedule> ");
        FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleByFlightScheduleId(Long.valueOf(scanner.nextLine().trim()));
        
        List<FlightReservation> inBoundFlightReservations = flightSchedule.getInBoundFlightReservations();
        List<FlightReservation> outBoundFlightReservations = flightSchedule.getOutBoundFlightReservations();
        System.out.printf("%8s\n", "Flight Reservation ID Inbound");

        for(FlightReservation flightReservation:inBoundFlightReservations)
        {
            System.out.printf("%8s\n", flightReservation.getFlightReservationId());
        }
        
        System.out.printf("%8s\n", "Flight Reservation ID outbound");
        for(FlightReservation flightReservation:outBoundFlightReservations)
        {
            System.out.printf("%8s\n", flightReservation.getFlightReservationId());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
        }
        catch (FlightNotFoundException ex){
            System.out.println("An error has occurred: The flight route is not found!\n");
        }
        catch (FlightScheduleNotFoundException ex){
            System.out.println("An error has occurred: The flight schedule is not found!\n");
        }
    }

}
