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
import entity.CabinClass;
import util.exception.DuplicateFareBasisCodeException;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import util.exception.FlightSchedulePlanNotFoundException;
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
                                 FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
    }

    public void scheduleManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Flight Management System :: Route Planner***\n");
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
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        Flight newFlight = new Flight();
        String flightNumber = "";
        String input = "";

        System.out.println("*** Flight Management System :: Schedule Manager :: Create New Flight ***\n");

            try {
            System.out.print("Enter Flight Number> ");
            flightNumber = scanner.nextLine().trim();
            newFlight.setFlightNumber(flightNumber);
            System.out.print("Enter Aircraft Configuration Name> ");
            AircraftConfiguration aircraftConfig = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(scanner.nextLine().trim());
            newFlight.setAircraftConfiguration(aircraftConfig);

            System.out.print("Enter Flight Route Origin (IATA Code)> ");
            String originAirport = scanner.nextLine().trim();
            System.out.print("Enter Flight Route Destination (IATA Code)> ");
            String destinationAirport = scanner.nextLine().trim();
            
            FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByDestinations(originAirport, destinationAirport);
            
            if (flightRoute.getEnabled() == false) {
                System.out.println("Flight route has been disabled, it cannot be used to create new flights!");
            } else {

                newFlight.setFlightRoute(flightRoute);
                newFlight.setEnabled(Boolean.TRUE);
                newFlight.setIsMain(Boolean.TRUE);

                Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight);
                System.out.println("New flight created successfully!: " + newFlightId + "\n");
                Flight mainFlight = flightSessionBeanRemote.retrieveFlightByFlightId(newFlightId);

                if (mainFlight.getFlightRoute().getComplementaryFlightRoute() != null) {
                    System.out.print("Would you like to create a complementary return flight? (Y for yes) > ");
                    input = scanner.nextLine().trim();
                    FlightRoute complementaryFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByFlightRouteId(mainFlight.getFlightRoute().getComplementaryFlightRoute().getFlightRouteId());

                    if (input.equals("Y")) {
                        Flight complementaryFlight = new Flight();
                        List<FlightSchedulePlan> complementaryFlightSchedulePlans = new ArrayList<>();

                        System.out.print("Enter Flight Number> ");
                        flightNumber = scanner.nextLine().trim();
                        complementaryFlight.setFlightNumber(flightNumber);
                        complementaryFlight.setAircraftConfiguration(aircraftConfig);
                        complementaryFlight.setFlightRoute(complementaryFlightRoute);
                        complementaryFlight.setEnabled(Boolean.TRUE);
                        complementaryFlight.setIsMain(Boolean.FALSE);
                        complementaryFlight.setComplementaryFlight(mainFlight);

                        Long complementaryFlightId = flightSessionBeanRemote.createNewFlight(complementaryFlight);

                        System.out.println("Complementary flight created successfully!: " + complementaryFlightId + "\n");

                        Flight createdComplementaryFlight = flightSessionBeanRemote.retrieveFlightByFlightId(complementaryFlightId);

                        mainFlight.setComplementaryFlight(createdComplementaryFlight);
                        flightSessionBeanRemote.updateFlight(mainFlight);
                    }
                }
            }
            }
            catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred: The flight route is not found!\n");
        }
            catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred: The aircraft configuration does not exist!\n");
        }
            catch (UpdateFlightException ex) {
            System.out.println("An error has occurred: The main flight cannot be updated!\n");
        }
            catch (FlightExistException ex) {
            System.out.println("An error has occurred: The flight already exists!\n");
        }
            catch (FlightNotFoundException ex) {
            System.out.println("An error has occurred: The flight cannot be found!\n");
        }
            catch (GeneralException ex) {
            System.out.println("An unknown error has occurred while creating the flight!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlights() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View All Flights ***\n");
        
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        System.out.printf("%8s%20s%20s%20s\n", "Flight ID", "Flight Number", "Origin AITA Code ", "Destination AITA Code");

        for(Flight flight:flights)
        {
            System.out.printf("%5s%20s%20s%20s\n", flight.getFlightId(), flight.getFlightNumber(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doViewFlightDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details ***\n");
        System.out.print("Enter Flight ID> ");
        Long flightId = scanner.nextLong();
        
        try
        {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightId(flightId);
            System.out.printf("%-15s%-25s%-25s\n", "Flight ID", "AITA Origin Code ", "AITA Destination Code");
            System.out.printf("%-15s%-25s%-25s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
            System.out.printf("%-15s%-25s\n", "Cabin Classes", "Number of Available Seats");
            for (int i = 0; i < flight.getAircraftConfiguration().getCabinClasses().size(); i++) {
                System.out.printf("%-15s%-25s\n", flight.getAircraftConfiguration().getCabinClasses().get(i).getCabinClassType().toString(), flight.getAircraftConfiguration().getCabinClasses().get(i).getMaxCapacity());
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
    
    private void doUpdateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Update Flight ***\n");
        
        try {

            System.out.print("Enter Aircraft Configuration ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                AircraftConfiguration newAircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByAircraftConfigurationId(Long.valueOf(input));
                flight.setAircraftConfiguration(newAircraftConfiguration);
            }

            /*System.out.print("Enter Flight Schedule Plan ID (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                //FlightSchedulePlan newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanID(Long.valueOf(input));
                //flight.setFlightSchedulePlan(newFlightSchedulePlan);
            }*/

            flightSessionBeanRemote.updateFlight(flight);
            System.out.println("Flight updated successfully!\n");
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }
        /*catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
        }*/
        catch (FlightNotFoundException | UpdateFlightException ex) {
            System.out.println("An error has occurred while updating flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteFlight(Flight flightToRemove) {
        Scanner scanner = new Scanner(System.in);        
        String input;
 
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Details :: Delete Flight Route ***\n");
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
        
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM yy");
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
        int layoverDuration = 0;
        String moreFlightSchedule = "";
        String createComplementPlan = "";
        Long complementaryFlightSchedulePlanId = 0l;
        
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
                System.out.print("Select Schedule Type (1: Single, 2: Multiple, 3: Recurrent every N Days, 4: Recurrent every Week)> ");
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
                    System.out.print("Enter Departure Date (dd mmm yy)> ");
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
                System.out.print("Enter NDay> ");
                newFlightSchedulePlan.setnDay(scanner.nextInt());
                scanner.nextLine();
                
                System.out.print("Enter Departure Time (h:mm AM/PM)> ");
                departureTime = inputTimeFormat.parse(scanner.nextLine().trim());
                
                System.out.print("Enter Flight Schedule Plan Start Date (dd mmm yy)> ");
                startDate = inputDateFormat.parse(scanner.nextLine().trim());
                startDate.setHours(departureTime.getHours());
                startDate.setMinutes(departureTime.getMinutes());
                newFlightSchedulePlan.setStartDateTime(startDate);
                
                System.out.print("Enter Flight Schedule Plan End Date (dd mmm yy)> ");
                endDate = inputDateFormat.parse(scanner.nextLine().trim());
                endDate.setHours(23);
                endDate.setMinutes(59);
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
                
                System.out.print("Enter Flight Schedule Plan Start Date (dd mmm yy)> ");
                startDate = inputDateFormat.parse(scanner.nextLine().trim());
                startDate.setHours(departureTime.getHours());
                startDate.setMinutes(departureTime.getMinutes());
                newFlightSchedulePlan.setStartDateTime(startDate);
                
                System.out.print("Enter Flight Schedule Plan End Date (dd mmm yy)> ");
                endDate = inputDateFormat.parse(scanner.nextLine().trim());
                endDate.setHours(23);
                endDate.setMinutes(59);
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
                    Calendar checkDayOfTheWeekCalendar = Calendar.getInstance();
                    checkDayOfTheWeekCalendar.setTime(startDate);
                    
                    if (checkDayOfTheWeekCalendar.get(Calendar.DAY_OF_WEEK) == newFlightSchedulePlan.getDayOfWeek())
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
                
            }
            else
            {
                System.out.println("Invalid Flight Schedule Type! Unable to create Flight Schedule Plan for Flight Number " + flightNumber);
            }
            
            newFlightSchedulePlan.setEnabled(true);
            
            Long flightSchedulePlanId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(flight.getFlightId(), newFlightSchedulePlan);
            System.out.println("Flight Schedule Plan " + flightSchedulePlanId + " for Flight Number " + flight.getFlightId() + " successfully created!");
            
            // Prompt user whether they wish to create a complementary return flight schedule plan
            if (flight.getComplementaryFlight() != null)
            {
                Flight complementaryReturnFlight = flight.getComplementaryFlight();
                System.out.print("A complementary return flight " + complementaryReturnFlight.getFlightNumber() + " exists! Create complementary return flight schedule plan? (Enter 'Y' to create)>  ");
                createComplementPlan = scanner.nextLine().trim();
                
                if (createComplementPlan.equals("Y"))
                {
                    System.out.print("Enter Layover Duration for " + complementaryReturnFlight.getFlightNumber() + "> ");
                    layoverDuration = scanner.nextInt();
                    scanner.nextLine();
                    
                    complementaryFlightSchedulePlanId = flightSchedulePlanSessionBeanRemote.createComplementaryFlightSchedulePlan(flightSchedulePlanId, layoverDuration, complementaryReturnFlight.getFlightNumber());
                    System.out.println("Complementary Flight Schedule Plan " + complementaryFlightSchedulePlanId + " successfully created for return flight " + complementaryReturnFlight.getFlightNumber() + "!");
                }
                else
                {
                    System.out.println("Did not create complementary return flight schedule plan!\n");
                }
                
            }
            else
            {
                System.out.println("No complementary return flight exists. Skipped creating a complementary return flight schedule plan!");
            }
            
            // TODO - create and persist fares
            List<CabinClass> cabinClasses = aircraftConfigurationSessionBeanRemote.retrieveCabinClassesByAircraftConfigurationId(flight.getAircraftConfiguration().getAircraftConfigurationId());
            List<Fare> fares = new ArrayList<>();
            
            for (CabinClass cabinClass : cabinClasses)
            {
                String moreFares = "";
                System.out.println("Requesting fare input for Cabin Class " + cabinClass.getCabinClassType().toString() + ": ");
                
                do
                {
                    Fare fare = new Fare();
                    fare.setCabinClass(cabinClass);
                    System.out.print("Enter Fare Basis Code> ");
                    fare.setFareBasisCode(scanner.nextLine().trim());              
                    System.out.print("Enter Fare Amount> ");
                    fare.setFareAmount(scanner.nextInt());
                    scanner.nextLine();
                    
                    fares.add(fare);
                    
                    System.out.print("More fares for Cabin Class " + cabinClass.getCabinClassType().toString() + "? (Enter 'N' to complete fare input)> ");
                    moreFares = scanner.nextLine().trim();
                }
                while (!moreFares.equals("N"));
                
            }
            
            
            flightSchedulePlanSessionBeanRemote.linkFlightSchedulePlanToFares(fares, flightSchedulePlanId, false);
            System.out.println("Successfully linked flight schedule plan to fares!");
            
            if (complementaryFlightSchedulePlanId > 0l)
            {
                flightSchedulePlanSessionBeanRemote.linkFlightSchedulePlanToFares(fares, complementaryFlightSchedulePlanId, true);
            }
        
        }
        catch(FlightNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "!\n");
        }
        catch(FlightSchedulePlanNotFoundException ex)
        {
            System.out.println("An error has occurred when creating Flight Schedule Plan: Flight Schedule Plan not found!");
        }
        catch(FlightSchedulePlanExistException | CreateNewFlightSchedulePlanException | DuplicateFareBasisCodeException ex)
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
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yy hh:mm a");
        
        System.out.println("\n*** Flight Management System :: Schedule Manager :: View All Flight Schedule Plans ***\n");
        
        try
        {
            List<FlightSchedulePlan> flightSchedulePlans = flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlans();
            System.out.printf("%23s%16s%18s%30s\n", "Flight Schedule Plan ID", "Flight Number", "Schedule Type", "First Departure Date/Time");

            for(FlightSchedulePlan flightSchedulePlan : flightSchedulePlans)
            {
                List<FlightSchedule> flightSchedules = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulesByFlightSchedulePlanId(flightSchedulePlan.getFlightSchedulePlanId());
                Date earliestDepartureDate = flightSchedules.get(0).getDepartureDateTime();
                System.out.printf("%23s%16s%18s%30s\n", flightSchedulePlan.getFlightSchedulePlanId(), flightSchedulePlan.getFlightNumber(), flightSchedulePlan.getFlightScheduleType().toString(), outputDateFormat.format(earliestDepartureDate));
                
                if (flightSchedulePlan.getComplementaryFlightSchedulePlan() != null)
                {
                    FlightSchedulePlan complementaryFlightSchedulePlan = flightSchedulePlan.getComplementaryFlightSchedulePlan();
                    List<FlightSchedule> complementaryFlightSchedules = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulesByFlightSchedulePlanId(complementaryFlightSchedulePlan.getFlightSchedulePlanId());
                    Date earliestComplementaryDepartureDate = complementaryFlightSchedules.get(0).getDepartureDateTime();
                    System.out.printf("%23s%16s%18s%30s\n", complementaryFlightSchedulePlan.getFlightSchedulePlanId(), complementaryFlightSchedulePlan.getFlightNumber(), complementaryFlightSchedulePlan.getFlightScheduleType().toString(), outputDateFormat.format(earliestComplementaryDepartureDate));
                }
            }
        
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        }
        catch(FlightSchedulePlanNotFoundException ex)
        {
            System.out.println("Error when retrieving all flight schedule plans: " + ex.getMessage() + "!\n");
        }
        
    }
    
    private void doViewFlightSchedulePlanDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yy hh:mm a");
        
        System.out.println("*** Flight Management System :: Schedule Manager :: View Flight Schedule Plan Details ***\n");
        System.out.print("Enter Flight Schedule Plan ID> ");
        Long flightSchedulePlanId = scanner.nextLong();
        scanner.nextLine();
        
        try
        {
            FlightSchedulePlan flightSchedulePlan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanByFlightSchedulePlanId(flightSchedulePlanId);
            List<FlightSchedule> flightSchedules = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulesByFlightSchedulePlanId(flightSchedulePlanId);
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightSchedulePlan.getFlightNumber());
            List<Fare> fares = flightSchedulePlanSessionBeanRemote.retrieveFaresByFlightSchedulePlanId(flightSchedulePlanId);
            
            System.out.printf("%23s%16s%25s\n", "Flight Schedule Plan ID", "Flight Number", "Origin - Destination");
            System.out.printf("%23s%16s%25s\n", flightSchedulePlan.getFlightSchedulePlanId(), flightSchedulePlan.getFlightNumber(), flight.getFlightRoute().originIATA() + "-" + flight.getFlightRoute().destinationIATA());
            
            System.out.printf("%11s%20s%15s\n", "Cabin Class", "Fare Basis Code", "Fare Amount");
            
            for (Fare fare : fares)
            {
                System.out.printf("%11s%20s%15s\n", fare.getCabinClass().getCabinClassType().toString(), fare.getFareBasisCode(), fare.getFareAmount());
            }
            
            System.out.printf("%19s%22s%20s%20s\n", "Flight Schedule ID", "Departure Date Time", "Arrival Date Time", "Flight Duration");
            
            for (FlightSchedule flightSchedule : flightSchedules)
            {
                System.out.printf("%19s%22s%20s%20s\n\n", flightSchedule.getFlightScheduleId(), outputDateFormat.format(flightSchedule.getArrivalDateTime()), outputDateFormat.format(flightSchedule.getDepartureDateTime()), flightSchedule.getFlightDurationHours() + " Hours " + flightSchedule.getFlightDurationMinutes() + " Minutes");
            }

            
            System.out.println("------------------------");
            System.out.println("1: Update Flight Schedule Plan");
            System.out.println("2: Delete Flight Schedule Plan");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateFlightSchedulePlan(flightSchedulePlan);
            }
            else if(response == 2)
            {
                doDeleteFlightSchedulePlan(flightSchedulePlan);
            }
        }
        catch(FlightSchedulePlanNotFoundException | FlightNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving flight schedule plan: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("\n*** Flight Management System :: Schedule Manager :: Update Flight Schedule Plan ***\n");
    }
    
    private void doDeleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("\n*** Flight Management System :: Schedule Manager :: Delete Flight Schedule Plan ***\n");
    }
}
