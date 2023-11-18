/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemcustomerclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightTicketSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import entity.CabinClass;
import entity.CreditCardRecord;
import entity.Customer;
import entity.FlightReservation;
import entity.FlightTicket;
import entity.Passenger;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassType;
import util.exception.CustomerUsernameExistException;
import util.exception.FlightReservationNotFoundException;
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
    //private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    //private FlightScheduleSessionBean flightScheduleSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public MainApp()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, PassengerSessionBeanRemote passengerSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote,
            FlightTicketSessionBeanRemote flightTicketSessionBeanRemote /*, flightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBean flightScheduleSessionBean*/) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.currentCustomer = new Customer();
        this.passengerSessionBeanRemote = passengerSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightTicketSessionBeanRemote = flightTicketSessionBeanRemote;
        //this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        //this.flightScheduleSessionBean = flightScheduleSessionBean;
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
        
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        System.out.println(newCustomer.getAddress() + newCustomer.getEmail() + newCustomer.getFirstName() + newCustomer.getLastName() + newCustomer.getUsername() + newCustomer.getPassword() + newCustomer.getMobileNumber());

        if (constraintViolations.isEmpty()) {
            try {
                Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
                System.out.println("New customer created successfully!: " + newCustomerId + "\n");
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
        String input = "";
        Integer tripType;
        String departureAirport = "";
        String destinationAirport = "";
        String departureDate = "";
        String returnDate = "";
        int noOfPassengers;
        Boolean directFlight;

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
        System.out.print("Enter departure airport> ");
        departureAirport = scanner.nextLine().trim();
        System.out.print("Enter destination airport> ");
        destinationAirport = scanner.nextLine().trim();
        System.out.print("Enter departure date> ");
        departureDate = scanner.nextLine().trim();
        
        if (tripType == 2 && tripType == 3) {
            System.out.print("Enter return date> ");
            returnDate = scanner.nextLine().trim();
        }
        
        System.out.print("Enter number of passengers> ");
        noOfPassengers = scanner.nextInt();
                
        while (true) {
            System.out.print("Select preference for flight (1: Direct, 2: Connecting, 3: No Preference> ");
            Integer flightPreferenceType = scanner.nextInt();
            if (flightPreferenceType >= 1 && flightPreferenceType <= 3) {
                break;
            } 
            else {
                System.out.println("Invalid option, please try again!\n");
            }
        }


        while (true) {
            System.out.print("Select Cabin Class Type (1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class)> ");
            Integer cabinClassTypeInt = scanner.nextInt();

            if (cabinClassTypeInt >= 1 && cabinClassTypeInt <= 4) {
                CabinClass newCabinClass = new CabinClass();
                newCabinClass.setCabinClassType(CabinClassType.values()[cabinClassTypeInt - 1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        //TODO - Flight Schedule printing
        //TODO - Fare printing
        
        
        System.out.println("Would you like to reserve a flight? (Enter 'Y' for yes)> ");
        input = scanner.nextLine().trim();
        if (input.equals("Y")) {
            reserveFlight(noOfPassengers);
        }
    }
    
    private void reserveFlight(int noOfPassengers) {
        Scanner scanner = new Scanner(System.in);
        
        
        
        //TODO - Flight Reservation Logic
        
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
            
            //Long passengerId = passengerSessionBeanRemote.createNewPassenger(passenger);
            
            System.out.print("Enter Passenger's Seat Number> ");
            flightTicket.setSeatNumber(scanner.nextLine().trim());
            flightTicket.setPassenger(passenger);
            
            //Bean Validation for Passenger
            
            //flightTicket.setCabinClass(cabinClass);
            //flightTicket.setFlightReservation(flightReservation);
            //flightTicket.setFlightSchedule(flightSchedule);
            
            //flightReservation.getFlightTickets().add(flightTicket);
            //TO DO - Add in the association with customer and flight ticket
        }
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
        System.out.print("Enter Expiry Date> ");
        //newCreditCard.setExpirationDate(scanner.nextLine().trim());
        System.out.print("Enter CVV> ");
        newCreditCard.setCVV(scanner.nextLine().trim());
        
        //Input Data Validation for Credit Card
        //Long creditCardId = creditCardSessionBeanRemote.createNewCreditCard();
        
        currentCustomer.setCreditCardRecord(newCreditCard);
        
        System.out.println("*** Checkout is successful! ***\n");

    }
    
    private void viewMyFlightReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Flight Reservation System :: Route Planner :: View All Flight Routes ***\n");
        
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.retrieveAllFlightReservations();
        System.out.printf("%8s%20s%20s\n", "Flight Route ID", "Origin AITA Code ", "Destination AITA Code");

        for(FlightReservation flightReservation:flightReservations)
        {
            //System.out.printf("%8s%20s%20s\n", flightRoute.getFlightRouteId(), flightRoute.getAirportOrigin().getIataAirportCode(), flightRoute.getAirportDestination().getIataAirportCode());
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
            /*System.out.printf("%-15s%-25s%-25s\n", "Flight ID", "AITA Origin Code ", "AITA Destination Code");
            System.out.printf("%-15s%-25s%-25s\n", flight.getFlightId().toString(), flight.getFlightRoute().getAirportOrigin().getIataAirportCode(), flight.getFlightRoute().getAirportDestination().getIataAirportCode());
            System.out.printf("%-15s%-25s\n", "Cabin Classes", "Number of Available Seats");
            for (int i = 0; i < flight.getAircraftConfiguration().getCabinClasses().size(); i++) {
                System.out.printf("%-15s%-25s\n", flight.getAircraftConfiguration().getCabinClasses().get(i).getCabinClassType().toString(), flight.getAircraftConfiguration().getCabinClasses().get(i).getMaxCapacity());
            }*/
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
}
