/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightException;
import javax.ejb.EJB;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;
    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;
    
    @Override
    public Long createNewFlight(Flight flight) throws FlightExistException, GeneralException {

        try {
            em.persist(flight);

            em.flush();

            return flight.getFlightId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new FlightExistException("Flight already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }

    }
    
    @Override
    public List<Flight> retrieveAllFlights()
    {
        
        Query query = em.createQuery("Select f FROM Flight f ORDER BY f.flightNumber ASC");
        
        return query.getResultList();
    }

    @Override
    public Flight retrieveFlightByFlightId(Long flightId) throws FlightNotFoundException
    {
        Flight flight = em.find(Flight.class, flightId);
        
        if(flight != null)
        {
            flight.getAircraftConfiguration().getCabinClasses().size();
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
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :inFlightNumber");
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
    public void updateFlight(Flight flight) throws AircraftConfigurationNotFoundException, FlightNotFoundException, UpdateFlightException, FlightRouteNotFoundException
    {
            System.out.println("check 1");
            
            if (flight != null && flight.getFlightId() != null) {
                Flight flightToUpdate = retrieveFlightByFlightId(flight.getFlightId());
                System.out.println("check 2");

                if (flightToUpdate.getFlightNumber().equals(flight.getFlightNumber())) {
                    System.out.println("check 3");
                    flightToUpdate.setFlightNumber(flight.getFlightNumber());
                    flightToUpdate.setEnabled(flight.getEnabled());
                    flightToUpdate.setIsMain(flight.getIsMain());


                    if (flight.getFlightRoute() != null) {
                        FlightRoute flightRouteUpdate = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(flight.getFlightRoute().getFlightRouteId());
                        flightToUpdate.setFlightRoute(flightRouteUpdate);
                        System.out.println("check 4");
                    } else {
                        throw new FlightRouteNotFoundException("Flight route to be updated cannot be found.");
                    }
                    
                    if (flight.getAircraftConfiguration() != null) {
                        AircraftConfiguration aircraftConfigUpdate = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByAircraftConfigurationId(flight.getAircraftConfiguration().getAircraftConfigurationId());
                        flightToUpdate.setAircraftConfiguration(aircraftConfigUpdate);
                        System.out.println("check 8");
                    } else {
                        throw new AircraftConfigurationNotFoundException("Aircraft configuration cannot be found.");
                    }
                    
                    if (flight.getComplementaryFlight() != null) {
                        System.out.println("complementary flight id: " + flight.getComplementaryFlight().getFlightId());
                        Flight complementaryFlight = retrieveFlightByFlightId(flight.getComplementaryFlight().getFlightId());
                        flightToUpdate.setComplementaryFlight(complementaryFlight);
                    } else {
                        System.out.println("Complementary flight not provided for update");
                    }
                    
                    //flightToUpdate.setFlightSchedulePlan(flight.getFlightSchedulePlans());
                    
                } else {
                    throw new UpdateFlightException("Flight number of flight record to be updated does not match the existing record");
                }
            }
    }
    
    @Override
    public void deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException
    {
        Flight flightToRemove = retrieveFlightByFlightId(flightId);
        
        if(!flightToRemove.getFlightSchedulePlans().isEmpty() && flightToRemove.getFlightRoute() == null)
        {
            if(flightToRemove.getIsMain() == true && flightToRemove.getComplementaryFlight() != null) {
                Flight complementaryFlight = retrieveFlightByFlightId(flightToRemove.getComplementaryFlight().getFlightId());
                em.remove(complementaryFlight);
            } else if (flightToRemove.getIsMain() == false) {
                Flight mainFlightRoute = retrieveFlightByFlightId(flightToRemove.getComplementaryFlight().getFlightId());
                mainFlightRoute.setComplementaryFlight(null);
            }
            
            em.remove(flightToRemove);
        }
        else
        {
            throw new DeleteFlightException("Flight ID " + flightId + " is associated with an existing flight schedule plan or flight route and cannot be deleted!");
        }
    }
    
}
