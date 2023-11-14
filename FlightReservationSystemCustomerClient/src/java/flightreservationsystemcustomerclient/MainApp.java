/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemcustomerclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentials;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private Customer currentCustomer;
    
    MainApp(CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.currentCustomer = new Customer();
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
        System.out.println("*** Welcome to Flight Reservation System ***\n");
        System.out.println("1: Register As Customer");
        System.out.println("2: Login");
        System.out.println("3: Exit\n");
        response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    registerAsCustomer();
                }
                else if (response == 2) {
                    try {
                        doCustomerLogin();
                        System.out.println("Login successful!\n");

                        menuMain();
                    } catch (InvalidLoginCredentials ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }
    
    private void registerAsCustomer() {
        Scanner scanner = new Scanner(System.in);
        Customer newCustomer = new Customer();

        System.out.println("*** Flight Reservation System :: Create Customer ***\n\n");
        System.out.print("Enter First Name> ");
        newCustomer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newCustomer.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Email> ");
        newCustomer.setEmail(scanner.nextLine().trim());
        System.out.print("Enter Mobile Phone Number> ");
        newCustomer.setMobileNumber(scanner.nextLine().trim());
        System.out.print("Enter Address> ");
        newCustomer.setAddress(scanner.nextLine().trim());

        System.out.print("Enter Username> ");
        newCustomer.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newCustomer.setPassword(scanner.nextLine().trim());
        
        System.out.println(newCustomer.getAddress() + newCustomer.getEmail() + newCustomer.getFirstName() + newCustomer.getLastName() + newCustomer.getUsername() + newCustomer.getPassword() + newCustomer.getMobileNumber());

        try {
            Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
            System.out.println("New customer created successfully!: " + newCustomerId + "\n");
        }
        catch (CustomerUsernameExistException ex) {
            System.out.println("An error has occurred while registering the a new customer account: The username already exists!\n");
        } 
        catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while registering the new customer!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doCustomerLogin() throws InvalidLoginCredentials {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Flight Reservation System :: Customer Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new InvalidLoginCredentials("Missing login credential!");
        }
    }
    
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
        System.out.println("*** Welcome to Flight Reservation System " + currentCustomer.getFirstName() + " :: ***\n");
        System.out.println("1: Search Flight");
        System.out.println("2: View My Flight Reservations");
        System.out.println("3: View My Flight Reservations Details");
        System.out.println("4: Logout\n");
        response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchFlight();
                }
                else if (response == 2) {
                    viewMyFlightReservations();
                } 
                else if (response == 3) {
                    viewMyFlightReservationDetails();
                }
                else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }
    
    private void searchFlight() {
        
    }
    
    private void viewMyFlightReservations() {
        
    }
    
    private void viewMyFlightReservationDetails() {
        
    }
    
    
}
