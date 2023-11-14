/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewFlight(Flight flight) throws FlightExistException, GeneralException
    {
        try
        {   
            em.persist(flight);
            
            em.flush();

            return flight.getFlightId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightExistException("Flight already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<Flight> retrieveAllFlights()
    {
        /*Query query = em.createQuery("SELECT f FROM Flight f\n" +
                                     "WHERE f.flightRoute.originAirport.aitaAirportCode = :originAirport\n" +
                                     "  AND f.destinationAirport.aitaAirportCode = :destinationAirport\n" +
                                     "UNION\n" +
                                     "SELECT r FROM FlightRoute r\n" +
                                     "WHERE r.flightRoute.originAirport.aitaAirportCode = :destinationAirport\n" +
                                     "  AND r.flightRoute.destinationAirport.aitaAirportCode = :originAirport\n" +
                                     "ORDER BY flightNumber ASC, flightType DESC;");*/
        
        Query query = em.createQuery("Select f FROM Flight f");
        
        return query.getResultList();
    }

    @Override
    public Flight retrieveFlightByFlightId(Long flightId) throws FlightNotFoundException
    {
        Flight flight = em.find(Flight.class, flightId);
        
        if(flight != null)
        {
            return flight;
        }
        else
        {
            throw new FlightNotFoundException("Flight ID " + flightId + " does not exist!");
        }
    }
    
    
    
    @Override
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException
    {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE s.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        
        try
        {
            return (Flight)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightNotFoundException("Flight Number " + flightNumber + " does not exist!");
        }
    }
    
    @Override
    public void updateFlight(Flight flight) throws FlightNotFoundException, UpdateFlightException
    {
        if(flight != null && flight.getFlightId() != null)
        {
            Flight flightToUpdate = retrieveFlightByFlightId(flight.getFlightId());
            
            if(flightToUpdate.getFlightNumber().equals(flight.getFlightNumber()))
            {
                flightToUpdate.setFlightNumber(flight.getFlightNumber());
                flightToUpdate.setEnabled(flight.getEnabled());
                //flightToUpdate.setAircraftConfiguration(flight.getAircraftConfiguration());
                flightToUpdate.setFlightRoute(flight.getFlightRoute());
                //flightToUpdate.setFlightSchedules(flight.getFlightSchedules());
            }
            else
            {
                throw new UpdateFlightException("Flight number of flight record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new FlightNotFoundException("Flight ID not provided for flight to be updated, flight does not exist");
        }
    }
    
    @Override
    public void deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException
    {
        Flight flightToRemove = retrieveFlightByFlightId(flightId);
        
        /*if(flightToRemove.getAircraftConfiguration() == null && flightToRemove.getFlightRoute() == null && flightToRemove.getFlightSchedulePlan() == null)
        {
            em.remove(flightToRemove);
        }
        else if(flightToRemove.getAircraftConfiguration() == null || flightToRemove.getFlightRoute() == null || flightToRemove.getFlightSchedulePlan() == null)
        {
            flightToRemove.setEnabled(Boolean.FALSE);
            throw new DeleteFlightException("Flight ID " + flightId + " has been disabled due to existing associations.");
        }
        else
        {
            throw new DeleteFlightException("Flight ID " + flightId + " is associated with existing aircraft configurations, flight routes and flight schedule plans and cannot be deleted!");
        }*/
    }
}
