/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlightException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;
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
        
        Query query = em.createQuery("Select f FROM Flight f");
        
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
    public void updateFlight(Flight flight) throws AircraftConfigurationNotFoundException, FlightNotFoundException, UpdateFlightException
    {
        try {
        if(flight != null && flight.getFlightId() != null && flight.getFlightNumber() != null)
        {
            Flight flightToUpdate = retrieveFlightByFlightId(flight.getFlightId());
            
            if(flightToUpdate.getFlightNumber().equals(flight.getFlightNumber()))
            {
                if (flight.getFlightNumber() != null) {
                    flightToUpdate.setFlightNumber(flight.getFlightNumber());
                }
                
                if (flight.getEnabled() != null) {
                    flightToUpdate.setEnabled(flight.getEnabled());
                }
                
                if (flight.getIsMain() != null) {
                    flightToUpdate.setIsMain(flight.getIsMain());
                }
                
                if (flight.getFlightRoute() != null) {
                    FlightRoute flightRouteUpdate = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(flight.getFlightRoute().getFlightRouteId());
                    flightToUpdate.setFlightRoute(flightRouteUpdate);
                }
                
                if (flight.getAircraftConfiguration() != null) {
                    AircraftConfiguration aircraftConfigUpdate = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByAircraftConfigurationId(flight.getAircraftConfiguration().getAircraftConfigurationId());
                    flightToUpdate.setAircraftConfiguration(aircraftConfigUpdate);
                }
                
                if (flight.getComplementaryFlight() != null) {
                    System.out.println("complementary flight id: " + flight.getComplementaryFlight().getFlightId());
                    Flight complementaryFlight = retrieveFlightByFlightId(flight.getComplementaryFlight().getFlightId());
                    if (complementaryFlight != null) {
                        flightToUpdate.setComplementaryFlight(complementaryFlight);
                    }
                }
                
                //flightToUpdate.setFlightSchedules(flight.getFlightSchedules());
            }
            else
            {
                throw new UpdateFlightException("Flight number of flight record to be updated does not match the existing record");
            }
        }
        }
        catch (FlightRouteNotFoundException ex)
        {
            throw new FlightNotFoundException("Flight route Id to be provided does not exist.");
        }
        catch (AircraftConfigurationNotFoundException ex)
        {
            throw new AircraftConfigurationNotFoundException("Aircraft configuration cannot be found.");
        }
        catch (FlightNotFoundException ex)
        {
            throw new FlightNotFoundException("Flight ID not provided for flight to be updated, flight does not exist.");
        }
    }
    
    @Override
    public void deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException
    {
        Flight flightToRemove = retrieveFlightByFlightId(flightId);
        
        if(flightToRemove.getAircraftConfiguration() == null && flightToRemove.getFlightRoute() == null && flightToRemove.getFlightSchedulePlans().isEmpty())
        {
            em.remove(flightToRemove);
        }
        else if(flightToRemove.getAircraftConfiguration() == null || flightToRemove.getFlightRoute() == null || flightToRemove.getFlightSchedulePlans().isEmpty())
        {
            flightToRemove.setEnabled(Boolean.FALSE);
            throw new DeleteFlightException("Flight ID " + flightId + " has been disabled due to existing associations.");
        }
        else
        {
            throw new DeleteFlightException("Flight ID " + flightId + " is associated with existing aircraft configurations, flight routes and flight schedule plans and cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Flight>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
