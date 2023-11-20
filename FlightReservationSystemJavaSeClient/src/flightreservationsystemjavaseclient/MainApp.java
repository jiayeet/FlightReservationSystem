/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemjavaseclient;

import java.util.Scanner;
import ws.partner.InvalidLoginCredentials_Exception;
import ws.partner.Partner;
import ws.partner.PartnerWebService;
import ws.partner.PartnerWebService_Service;

/**
 *
 * @author 65968
 */
public class MainApp {
    
    MainApp() {
        
    }
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response;
        
        while(true)
        {
            System.out.println("*** Holiday Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Flight");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    } catch (InvalidLoginCredentials_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    searchFlight();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.print("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    

    private void doLogin() throws InvalidLoginCredentials_Exception
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Login ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        try
        {
            if(username.length() > 0 && password.length() > 0) {
                PartnerWebService_Service service = new PartnerWebService_Service();
                PartnerWebService port = service.getPartnerWebServicePort();
                Partner partner = port.partnerLogin(username, password);
            }

        }
        catch (InvalidLoginCredentials_Exception ex)
        {
            System.out.println("An error has occurred: " + ex.getMessage());
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Holiday Reservation System :: Main Menu ***\n");
        
            System.out.println("1: Search Flight");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: View Flight Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    searchFlight();
                } else if (response == 2) {
                    viewFlightReservations();
                } else if (response == 3) {
                    viewFlightReservationDetails();
                } else if (response == 4) {
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
        System.out.println("Search flight has not been implemeneted!");
    }

    private void viewFlightReservations() {
        System.out.println("View flight reservations has not been implemented!");
    }

    private void viewFlightReservationDetails() {
        System.out.println("View flight reservation details has not been implemented!");
    }
}
