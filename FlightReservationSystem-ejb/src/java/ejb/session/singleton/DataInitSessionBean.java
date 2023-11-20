/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.AircraftType;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeUserRoleEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;
    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;
    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public DataInitSessionBean() {
        
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("fleetmanager");
        } catch (EmployeeNotFoundException ex) {
            //initialiseData();
            initialiseData1();
        }
    }
    
    /*private void initialiseData() {
        try 
        {
        airportSessionBeanLocal.createNewAirport(new Airport("SIN", "Changi", "Singapore", "Singapore", "Singapore"));
        airportSessionBeanLocal.createNewAirport(new Airport("HKG", "Hong Kong", "Chek Lap Kok", "Hong Kong", "China"));
        employeeSessionBeanLocal.createNewEmployee(new Employee("firstName", "lastName", "systemAdministrator", "password", EmployeeUserRoleEnum.SYSTEMADMINISTRATOR));
        partnerSessionBeanLocal.createNewPartner(new Partner("firstName", "lastName", "partner", "password"));
        aircraftTypeSessionBeanLocal.createNewAircraft(new AircraftType("Boeing 737", 200));
        }
        catch (EmployeeUsernameExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }*/
    
    private void initialiseData1() {
        try
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("Fleet", "Manager", "fleetmanager", "password", EmployeeUserRoleEnum.FLEETMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Route", "Planner", "routeplanner", "password", EmployeeUserRoleEnum.ROUTEPLANNER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Schedule", "Manager", "schedulemanager", "password", EmployeeUserRoleEnum.SCHEDULEMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Sales", "Manager", "salesmanager", "password", EmployeeUserRoleEnum.SALESMANAGER));
            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday", ".com", "holidaydotcom", "password"));
            airportSessionBeanLocal.createNewAirport(new Airport("SIN", "Changi", "Singapore", "Singapore", "Singapore"));
            airportSessionBeanLocal.createNewAirport(new Airport("HKG", "Hong Kong", "Chek Lap Kok", "Hong Kong", "China"));
            airportSessionBeanLocal.createNewAirport(new Airport("TPE", "Taoyuan", "Taipei", "Taiwan", "R.O.C"));
            airportSessionBeanLocal.createNewAirport(new Airport("NRT", "Narita", "Narita", "Chiba", "Japan"));
            airportSessionBeanLocal.createNewAirport(new Airport("SYD", "Sydney", "Sydney", "New South Wales", "Australia"));
            aircraftTypeSessionBeanLocal.createNewAircraft(new AircraftType("Boeing 737", 200));
            aircraftTypeSessionBeanLocal.createNewAircraft(new AircraftType("Boeing 747", 400));
        }
        catch (EmployeeUsernameExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
}
