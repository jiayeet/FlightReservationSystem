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
public class FleetManagerModule {
    
    public FleetManagerModule() 
    {
    }
    
    public void fleetManagerMenu() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Reservation System :: Fleet Manager ***\n");
            System.out.println("1: Create Aircraft Configurations");
            System.out.println("2: View All Aircraft Configurations");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewAircraftConfiguration();
                }
                else if(response == 2)
                {
                    doViewAllAircraftConfigurations();
                }
                else if(response == 3)
                {
                    doViewAircraftConfigurationDetails();
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
    
    public void doCreateNewAircraftConfiguration() {
        System.out.println("New configuration created!");
    }
    
    public void doViewAllAircraftConfigurations() {
        System.out.println("Viewing all aircraft configurations");
    }
    
    public void doViewAircraftConfigurationDetails() {
        System.out.println("Viewing a specific aircraft configuration detail");
    }
}
