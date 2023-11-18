/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewFlightReservation(FlightReservation newFlightReservation)
    {
        em.persist(newFlightReservation);
        em.flush();
        
        return newFlightReservation.getFlightReservationId();
    }
    
    @Override
    public FlightReservation retrieveFlightReservationByFlightReservationId(Long flightReservationId) throws FlightReservationNotFoundException
    {
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);
        
        if(flightReservation != null)
        {
            return flightReservation;
        }
        else
        {
            throw new FlightReservationNotFoundException("Flight Reservation ID " + flightReservationId + " does not exist!");
        }
    }
    
    @Override
    public List<FlightReservation> retrieveAllFlightReservations()
    {
        Query query = em.createQuery("SELECT frs FROM FlightReservation frs");
        
        return query.getResultList();
    }
}
