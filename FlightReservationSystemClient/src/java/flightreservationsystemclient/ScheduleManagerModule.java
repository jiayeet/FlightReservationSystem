/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Flight;
import entity.FlightRoute;
import java.util.Scanner;
import util.exception.FlightExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author 65968
 */
public class ScheduleManagerModule {
    
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    //private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    
    public ScheduleManagerModule() {
        
    }
    
    public ScheduleManagerModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote) {
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
    }

    public void scheduleManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Reservation System :: Route Planner***\n");
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Schedule Plans");
            System.out.println("6: View Flight Schedule Plan Details");
            System.out.println("7: Logout\n");
            response = 0;
            
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateFlight();
                }
                else if(response == 2)
                {
                    doViewAllFlights();
                }
                else if(response == 3)
                {
                    doViewFlightDetails();
                }
                else if(response == 4)
                {
                    createFlightSchedulePlan();
                }
                else if(response == 5)
                {
                    doViewAllFlightSchedulePlans();
                }
                else if(response == 6)
                {
                    doViewFlightSchedulePlanDetails();
                }
                else if (response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    
    private void doCreateFlight() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        Flight newFlight = new Flight();
        
        System.out.println("*** Flight Reservation System :: Schedule Manager :: Create New Flight ***\n");
        
        try {
        System.out.print("Enter Aircraft Configuration Name>");
        //AircraftConfiguration aircraftConfig = aircraftConfigurationSessionBeanRemote.retrieveAirportByAirportName(scanner.nextLine().trim());
        //newFlight.setAircraftConfiguration(aircraftConfig);
        System.out.print("Enter Flight Route Id>");
        FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(scanner.nextLong());
        //newFlight.setFlightRoute(flightRoute);
        
        //add in schedule plan
    
        Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight);
        System.out.println("New flight route created successfully!: " + newFlightId + "\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred while creating the new flight route: The airport does not exist!\n");
        } 
        catch (FlightExistException ex) {
            System.out.println("An error has occurred while creating the new flight route: The flight route already exists!\n");
        }
        catch (GeneralException ex) {
            System.out.println("An unknown error has occurred while registering the new customer!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlights() {
        
    }
    
    private void doViewFlightDetails() {
        
    }
    
    private void createFlightSchedulePlan() {
        
    }
    
    private void doViewAllFlightSchedulePlans() {
        
    }
    
    private void doViewFlightSchedulePlanDetails() {
        
    }
}
