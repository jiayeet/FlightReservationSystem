/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import util.enumeration.FlightScheduleType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.CreateNewFlightSchedulePlanException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.GeneralException;
import util.exception.UpdateFlightException;

/**
 *
 * @author 65968
 */
public class ScheduleManagerModule {
    
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    
    
    public ScheduleManagerModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
                                 FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote)
    {
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
    }

    public void scheduleManagerMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Management System :: Schedule Manager***\n");
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
    
    private void doCreateFlight()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        Flight newFlight = new Flight();
        
        System.out.println("*** Flight Management System :: Schedule Manager :: Create New Flight ***\n\n");
        
        try
        {
            System.out.print("Enter Aircraft Configuration Id> ");
            AircraftConfiguration aircraftConfig = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(scanner.nextLine().trim()));
            newFlight.setAircraftConfiguration(aircraftConfig);
            System.out.print("Enter Flight Route Id> ");
            FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(scanner.nextLong());
            newFlight.setFlightRoute(flightRoute);

            //add in schedule plan
            Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight);
            System.out.println("New flight created successfully!: " + newFlightId + "\n");
        }
        catch (FlightRouteNotFoundException ex)
        {
            System.out.println("An error has occurred: The flight route is not found!\n");
        }
        catch (AircraftConfigurationNotFoundException ex)
        {
            System.out.println("An error has occurred: The aircraft configuration does not exist!\n");
        }
        catch (FlightExistException ex)
        {
            System.out.println("An error has occurred: The flight already exists!\n");
        } 
        catch (GeneralException ex)
        {
            System.out.println("An unknown error has occurred while creating the flight!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlights()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View All Flights ***\n\n");
        
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        System.out.printf("%8s%20s%20s\n", "Flight ID", "Origin AITA Code ", "Destination AITA Code");

        for(Flight flight:flights)
        {
            System.out.printf("%8s%20s%20s\n", flight.getFlightId(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doViewFlightDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details ***\n\n");
        System.out.print("Enter Flight ID> ");
        Long flightId = scanner.nextLong();
        
        try
        {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightId(flightId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Flight ID", "AITA Origin Code", "AITA Destination Code");
            System.out.printf("%8s%20s%20s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Cabin Classes", "Number of Available Seats");
            for (int i = 0; i < flight.getAircraftConfiguration().getCabinClasses().size(); i++) {
                System.out.printf("%20s%20s\n", flight.getAircraftConfiguration().getCabinClasses().get(i).getCabinClassType().toString(), flight.getAircraftConfiguration().getCabinClasses().get(i).calculateTotalSeats());
            }
            System.out.println("------------------------");
            System.out.println("1: Update Flight");
            System.out.println("2: Delete Flight");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateFlight(flight);
            }
            else if(response == 2)
            {
                doDeleteFlight(flight);
            }
        }
        catch(FlightNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateFlight(Flight flight)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Update Flight ***\n\n");
        System.out.print("Enter Flight Route ID (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        try {
            if (input.length() > 0)
            {
                FlightRoute newFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(Long.valueOf(input));
                flight.setFlightRoute(newFlightRoute);
            }

            System.out.print("Enter Aircraft Configuration ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0)
            {
                AircraftConfiguration newAircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(input));
                flight.setAircraftConfiguration(newAircraftConfiguration);
            }

            System.out.print("Enter Flight Schedule Plan ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0)
            {
                //FlightSchedulePlan newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanID(Long.valueOf(input));
                //flight.setFlightSchedulePlan(newFlightSchedulePlan);
            }

            flightSessionBeanRemote.updateFlight(flight);
            System.out.println("Flight updated successfully!\n");
        }
        catch (FlightRouteNotFoundException ex)
        {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        catch (AircraftConfigurationNotFoundException ex)
        {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        /*catch (FlightSchedulePlanNotFoundException ex)
        {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }*/
        catch (FlightNotFoundException | UpdateFlightException ex)
        {
            System.out.println("An error has occurred while updating flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteFlight(Flight flightToRemove)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
 
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Delete Flight Route ***\n\n");
        System.out.printf("Confirm Delete Flight of origin AITA %s and destination AITA %s (Flight Route ID: %d) (Enter 'Y' to Delete)> ", flightToRemove.getFlightRoute().getAirportOrigin().getAirportName(), flightToRemove.getFlightRoute().getAirportDestination().getIataAirportCode(), flightToRemove.getFlightId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                flightSessionBeanRemote.deleteFlight(flightToRemove.getFlightId());
                System.out.println("Flight deleted successfully!\n");
            }
            catch (FlightNotFoundException| DeleteFlightException ex) 
            {
                System.out.println("An error has occurred while deleting the flight: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Flight NOT deleted!\n");
        }
    }
    
    private void createFlightSchedulePlan()
    {
        Scanner scanner = new Scanner(System.in);
        FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan();
        FlightSchedule newFlightSchedule = new FlightSchedule();
        
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("h:mm a");
        Date departureDate;
        Date departureTime;
        Date arrivalDateTime;
        String dayOfWeek;
        Date startDate;
        Date endDate;
        String[] flightDuration;
        int flightDurationHours = 0;
        int flightDurationMinutes = 0;
        String moreFlightSchedule = "";
        
        System.out.println("*** Flight Management System :: Schedule Manager :: Create Flight Schedule Plan ***\n\n");
        System.out.print("Enter Flight Number> ");
        
        try
        {
            String flightNumber = "";
            Flight flight;
            
            do {
                flightNumber = scanner.nextLine().trim();
                flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
                
                if (!flight.getEnabled())
                {
                    System.out.println("Indicated flight " + flightNumber + "has been disabled! No new flight schedules can be created!\n");
                }
                else
                {
                    newFlightSchedulePlan.setFlightNumber(flightNumber);
                    break;
                }
            } while(true);
            
            while(true)
            {
                System.out.println("Select Schedule Type (1: Single, 2: Multiple, 3: Recurrent every N Days, 4: Recurrent every Week)> ");
                Integer scheduleType = 0;
                scheduleType = scanner.nextInt();
                
                if (scheduleType >= 1 && scheduleType <= 4)
                {
                    newFlightSchedulePlan.setFlightScheduleType(FlightScheduleType.values()[scheduleType-1]);
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            scanner.nextLine();
            
            if (newFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.SINGLE || newFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.MULTIPLE)
            {
                do
                {
                    System.out.print("Enter Departure Date (dd/mmm/yy)> ");
                    departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                    System.out.print("Enter Departure Time (h:mm AM/PM)> ");
                    departureTime = inputTimeFormat.parse(scanner.nextLine().trim());
                    
                    departureDate.setHours(departureTime.getHours());
                    departureDate.setMinutes(departureTime.getMinutes());
                    newFlightSchedule.setDepartureDateTime(departureDate);
                
                    System.out.print("Enter Flight Duration (hh Hours mm Minutes)> ");
                    flightDuration = scanner.nextLine().trim().split("\\s+");
                    
                    // Extracting Flight Duration Hours and Minutes from User Input
                    for (int i = 0; i < flightDuration.length; i++)
                    {
                        if(flightDuration[i].equalsIgnoreCase("Hours") && i > 0)
                        {
                            newFlightSchedule.setFlightDurationHours(Integer.parseInt(flightDuration[i-1]));
                        }
                        else if(flightDuration[i].equalsIgnoreCase("Minutes") && i > 0)
                        {
                            newFlightSchedule.setFlightDurationMinutes(Integer.parseInt(flightDuration[i-1]));
                        }
                    }
                    
                    GregorianCalendar arrivalDateTimeCalendar = new GregorianCalendar();
                    arrivalDateTimeCalendar.setTime(newFlightSchedule.getDepartureDateTime());
                    arrivalDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, newFlightSchedule.getFlightDurationHours());
                    arrivalDateTimeCalendar.add(GregorianCalendar.MINUTE, newFlightSchedule.getFlightDurationMinutes());
                    newFlightSchedule.setArrivalDateTime(arrivalDateTimeCalendar.getTime());
                    
                    newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
                    
                    if (newFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.SINGLE)
                    {
                        break;
                    }
                    else
                    {
                        System.out.print("More Flight Schedule? (Enter 'N' to complete flight schedule creation)> ");
                        moreFlightSchedule = scanner.nextLine().trim();
                    }
                    
                    newFlightSchedule = new FlightSchedule();
                } while (!moreFlightSchedule.equals("N"));
                
            }
            else if(newFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTNDAY)
            {
                System.out.println("Enter NDay> ");
                newFlightSchedulePlan.setnDay(scanner.nextInt());
                
                System.out.print("Enter Departure Time (h:mm AM/PM)> ");
                departureTime = inputTimeFormat.parse(scanner.nextLine().trim());
                
                System.out.print("Enter Flight Schedule Plan Start Date (dd/mmm/yy)> ");
                startDate = inputDateFormat.parse(scanner.nextLine().trim());
                startDate.setHours(departureTime.getHours());
                startDate.setMinutes(departureTime.getMinutes());
                newFlightSchedulePlan.setStartDateTime(startDate);
                
                System.out.print("Enter Flight Schedule Plan End Date (dd/mmm/yy)> ");
                endDate = inputDateFormat.parse(scanner.nextLine().trim());
                newFlightSchedulePlan.setEndDate(endDate);
                
                System.out.print("Enter Flight Duration (hh Hours mm Minutes)> ");
                flightDuration = scanner.nextLine().trim().split("\\s+");
                
                // Extracting Flight Duration Hours and Minutes from User Input
                for (int i = 0; i < flightDuration.length; i++)
                {
                    if (flightDuration[i].equalsIgnoreCase("Hours") && i > 0)
                    {
                        flightDurationHours = Integer.parseInt(flightDuration[i-1]);
                    }
                    else if (flightDuration[i].equalsIgnoreCase("Minutes") && i > 0)
                    {
                        flightDurationMinutes = Integer.parseInt(flightDuration[i-1]);
                    }
                } 
                
                // Loop through and create new flight schedules every nDays between the startDate and endDate
                while (startDate.before(endDate))
                {
                    newFlightSchedule.setDepartureDateTime(startDate);
                    newFlightSchedule.setFlightDurationHours(flightDurationHours);
                    newFlightSchedule.setFlightDurationMinutes(flightDurationMinutes);
                    
                    GregorianCalendar arrivalDateTimeCalendar = new GregorianCalendar();
                    arrivalDateTimeCalendar.setTime(newFlightSchedule.getDepartureDateTime());
                    arrivalDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, newFlightSchedule.getFlightDurationHours());
                    arrivalDateTimeCalendar.add(GregorianCalendar.MINUTE, newFlightSchedule.getFlightDurationMinutes());
                    arrivalDateTime = arrivalDateTimeCalendar.getTime();
                    
                    if (arrivalDateTime.before(endDate))
                    {
                        newFlightSchedule.setArrivalDateTime(arrivalDateTime);
                        
                        // Associate newly created flight schedule and flight schedule plan
                        newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
                    }
                    
                    GregorianCalendar nextFlightScheduleStartDateCalendar = new GregorianCalendar();
                    nextFlightScheduleStartDateCalendar.setTime(startDate);
                    
                    // Add nDays to the current flight schedule start date to get the departureDateTime of the new flight schedule
                    nextFlightScheduleStartDateCalendar.add(Calendar.DAY_OF_YEAR, newFlightSchedulePlan.getnDay());
                    startDate = nextFlightScheduleStartDateCalendar.getTime();
                    newFlightSchedule = new FlightSchedule();
                }
                  
            }
            else if(newFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTWEEKLY)
            {
                System.out.print("Enter Day of Week> ");
                dayOfWeek = scanner.nextLine().trim();
                Calendar calendar = Calendar.getInstance();
                
                switch(dayOfWeek.toLowerCase())
                {
                    case "sunday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        break;
                    case "monday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        break;
                    case "tuesday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        break;
                    case "wednesday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        break;
                    case "thursday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        break;
                    case "friday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        break;
                    case "saturday":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        break;
                    default:
                        System.out.println("Invalid Day Of Week");
                }
                
                newFlightSchedulePlan.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
                
                System.out.print("Enter Departure Time (h:mm AM/PM)> ");
                departureTime = inputTimeFormat.parse(scanner.nextLine().trim());
                
                System.out.print("Enter Flight Schedule Plan Start Date (dd/mmm/yy)> ");
                startDate = inputDateFormat.parse(scanner.nextLine().trim());
                startDate.setHours(departureTime.getHours());
                startDate.setMinutes(departureTime.getMinutes());
                newFlightSchedulePlan.setStartDateTime(startDate);
                
                System.out.print("Enter Flight Schedule Plan End Date (dd/mmm/yy)> ");
                endDate = inputDateFormat.parse(scanner.nextLine().trim());
                newFlightSchedulePlan.setEndDate(endDate);
                
                System.out.print("Enter Flight Duration (hh Hours mm Minutes)> ");
                flightDuration = scanner.nextLine().trim().split("\\s+");
                
                // Extracting Flight Duration Hours and Minutes from User Input
                for (int i = 0; i < flightDuration.length; i++)
                {
                    if (flightDuration[i].equalsIgnoreCase("Hours") && i > 0)
                    {
                        flightDurationHours = Integer.parseInt(flightDuration[i-1]);
                    }
                    else if (flightDuration[i].equalsIgnoreCase("Minutes") && i > 0)
                    {
                        flightDurationMinutes = Integer.parseInt(flightDuration[i-1]);
                    }
                }
                
                // Loop through and create new weekly flight schedules between the startDate and endDate
                while (startDate.before(endDate))
                {
                    newFlightSchedule.setDepartureDateTime(startDate);
                    newFlightSchedule.setFlightDurationHours(flightDurationHours);
                    newFlightSchedule.setFlightDurationMinutes(flightDurationMinutes);
                    
                    GregorianCalendar arrivalDateTimeCalendar = new GregorianCalendar();
                    arrivalDateTimeCalendar.setTime(newFlightSchedule.getDepartureDateTime());
                    arrivalDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, newFlightSchedule.getFlightDurationHours());
                    arrivalDateTimeCalendar.add(GregorianCalendar.MINUTE, newFlightSchedule.getFlightDurationMinutes());
                    arrivalDateTime = arrivalDateTimeCalendar.getTime();
                    
                    if (arrivalDateTime.before(endDate))
                    {
                        newFlightSchedule.setArrivalDateTime(arrivalDateTime);
                        
                        // Associate newly created flight schedule and flight schedule plan
                        newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
                    }
                    
                    GregorianCalendar nextFlightScheduleStartDateCalendar = new GregorianCalendar();
                    nextFlightScheduleStartDateCalendar.setTime(startDate);
                    
                    // Calculate the number of days to reach the next recurring flight schedule
                    int daysToAdd = newFlightSchedulePlan.getDayOfWeek() - nextFlightScheduleStartDateCalendar.get(Calendar.DAY_OF_WEEK);
                    
                    if (daysToAdd <= 0)
                    {
                        // If it is already the specified day of the week or later, add 7 days to get to the next occurrence
                        daysToAdd += 7;
                    }
                    
                    nextFlightScheduleStartDateCalendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
                    startDate = nextFlightScheduleStartDateCalendar.getTime();
                    newFlightSchedule = new FlightSchedule();
                }
                
            } else
            {
                System.out.println("Invalid Flight Schedule Type! Unable to create Flight Schedule Plan for Flight Number " + flightNumber);
            }
            
            newFlightSchedulePlan.setEnabled(true);
            
            Long flightSchedulePlanId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(flight.getFlightId(), newFlightSchedulePlan);
            System.out.println("Flight Schedule Plan " + flightSchedulePlanId + " for Flight Number " + flight.getFlightId() + " successfully created!");
            
            // TODO - create and persist fares
        
        }
        catch(FlightNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "!\n");
        }
        catch(FlightSchedulePlanExistException | CreateNewFlightSchedulePlanException ex)
        {
            System.out.println("An error has occurred when creating Flight Schedule Plan: " + ex.getMessage() + "\n");
        }
        catch(GeneralException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date/time input!\n");
        }
        
    }
    
    private void doViewAllFlightSchedulePlans()
    {
        
    }
    
    private void doViewFlightSchedulePlanDetails()
    {
        
    }
}
