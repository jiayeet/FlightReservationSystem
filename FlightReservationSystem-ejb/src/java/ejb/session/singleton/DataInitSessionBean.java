/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AirportSessionBeanLocal;
import entity.Airport;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 65968
 */
@Singleton
@LocalBean
public class DataInitSessionBean {

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
    }
}
