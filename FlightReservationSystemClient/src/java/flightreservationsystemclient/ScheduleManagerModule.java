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
public class ScheduleManagerModule {
    
    public ScheduleManagerModule() {
        
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
