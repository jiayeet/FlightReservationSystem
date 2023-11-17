/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
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

    @EJB(name = "AircraftTypeSessionBeanLocal")
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;
    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB(name = "AirportSessionBeanLocal")
    private AirportSessionBeanLocal airportSessionBeanLocal;
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public DataInitSessionBean() {
        
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("systemAdministrator");
        } catch (EmployeeNotFoundException ex) {
            initialiseData();
        }
    }
    
    private void initialiseData() {
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
    }
}
