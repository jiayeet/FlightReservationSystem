/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClassType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CreateNewAircraftConfigurationException;

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
        System.out.print("Enter Maximum Capacity of the Aircraft Configuration> ");
        int maximumCapacity = scanner.nextInt();
        newAircraftConfiguration.setMaximumCapacity(maximumCapacity);
        
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
        
        
        int cabinsCapacity = 0;
        
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
            System.out.print("Enter Maximum Seating Capacity> ");
            newCabinClass.setMaxCapacity(scanner.nextInt()); 
            cabinsCapacity += newCabinClass.getMaxCapacity();
            
            // TODO - Insert Bean Validation try-catch block here
            
            if (cabinsCapacity <= maximumCapacity) {
                newAircraftConfiguration.getCabinClasses().add(newCabinClass);
            } else {
                System.out.println("Invalid option, please try again! The new cabin class seat capacity exceeds the capacity of the aircraft configuration\n");
            }
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
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Reservation System :: Fleet Manager :: View All Aircraft Configurations ***\n");
        
        List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfigurations();
        System.out.printf("%25s%20s%30s%25s\n", "Aircraft Configuration ID", "Aircraft Type Name", "Configuration Name", "Number of Cabin Class");
        
        for (AircraftConfiguration aircraftConfiguration : aircraftConfigurations) 
        {
            System.out.printf("%25s%20s%30s%25s\n", aircraftConfiguration.getAircraftConfigurationId().toString(), aircraftConfiguration.getAircraftType().getName(), 
                    aircraftConfiguration.getName(), aircraftConfiguration.getNumOfCabinClass());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    public void doViewAircraftConfigurationDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Reservation System :: Fleet Manager :: View Aircraft Configuration Details ***\n");
        System.out.print("Enter Aircraft Configuration ID> ");
        Long aircraftConfigurationId = scanner.nextLong();
        
        try
        {
            AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(aircraftConfigurationId);  
            System.out.printf("%25s%20s%30s%25s\n", "Aircraft Configuration ID", "Aircraft Type Name", "Configuration Name", "Number of Cabin Class");
            System.out.printf("%25s%20s%30s%25s\n", aircraftConfiguration.getAircraftConfigurationId().toString(), aircraftConfiguration.getAircraftType().getName(), 
                    aircraftConfiguration.getName(), aircraftConfiguration.getNumOfCabinClass());
            
            System.out.printf("%16s%22s%18s%30s%35s\n", "Cabin Class Type", "Number of Aisles", "Number of Rows", "Number of Seats Abreast", "Seat Configuration Per Column");
            
            for (CabinClass cabinClass : aircraftConfiguration.getCabinClasses()) 
            {
                System.out.printf("%16s%22s%18s%30s%35s\n", cabinClass.getCabinClassType().toString(), cabinClass.getNumOfAisles(), cabinClass.getNumOfRows(), 
                        cabinClass.getNumOfSeatsAbreast(), cabinClass.getSeatConfiguration());
            }
        }
        catch(AircraftConfigurationNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving Aircraft Configuration: " + ex.getMessage() + "\n");
        }
        
    }
}
