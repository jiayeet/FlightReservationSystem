/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import java.util.Scanner;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightException;

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
            System.out.println("*** Flight Management System :: Route Planner***\n");
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
        
        System.out.println("*** Flight Management System :: Schedule Manager :: Create New Flight ***\n");
        
        try {
        System.out.print("Enter Aircraft Configuration Name> ");
        //AircraftConfiguration aircraftConfig = aircraftConfigurationSessionBeanRemote.retrieveAirportByAirportName(scanner.nextLine().trim());
        //newFlight.setAircraftConfiguration(aircraftConfig);
        System.out.print("Enter Flight Route Id> ");
        FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(scanner.nextLong());
        //newFlight.setFlightRoute(flightRoute);
        
        //add in schedule plan
    
        Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight);
        System.out.println("New flight created successfully!: " + newFlightId + "\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred while creating the new flight route: The airport does not exist!\n");
        }
        catch (FlightExistException ex) {
            System.out.println("An error has occurred while creating the new flight route: The airport does not exist!\n");
        } 
        catch (GeneralException ex) {
            System.out.println("An unknown error has occurred while registering the new customer!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlights() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View All Flights ***\n");
        
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        System.out.printf("%8s%20s%20s\n", "Flight ID", "Origin AITA Code ", "Destination AITA Code");

        for(Flight flight:flights)
        {
            System.out.printf("%8s%20s%20s\n", flight.getFlightId(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doViewFlightDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details ***\n");
        System.out.print("Enter Flight ID> ");
        Long flightId = scanner.nextLong();
        
        try
        {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightId(flightId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Flight ID", "AITA Origin Code", "AITA Destination Code", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());         
            System.out.println("------------------------");
            System.out.println("1: Update Staff");
            System.out.println("2: Delete Staff");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateFlight(flight);
            }
            else if(response == 2)
            {
                doDeleteFlight(flight);
            }
        }
        catch(FlightNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Update Flight ***\n");
        System.out.print("Enter Flight Route ID (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        try {
            if (input.length() > 0) {
                FlightRoute newFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(Long.valueOf(input));
                flight.setFlightRoute(newFlightRoute);
            }

            System.out.print("Enter Aircraft Configuration ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                //AircraftConfiguration newAircraftConfiguration = flightRouteSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(input));
                //flight.setAircraftConfiguration(newAircraftConfiguration);
            }

            System.out.print("Enter Flight Schedule Plan ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                //FlightSchedulePlan newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanID(Long.valueOf(input));
                //flight.setFlightSchedulePlan(newFlightSchedulePlan);
            }

            flightSessionBeanRemote.updateFlight(flight);
            System.out.println("Flight updated successfully!\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        /*catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }*/
        catch (FlightNotFoundException | UpdateFlightException ex) {
            System.out.println("An error has occurred while updating flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteFlight(Flight flightToRemove) {
        Scanner scanner = new Scanner(System.in);        
        String input;
 
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Delete Flight Route ***\n");
        System.out.printf("Confirm Delete Flight of origin AITA %s and destination AITA %s (Flight Route ID: %d) (Enter 'Y' to Delete)> ", flightToRemove.getFlightRoute().getAirportOrigin().getAirportName(), flightToRemove.getFlightRoute().getAirportDestination().getIataAirportCode(), flightToRemove.getFlightId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                flightSessionBeanRemote.deleteFlight(flightToRemove.getFlightId());
                System.out.println("Flight deleted successfully!\n");
            }
            catch (FlightNotFoundException| DeleteFlightException ex) 
            {
                System.out.println("An error has occurred while deleting the flight: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Flight NOT deleted!\n");
        }
    }
    
    private void createFlightSchedulePlan() {
        
    }
    
    private void doViewAllFlightSchedulePlans() {
        
    }
    
    private void doViewFlightSchedulePlanDetails() {
        
    }
}
