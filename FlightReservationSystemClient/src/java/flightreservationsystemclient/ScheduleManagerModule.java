/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightException;

/**
 *
 * @author 65968
 */
public class ScheduleManagerModule {
    
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    
    public ScheduleManagerModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
                                 FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
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
        String flightNumber = "";
        String input = "";

        System.out.println("*** Flight Management System :: Schedule Manager :: Create New Flight ***\n");

            try {
            System.out.print("Enter Flight Number> ");
            flightNumber = scanner.nextLine().trim();
            newFlight.setFlightNumber(flightNumber);
            System.out.print("Enter Aircraft Configuration Id> ");
            AircraftConfiguration aircraftConfig = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(scanner.nextLine().trim()));
            newFlight.setAircraftConfiguration(aircraftConfig);

            System.out.print("Enter Flight Route Id> ");
            FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(Long.valueOf(scanner.nextLine().trim()));

            while (flightRoute.getEnabled() == false) {
                System.out.println("Flight route has been disabled, please input another flightRoute Id");
                System.out.print("Enter Flight Route Id> ");
                flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(Long.valueOf(scanner.nextLine().trim()));
            }

            newFlight.setFlightRoute(flightRoute);
            newFlight.setEnabled(Boolean.TRUE);
            newFlight.setIsMain(Boolean.TRUE);
            
            System.out.print("How many flight schedule plans would you like to add? ");
            response = scanner.nextInt();
            List<FlightSchedulePlan> flightSchedulePlans = new ArrayList<>();
            
            for (int i = 0; i < response; i++) {
                System.out.print("Enter Flight Schedule Plan Id> ");
                FlightSchedulePlan flightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanId(Long.valueOf(scanner.nextLine().trim()));
                flightSchedulePlans.add(flightSchedulePlan);
            }
            
