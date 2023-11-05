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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct()
    {
        if(em.find(Airport.class, 1l) == null)
        {
            airportSessionBeanLocal.createNewAirport(new Airport("SIN", "Merlion Airport", "Singapore", "Singapore", "Singapore"));
            airportSessionBeanLocal.createNewAirport(new Airport("TPE", "Merlion Airport", "Taipei", "Taiwan", "Taiwan"));
        }
        
        if(em.find(Employee.class, 1l) == null)
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("firstName", "lastName", "systemAdministrator", "password", EmployeeUserRoleEnum.SYSTEMADMINISTRATOR));
        }
        
        if(em.find(Partner.class, 1l) == null)
        {
            partnerSessionBeanLocal.createNewPartner(new Partner("firstName", "lastName", "partner", "password"));
        }
        
        if(em.find(AircraftType.class, 1l) == null)
        {
            aircraftTypeSessionBeanLocal.createNewAircraft(new AircraftType("Boeing 737", 100));
        }
    }
}
