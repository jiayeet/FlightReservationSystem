/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeUserRoleEnum;
import util.exception.InvalidLoginCredentials;

/**
 *
 * @author 65968
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    
    private SalesManagerModule salesManagerModule;
    private FleetManagerModule fleetManagerModule;
    private ScheduleManagerModule scheduleManagerModule;
    private RoutePlannerModule routePlannerModule;

    private Employee currentEmployee;
    
    public MainApp() {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
    }
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
        System.out.println("*** Welcome to Flight Reservation System ***\n");
        System.out.println("1: Login");
        System.out.println("2: Exit\n");
        response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");

                        fleetManagerModule = new FleetManagerModule(aircraftConfigurationSessionBeanRemote);
                        salesManagerModule = new SalesManagerModule(flightSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightReservationSessionBeanRemote, flightScheduleSessionBeanRemote);
                        routePlannerModule = new RoutePlannerModule(airportSessionBeanRemote, flightRouteSessionBeanRemote);
                        scheduleManagerModule = new ScheduleManagerModule(flightRouteSessionBeanRemote, flightSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightScheduleSessionBeanRemote, flightSchedulePlanSessionBeanRemote);
                        
                        menuMain();
                    } catch (InvalidLoginCredentials ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }
        
    private void doLogin() throws InvalidLoginCredentials 
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Flight Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentials("Missing login credential!");
        }
    }
    
        private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Reservation System ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getUserRole().toString() + " rights\n");
            
            if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.SYSTEMADMINISTRATOR) {
                System.out.println("1: Fleet Manager");
                System.out.println("2: Route Planner");
                System.out.println("3: Schedule Manager");
                System.out.println("4: Sales Manager");
                System.out.println("5: Back\n");
                response = 0;

                while (response < 1 || response > 5) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        fleetManagerModule.fleetManagerMenu();
                    } else if (response == 2) {
                        routePlannerModule.routePlannerMenu();
                    } else if (response == 3) {
                        scheduleManagerModule.scheduleManagerMenu();
                    } else if (response == 4) {
                        salesManagerModule.salesManagerMenu();
                    } else if (response == 5) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.FLEETMANAGER) {
                fleetManagerModule.fleetManagerMenu();
                break;
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.ROUTEPLANNER) {
                routePlannerModule.routePlannerMenu();
                break;
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.SCHEDULEMANAGER) {
                scheduleManagerModule.scheduleManagerMenu();
                break;
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.SALESMANAGER) {
                salesManagerModule.salesManagerMenu();
                break;
            } else {
                System.out.println("There is no such user role!");
            }
            if (response == 5) {
            break;
            }
        }
        
    }
}
