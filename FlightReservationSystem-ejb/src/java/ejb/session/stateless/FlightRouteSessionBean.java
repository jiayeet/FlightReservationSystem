/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
    public FlightRoute retrieveFlightRouteByDestinations(String originDestination, String targetDestination) throws FlightRouteNotFoundException {
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.airportOrigin.iataAirportCode = :origin AND fr.airportDestination.iataAirportCode = :target");
        query.setParameter("origin", originDestination);
        query.setParameter("target", targetDestination);
        
        try {
        return (FlightRoute) query.getSingleResult();
    } catch (NoResultException ex) {
        throw new FlightRouteNotFoundException("Flight Route of origin " + originDestination + " and " + targetDestination+ " does not exist!");
        }
    }
    
    @Override
    public List<FlightRoute> retrieveAllFlightRoutes()
    {
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr " +
                                     "ORDER BY fr.airportOrigin.airportName ASC");
        
        return query.getResultList();
    }
    
    @Override
    public void updateFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException
    {   
        if(flightRoute != null && flightRoute.getFlightRouteId() != null)
        {
            FlightRoute flightRouteToUpdate = retrieveFlightRouteByFlightRouteId(flightRoute.getFlightRouteId());
            
                flightRouteToUpdate.setAirportDestination(flightRoute.getAirportDestination());
                flightRouteToUpdate.setAirportOrigin(flightRoute.getAirportOrigin());
            
            if(flightRoute.getComplementaryFlightRoute() != null) {
                FlightRoute complementaryFlight = retrieveFlightRouteByFlightRouteId(flightRoute.getComplementaryFlightRoute().getFlightRouteId());
                flightRouteToUpdate.setComplementaryFlightRoute(complementaryFlight);
            }
        }
        else
        {
            throw new FlightRouteNotFoundException("Flight Route ID not provided for flight to be updated");
        }
    }
    
    /*public void DeleteRoute(FlightRoute flightRoute) {
        try {
            FlightRoute flightRouteToDelete = retrieveFlightRouteByFlightRouteId(flightRoute.getFlightRouteId());
            em.remove(flightRouteToDelete);
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("Flight route is not found!");
        }
    }*/
    
    @Override
    public void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException, DeleteFlightRouteException
    {
        FlightRoute flightRouteToRemove = retrieveFlightRouteByFlightRouteId(flightRouteId);
        
        if(flightRouteToRemove.getFlights().isEmpty())
        {
            if(flightRouteToRemove.getIsMain() == true && flightRouteToRemove.getComplementaryFlightRoute() != null) {
                FlightRoute complementaryFlightRoute = retrieveFlightRouteByFlightRouteId(flightRouteToRemove.getComplementaryFlightRoute().getFlightRouteId());
                em.remove(complementaryFlightRoute);
            } else if (flightRouteToRemove.getIsMain() == false) {
                FlightRoute mainFlightRoute = retrieveFlightRouteByFlightRouteId(flightRouteToRemove.getComplementaryFlightRoute().getFlightRouteId());
                mainFlightRoute.setComplementaryFlightRoute(null);
            }
            
            em.remove(flightRouteToRemove);
        }
        else
        {
            flightRouteToRemove.setEnabled(Boolean.FALSE);
            throw new DeleteFlightRouteException("Flight Route ID " + flightRouteId + " is associated with existing flights and cannot be deleted!");
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
