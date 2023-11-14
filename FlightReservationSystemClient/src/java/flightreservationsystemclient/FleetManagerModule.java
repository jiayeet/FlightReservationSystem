/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.CabinClassType;
import util.enumeration.EmployeeUserRoleEnum;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CreateNewAircraftConfigurationException;
import util.exception.InvalidUserRoleException;

/**
 *
 * @author 65968
 */
public class FleetManagerModule {
    
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    
    
    public FleetManagerModule() 
    {
    }
    
    public FleetManagerModule(AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote) 
    {
        this();
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
    }
    
    
    public void fleetManagerMenu()
    {
        
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
        Scanner scanner = new Scanner(System.in);
        AircraftConfiguration newAircraftConfiguration = new AircraftConfiguration();
        
        System.out.println("*** Flight Reservation System :: Fleet Manager :: Create New Aircraft Configuration ***\n");
        System.out.print("Enter Aircraft Configuration Name> ");
        newAircraftConfiguration.setName(scanner.nextLine().trim());
        Integer numOfCabinClass = 0;
        
        do 
        {
            System.out.print("Enter Number of Cabin Classes (1 to 4)> ");
            numOfCabinClass = scanner.nextInt();
            
            if (numOfCabinClass > 0 && numOfCabinClass < 5)
            {
                newAircraftConfiguration.setNumOfCabinClass(numOfCabinClass);
            }
            else
            {
                System.out.println("Invalid Number of Cabin Classes!\n");
            }
            
            scanner.nextLine().trim();
        } while(numOfCabinClass < 1 || numOfCabinClass > 4);
        
        for (int i = 1; i <= numOfCabinClass; i++)
        {
            CabinClass newCabinClass = new CabinClass();
            
            System.out.println("Cabin Class " + i);
            
            while(true)
            {
                System.out.print("Select Cabin Class Type (1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class)> ");
                Integer cabinClassTypeInt = scanner.nextInt();
                
                if (cabinClassTypeInt >= 1 && cabinClassTypeInt <= 4) 
                {
                    newCabinClass.setCabinClassType(CabinClassType.values()[cabinClassTypeInt-1]);
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            // TODO - Bean Validation for each Cabin Class?
            System.out.print("Enter Number of Aisles (0 to 2)> ");
            newCabinClass.setNumOfAisles(scanner.nextInt());
            System.out.print("Enter Number of Rows> ");
            newCabinClass.setNumOfRows(scanner.nextInt());
            System.out.print("Enter Number of Seats Abreast> ");
            newCabinClass.setNumOfSeatsAbreast(scanner.nextInt());
            scanner.nextLine();
            System.out.print("Enter Seating Configuration per Column> ");
            newCabinClass.setSeatConfiguration(scanner.nextLine().trim());
            
            // TODO - Insert Bean Validation try-catch block here
            
            newAircraftConfiguration.getCabinClasses().add(newCabinClass);
            
        }
        
        try
        {
            newAircraftConfiguration = aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(newAircraftConfiguration);
            
            System.out.println("New Aircraft Configuration created successfully!: " + newAircraftConfiguration.getAircraftConfigurationId() + "\n");
        }
        catch(AircraftTypeNotFoundException ex) 
        {
            System.out.println("An error has occurred while creating the new aircraft configuration!: Invalid Aircraft Configuration Name\n");
        }
        catch(AircraftTypeMaxSeatCapacityExceededException ex)
        {
            System.out.println("An error has occurred while creating the new aircraft configuration!: " + ex.getMessage() + "\n");
        }
        catch(CreateNewAircraftConfigurationException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
        
    }
    
    public void doViewAllAircraftConfigurations() {
        System.out.println("Viewing all aircraft configurations");
    }
    
    public void doViewAircraftConfigurationDetails() {
        System.out.println("Viewing a specific aircraft configuration detail");
    }
}