            newFlight.setFlightSchedulePlans(flightSchedulePlans);
            
            Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight);
            System.out.println("New flight created successfully!: " + newFlightId + "\n");
            Flight mainFlight = flightSessionBeanRemote.retrieveFlightByFlightId(newFlightId);

            if (mainFlight.getFlightRoute().getComplementaryFlightRoute() != null) {
                System.out.print("Would you like to create a complementary return flight? (Y for yes) > ");
                input = scanner.nextLine().trim();
                FlightRoute complementaryFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(mainFlight.getFlightRoute().getComplementaryFlightRoute().getFlightRouteId());
                
                
                if (input.equals("Y")) {
                    Flight complementaryFlight = new Flight();
                    List<FlightSchedulePlan> complementaryFlightSchedulePlans = new ArrayList<>();

                    System.out.print("Enter Flight Number> ");
                    flightNumber = scanner.nextLine().trim();
                    complementaryFlight.setFlightNumber(flightNumber);
                    complementaryFlight.setAircraftConfiguration(aircraftConfig);
                    complementaryFlight.setFlightRoute(complementaryFlightRoute);
                    complementaryFlight.setEnabled(Boolean.TRUE);
                    complementaryFlight.setIsMain(Boolean.FALSE);
                    complementaryFlight.setComplementaryFlight(mainFlight);
                    
                    for(int i = 0; i < mainFlight.getFlightSchedulePlans().size(); i++) {
                        FlightSchedulePlan complementaryFlightSchedPlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanId(mainFlight.getFlightSchedulePlans().get(i).getFlightSchedulePlanId());
                        complementaryFlightSchedulePlans.add(complementaryFlightSchedPlan);
                    }
                    complementaryFlight.setFlightSchedulePlans(complementaryFlightSchedulePlans);

                    Long complementaryFlightId = flightSessionBeanRemote.createNewFlight(complementaryFlight);

                    System.out.println("Complementary flight created successfully!: " + complementaryFlightId + "\n");
                    
                    Flight createdComplementaryFlight = flightSessionBeanRemote.retrieveFlightByFlightId(complementaryFlightId);
                    
                    mainFlight.setComplementaryFlight(createdComplementaryFlight);
                    flightSessionBeanRemote.updateFlight(mainFlight);
                }
            }
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred: The flight route is not found!\n");
        }
        catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred: The aircraft configuration does not exist!\n");
        }
        catch (UpdateFlightException ex) {
            System.out.println("An error has occurred: The main flight cannot be updated!\n");
        }
        catch (FlightExistException ex) {
            System.out.println("An error has occurred: The flight already exists!\n");
        }
        catch (FlightNotFoundException ex) {
            System.out.println("An error has occurred: The flight cannot be found!\n");
        }
        catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println("An error has occurred: The flight schedule plancannot be found!\n");
        }
        catch (GeneralException ex) {
            System.out.println("An unknown error has occurred while creating the flight!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlights() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View All Flights ***\n");
        
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        System.out.printf("%8s%20s%20s%20s\n", "Flight ID", "Flight Number", "Origin AITA Code ", "Destination AITA Code");

        for(Flight flight:flights)
        {
            System.out.printf("%5s%20s%20s%20s\n", flight.getFlightId(), flight.getFlightNumber(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
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
            System.out.printf("%-15s%-25s%-25s\n", "Flight ID", "AITA Origin Code ", "AITA Destination Code");
            System.out.printf("%-15s%-25s%-25s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
            System.out.printf("%-15s%-25s\n", "Cabin Classes", "Number of Available Seats");
            for (int i = 0; i < flight.getAircraftConfiguration().getCabinClasses().size(); i++) {
                System.out.printf("%-15s%-25s\n", flight.getAircraftConfiguration().getCabinClasses().get(i).getCabinClassType().toString(), flight.getAircraftConfiguration().getCabinClasses().get(i).getMaxCapacity());
            }
            System.out.println("------------------------");
            System.out.println("1: Update Flight");
            System.out.println("2: Delete Flight");
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
            System.out.println("An error has occurred while retrieving flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Update Flight ***\n");
        
        try {

            System.out.print("Enter Aircraft Configuration ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                AircraftConfiguration newAircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(input));
                flight.setAircraftConfiguration(newAircraftConfiguration);
            }

            /*System.out.print("Enter Flight Schedule Plan ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                //FlightSchedulePlan newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanID(Long.valueOf(input));
                //flight.setFlightSchedulePlan(newFlightSchedulePlan);
            }*/

            flightSessionBeanRemote.updateFlight(flight);
            System.out.println("Flight updated successfully!\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        /*catch (FlightSchedulePlanNotFoundException ex) {
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
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Schedule Plan Details ***\n");
        System.out.print("Enter Flight Schedule Plan ID> ");
        Long flightSchedulePlanId = scanner.nextLong();
        
        try
        {
            FlightSchedulePlan flightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanId(flightSchedulePlanId);
            
            //TO CONFIGURE BASED ON BUSINESS RULES
            /*System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Flight ID", "AITA Origin Code", "AITA Destination Code");
            System.out.printf("%8s%20s%20s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Cabin Classes", "Number of Available Seats");
            for (int i = 0; i < flight.getAircraftConfiguration().getCabinClasses().size(); i++) {
                System.out.printf("%20s%20s\n", flight.getAircraftConfiguration().getCabinClasses().get(i).getCabinClassType().toString(), flight.getAircraftConfiguration().getCabinClasses().get(i).calculateTotalSeats());
            }*/
            
            System.out.println("------------------------");
            System.out.println("1: Update Flight Schedule Plan");
            System.out.println("2: Delete Flight Schedule Plan");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateFlightSchedulePlan(flightSchedulePlan);
            }
            else if(response == 2)
            {
                doDeleteFlightSchedulePlan(flightSchedulePlan);
            }
        }
        catch(FlightSchedulePlanNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving flight schedule plan: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
    }
    
    private void doDeleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
    }
}
