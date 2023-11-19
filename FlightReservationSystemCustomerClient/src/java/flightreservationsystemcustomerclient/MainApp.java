/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemcustomerclient;

import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightTicketSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import entity.CabinClass;
import entity.CreditCardRecord;
import entity.Customer;
import entity.FlightReservation;
import entity.FlightTicket;
import entity.Passenger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerUsernameExistException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightTicketIdExistenceException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentials;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private Customer currentCustomer;
    private PassengerSessionBeanRemote passengerSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FlightTicketSessionBeanRemote flightTicketSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    //private FlightScheduleSessionBean flightScheduleSessionBean;
    private FlightReservationSessionBeanRemote flightReservationSessionBean;
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public MainApp()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, PassengerSessionBeanRemote passengerSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote,
            FlightTicketSessionBeanRemote flightTicketSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            CreditCardSessionBeanRemote creditCardRecordSessionBeanRemote/*, FlightScheduleSessionBean flightScheduleSessionBean*/) {
        this();
        if (customerSessionBeanRemote == null) {
            System.out.println("customersessionbeanremote is null");
        }
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        if (this.customerSessionBeanRemote == null) {
            System.out.println("this.custommersessionbeanremote is null");
        }
        this.currentCustomer = new Customer();
        this.passengerSessionBeanRemote = passengerSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightTicketSessionBeanRemote = flightTicketSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        //this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
        
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
        System.out.println("*** Welcome to Flight Reservation System ***\n");
        System.out.println("1: Register As Customer");
        System.out.println("2: Login");
        System.out.println("3: Search Flight");
        System.out.println("4: Exit\n");
        response = 0;

            while (response < 1 || response > 4) {
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
                    searchFlight();
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
        
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        System.out.println(newCustomer.getAddress() + newCustomer.getEmail() + newCustomer.getFirstName() + newCustomer.getLastName() + newCustomer.getUsername() + newCustomer.getPassword() + newCustomer.getMobileNumber());

        if (constraintViolations.isEmpty()) {
            try {
                if(customerSessionBeanRemote != null) {
                    Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
                    System.out.println("New customer created successfully!: " + newCustomerId + "\n");
                } else {
                    System.out.println("New customer not created." + newCustomer.getAddress());
                }
                
            } catch (CustomerUsernameExistException ex) {
                System.out.println("An error has occurred while registering the a new customer account: The username already exists!\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while registering the new customer!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
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
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM yy");
        String input = "";
        Integer tripType;
        String departureAirport = "";
        String destinationAirport = "";
        Date departureDate;
        Date returnDate;
        int noOfPassengers = 0;
        Boolean directFlight;
        Integer flightPreferenceType;
        Integer cabinPreferenceType;

        System.out.println("*** Flight Reservation System :: Customer :: Search Flight ***\n");
        while (true) {
            System.out.print("Select Trip Type (1: Regular Trip, 2: Round-Trip, 3: Return Trip>");
            tripType = scanner.nextInt();
            if (tripType >= 1 && tripType <= 3) {
                    break;
            } 
            else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        try {
        System.out.print("Enter Departure airport> ");
        departureAirport = scanner.nextLine().trim();
        System.out.print("Enter Destination airport> ");
        destinationAirport = scanner.nextLine().trim();
        System.out.print("Enter Departure Date (dd mm yy)> ");
        departureDate = inputDateFormat.parse(scanner.nextLine().trim());
        
        if (tripType == 2 && tripType == 3) {
            System.out.print("Enter return date> ");
            returnDate = inputDateFormat.parse(scanner.nextLine().trim());
        }
        
        System.out.print("Enter number of passengers> ");
        noOfPassengers = scanner.nextInt();
                
        while (true) {
            System.out.print("Select preference for flight (1: Direct, 2: Connecting, 3: No Preference> ");
            flightPreferenceType = scanner.nextInt();
            if (flightPreferenceType >= 1 && flightPreferenceType <= 3) {
                break;
            } 
            else {
                System.out.println("Invalid option, please try again!\n");
            }
        }


        while (true) {
            System.out.print("Select Cabin Class Type (1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class)> ");
            cabinPreferenceType = scanner.nextInt();

            if (cabinPreferenceType >= 1 && cabinPreferenceType <= 4) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date/time input!\n");
        }
        
        //Get all flight schedules
        //Iterate through the flight schedules and find dates that are on the day itself and print
        //Iterate and find dates that depart 1/2/3 days before the required date
        //Iterate and find dates that depart 1/2/3 after the required date
        //print flightscheduleId, departuredatetime, arrivaldatetime, cabin classes available, seats available, price per passenger (lowest fare), price for all passengers 
        
        
        System.out.println("Would you like to reserve a flight? (Enter 'Y' for yes)> ");
        input = scanner.nextLine().trim();
        if (input.equals("Y")) {
            reserveFlight(noOfPassengers);
        }
    }
    
    private void reserveFlight(int noOfPassengers) {
        Scanner scanner = new Scanner(System.in);
        CabinClass cabinClassSelected;
        FlightReservation flightReservation = new FlightReservation();
        
        System.out.println("*** Flight Reservation System :: Customer :: Search Flight :: Reserve Flight ***\n");
        
        System.out.println("***Select a Flight Schedule (Input the Flight Schedule Id) )>  ***\n");
        //FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightSchedulebyFlightScheduleId(scanner.next(Long.valueOf(scanner.nextLine().trim()));
        //Flight flight = flightSessionBeanRemote.retrieveFlightbyFlightNumber(flightSchedule.getFlightSchedulePlan().getFlightNumber());
        //List<CabinClasses> cabinClasses = flight.getAircraftConfiguration().getCabinClasses();
        //Inbound flights
        //Outbound flights
        
        System.out.print("Select Cabin Class Type (1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class)> ");        
        while (true) {
            int cabinPreferenceType = scanner.nextInt();

            if (cabinPreferenceType >= 1 && cabinPreferenceType <= 4) {
                //cabinClassSelected = cabinClasses.getCabinClassType(CabinClassType.values()[cabinPreferenceType-1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        //flightReservation.setCabinClasses(cabinClasses);
        
        for(int i = 0; i < noOfPassengers; i++) {
            Passenger passenger = new Passenger();
            FlightTicket flightTicket = new FlightTicket();
            
            System.out.println("Passenger " + "1\n");
            
            System.out.print("Enter Passenger's First Name> ");
            passenger.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Passenger's Last Name> ");
            passenger.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Passenger's Passport Number> ");
            passenger.setPassportNumber(scanner.nextLine().trim());
            
            System.out.print("Enter Passenger's Seat Number> ");
            flightTicket.setSeatNumber(scanner.nextLine().trim());
            flightTicket.setPassenger(passenger);
            
            Set<ConstraintViolation<Passenger>>constraintViolations = validator.validate(passenger);
            
            //flightTicket.setCabinClass(cabinClassSelected);
            //flightTicket.setFlightReservation(flightReservation);
            //flightTicket.setFlightSchedule(flightSchedule);
            
            flightReservation.getFlightTickets().add(flightTicket);
            
            if (constraintViolations.isEmpty()) {
            try {
                Long newPassengerId = passengerSessionBeanRemote.createNewPassenger(passenger);
                Long flightTicketId = flightTicketSessionBeanRemote.createNewFlightTicket(flightTicket);
                System.out.println("Flight ticket of id: " + flightTicketId + " has been created!");
            }
            catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating a new flight ticket!: " + ex.getMessage() + "\n");
            } 
            catch (FlightTicketIdExistenceException ex) {
                System.out.println("An unknown error has occurred while creating the ticket!: " + ex.getMessage() + "\n");
            } 
            catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            } else {
                showInputDataValidationErrorsForPassenger(constraintViolations);
        }
        }
        Long flightReservationId = flightReservationSessionBeanRemote.createNewFlightReservation(flightReservation);
        System.out.println("Flight ticket of id: " + flightReservationId + " has been created!");
    }
    
    private void checkOut() {

        Scanner scanner = new Scanner(System.in);
        CreditCardRecord newCreditCard = new CreditCardRecord();
        
        System.out.println("*** Flight Reservation System :: Customer :: Reserve Flight :: Checkout ***\n");
        System.out.print("Enter Card Holder Name> ");
        newCreditCard.setCardHolderName(scanner.nextLine().trim());
        System.out.print("Enter Card Number> ");
        newCreditCard.setCardNumber(scanner.nextLine().trim());
        System.out.print("Enter Pin> ");
        newCreditCard.setPin(scanner.nextLine().trim());
        System.out.print("Enter Expiry Date (mm/yy) > ");
        newCreditCard.setExpirationDate(scanner.nextLine().trim());
        System.out.print("Enter CVV> ");
        newCreditCard.setCVV(scanner.nextLine().trim());
        
        Set<ConstraintViolation<CreditCardRecord>>constraintViolations = validator.validate(newCreditCard);
        
        if (constraintViolations.isEmpty()) {
            try {
                Long newCreditCardId = creditCardSessionBeanRemote.createNewCreditCard(newCreditCard);
                System.out.println("New customer created successfully!: " + newCreditCardId + "\n");
            }
            catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCreditCard(constraintViolations);
        }
        
        currentCustomer.setCreditCardRecord(newCreditCard);
        
        
        System.out.println("*** Checkout is successful! ***\n");

    }
    
    private void viewMyFlightReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Reservation System :: Route Planner :: View All Flight Routes ***\n");
        
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.retrieveAllFlightReservations();
        System.out.printf("%8s\n", "Flight Reservation ID");

        for(FlightReservation flightReservation:flightReservations)
        {
            System.out.printf("%8s\n", flightReservation.getFlightReservationId());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void viewMyFlightReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Flight Management System :: Customer :: View Flight Reservation Details ***\n");
        System.out.print("Enter Flight Reservation ID> ");
        Long flightReservationId = scanner.nextLong();
        
        try
        {
            FlightReservation flightReservation = flightReservationSessionBeanRemote.retrieveFlightReservationByFlightReservationId(flightReservationId);
            System.out.printf("%-15s\n", "Flight Reservation ID");
            System.out.printf("%-15s\n", flightReservation.getFlightReservationId().toString());
            System.out.printf("%-15s%\n", "Passenger");
            for (int i = 0; i < flightReservation.getFlightTickets().size(); i++) {
                System.out.printf("%-15s%-25s\n", "Flight Ticket Id", "Seat Number");
                System.out.printf("%-15s%-25s\n", flightReservation.getFlightTickets().get(i).getFlightTicketId(), flightReservation.getFlightTickets().get(i).getSeatNumber());
            }
        }
        catch(FlightReservationNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving flight: " + ex.getMessage() + "\n");
        }
    }
    
    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForFlightTicket(Set<ConstraintViolation<FlightTicket>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForPassenger(Set<ConstraintViolation<Passenger>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForCreditCard(Set<ConstraintViolation<CreditCardRecord>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
