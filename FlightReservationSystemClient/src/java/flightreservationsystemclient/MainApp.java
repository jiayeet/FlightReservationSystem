/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
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
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    
    private SalesManagerModule salesManagerModule;
    private FleetManagerModule fleetManagerModule;
    private ScheduleManagerModule scheduleManagerModule;
    private RoutePlannerModule routePlannerModule;

    private Employee currentEmployee;
    
    public MainApp() {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
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
                        salesManagerModule = new SalesManagerModule();
                        routePlannerModule = new RoutePlannerModule();
                        scheduleManagerModule = new ScheduleManagerModule();
                        
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
            System.out.println("testing to see if the method calling makes sense !");
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
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.ROUTEPLANNER) {
                routePlannerModule.routePlannerMenu();
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.SCHEDULEMANAGER) {
                scheduleManagerModule.scheduleManagerMenu();
            } else if (currentEmployee.getUserRole() == EmployeeUserRoleEnum.SYSTEMADMINISTRATOR) {
                salesManagerModule.salesManagerMenu();
            } else {
                System.out.println("There is no such user role!");
            }

        }
    }
}
