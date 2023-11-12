/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import java.util.Scanner;

/**
 *
 * @author 65968
 */
public class RoutePlannerModule {
    
    public RoutePlannerModule() {
        
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
    }
    
    private void doViewAllFlightRoutes() {
    }
    
    private void deleteFlightRoute() {
    }
}
