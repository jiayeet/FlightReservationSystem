/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import java.util.Scanner;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author 65968
 */
public class RoutePlannerModule {
    
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    
    public RoutePlannerModule() {
        
    }
    
    public RoutePlannerModule(AirportSessionBeanRemote airportSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) {
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
    }
    
    public void routePlannerMenu() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Reservation System :: Route Planner***\n");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewFlightRoute();
                }
                else if(response == 2)
                {
                    doViewAllFlightRoutes();
                }
                else if(response == 3)
                {
                    deleteFlightRoute();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    private void doCreateNewFlightRoute() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        FlightRoute newFlightRoute = new FlightRoute();
        String input = "";
        
        System.out.println("*** Flight Reservation System :: Route Planner :: Create New Flight Route ***\n");
        
        try {
        System.out.print("Enter Origin Airport IATA Code> ");
        Airport originAirport = airportSessionBeanRemote.retrieveAirportByIATACode(scanner.nextLine().trim());
        newFlightRoute.setAirportOrigin(originAirport);
        System.out.print("Enter Destination Airport IATA Code> ");
        Airport destinationAirport = airportSessionBeanRemote.retrieveAirportByIATACode(scanner.nextLine().trim());
        newFlightRoute.setAirportDestination(destinationAirport);
        newFlightRoute.setEnabled(Boolean.TRUE);
        newFlightRoute.setIsMain(Boolean.TRUE);
        
        Long newFlightRouteId = flightRouteSessionBeanRemote.createNewFlightRoute(newFlightRoute);
        System.out.println("New flight route created successfully!: " + newFlightRouteId + "\n");

        System.out.print("Would you like to create a complementary return flight route? (Y for yes) > ");
        
        FlightRoute flightRouteToBeUpdated = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(newFlightRouteId);
        input = scanner.nextLine().trim();
        
        if(input.equals("Y")) {
            FlightRoute complementaryFlightRoute = new FlightRoute();
            complementaryFlightRoute.setAirportOrigin(destinationAirport);
            complementaryFlightRoute.setAirportDestination(originAirport);
            complementaryFlightRoute.setEnabled(Boolean.TRUE);
            complementaryFlightRoute.setIsMain(Boolean.FALSE);
            complementaryFlightRoute.setComplementaryFlightRoute(flightRouteToBeUpdated);
            
            Long complementaryFlightRouteId = flightRouteSessionBeanRemote.createNewFlightRoute(complementaryFlightRoute);
            System.out.println("Complementary flight route created successfully!: " + complementaryFlightRouteId + "\n");
            
                FlightRoute createdComplementaryFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(complementaryFlightRouteId);
                flightRouteToBeUpdated.setComplementaryFlightRoute(createdComplementaryFlightRoute);
                flightRouteSessionBeanRemote.updateFlightRoute(flightRouteToBeUpdated);
        }
        }
        catch (AirportNotFoundException ex) {
            System.out.println("An error has occurred while creating the new flight route: The airport does not exist!\n");
        } 
        catch (FlightRouteExistException ex) {
            System.out.println("An error has occurred while creating the new flight route: The flight route already exists!\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred while updating the main flight route: Flight route cannot be found!");
        }
        catch (GeneralException ex) {
            System.out.println("An unknown error has occurred while creating the new flight route!: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doViewAllFlightRoutes() {
        Scanner scanner = new Scanner(System.in);
        
        List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();

        if (!flightRoutes.isEmpty()) {
            System.out.println("*** Flight Reservation System :: Route Planner :: View All Flight Routes ***\n");
            System.out.printf("%8s%20s%20s\n", "Flight Route ID", "Origin AITA Code ", "Destination AITA Code");
            for (FlightRoute flightRoute : flightRoutes) {
                if (flightRoute.getIsMain() == true) {
                    System.out.printf("%8s%20s%20s\n", flightRoute.getFlightRouteId(), flightRoute.getAirportOrigin().getIataAirportCode(), flightRoute.getAirportDestination().getIataAirportCode());
                    if(flightRoute.getComplementaryFlightRoute() != null) {
                        System.out.printf("%8s%20s%20s\n", flightRoute.getComplementaryFlightRoute().getFlightRouteId(), flightRoute.getComplementaryFlightRoute().getAirportOrigin().getIataAirportCode(), flightRoute.getComplementaryFlightRoute().getAirportDestination().getIataAirportCode());
                    }
                }
            }
        } else {
            System.out.println("There are no flight routes stored in the database to be printed!\n");
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void deleteFlightRoute() {
        Scanner scanner = new Scanner(System.in);        
        String input;
 
        
        System.out.println("*** Flight Reservation System :: Route Planner :: Delete Flight Route ***\n");
        
        try {
            System.out.print("Enter Origin Airport IATA Code> ");
            String origin = scanner.nextLine().trim();
            System.out.print("Enter Destination Airport IATA Code> ");
            String destination = scanner.nextLine().trim();
            FlightRoute flightRouteToRemove = flightRouteSessionBeanRemote.retrieveFlightRouteByDestinations(origin, destination);

            System.out.printf("Confirm Delete Flight Route of origin AITA %s and destination AITA %s (Flight Route ID: %d) (Enter 'Y' to Delete)> ", flightRouteToRemove.getAirportOrigin().getIataAirportCode(), flightRouteToRemove.getAirportDestination().getIataAirportCode(), flightRouteToRemove.getFlightRouteId());
            input = scanner.nextLine().trim();

            if (input.equals("Y")) {

                flightRouteSessionBeanRemote.deleteFlightRoute(flightRouteToRemove.getFlightRouteId());
                System.out.println("Flight Route deleted successfully!\n");

            } else {
                System.out.println("Flight Route NOT deleted!\n");
            }
        } catch (FlightRouteNotFoundException | DeleteFlightRouteException ex) {
            System.out.println("An error has occurred while deleting the flight route: " + ex.getMessage() + "\n");
        }
    }
}
