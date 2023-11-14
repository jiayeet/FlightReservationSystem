/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    
    @Override
    public Long createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException
    {
        try
        {   
            if (!isOriginDestinationUnique(flightRoute)) {
            throw new FlightRouteExistException("Flight Route with the same origin and destination airports already exists");
            }
            
            em.persist(flightRoute);
            
            Airport originAirport = em.find(Airport.class, flightRoute.getAirportOrigin().getAirportId());
            Airport destinationAirport = em.find(Airport.class, flightRoute.getAirportDestination().getAirportId());
            
            originAirport.getOriginFlightRoutes().add(flightRoute);
            destinationAirport.getDestinationFlightRoutes().add(flightRoute);
            
            em.flush();

            return flightRoute.getFlightRouteId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightRouteExistException("Flight Route with same O-D pair already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId) throws FlightRouteNotFoundException
    {
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
        
        if(flightRoute != null)
        {
            return flightRoute;
        }
        else
        {
            throw new FlightRouteNotFoundException("Flight Route ID " + flightRouteId + " does not exist!");
        }
    }
    
    @Override
    public List<FlightRoute> retrieveAllFlightRoutes()
    {
        Query query = em.createQuery("SELECT f FROM FlightRoute f WHERE (f.originAirport = :originAirport AND f.destinationAirport = :destinationAirport) OR " +
                                     "(f.originAirport = :destinationAirport AND f.destinationAirport = :originAirport) " +
                                     "ORDER BY f.originAirport ASC");
        
        return query.getResultList();
    }
    
    @Override
    public void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException, DeleteFlightRouteException
    {
        FlightRoute flightRouteToRemove = retrieveFlightRouteByFlightRouteId(flightRouteId);
        
        if(flightRouteToRemove.getAirportOrigin() == null && flightRouteToRemove.getAirportDestination() == null)
        {
            em.remove(flightRouteToRemove);
        }
        else
        {
            flightRouteToRemove.setEnabled(Boolean.FALSE);
            throw new DeleteFlightRouteException("Flight Route ID " + flightRouteId + " is associated with existing airports and cannot be deleted!");
        }
    }
    
    private boolean isOriginDestinationUnique(FlightRoute flightRoute) {
        Query query = em.createQuery("SELECT COUNT(f) FROM FlightRoute f WHERE f.airportOrigin = :origin AND f.airportDestination = :destination");
        query.setParameter("origin", flightRoute.getAirportOrigin());
        query.setParameter("destination", flightRoute.getAirportDestination());

        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

}
