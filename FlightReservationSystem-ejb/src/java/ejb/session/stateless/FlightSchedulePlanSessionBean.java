/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateNewFlightSchedulePlanException;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FlightNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightSchedulePlanException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    @Resource
    private EJBContext eJBContext;
    
    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;

    
    public FlightSchedulePlanSessionBean() 
    {
    }
    
    
    @Override
    public Long createNewFlightSchedulePlan(Long flightId, FlightSchedulePlan newFlightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException, CreateNewFlightSchedulePlanException
    {
        
        if (newFlightSchedulePlan != null) 
        {
            try
            {
                // Need to retrieve flight to to associate within the session bean instead of client since it already exists.
                Flight flight = flightSessionBeanLocal.retrieveFlightByFlightId(flightId);
                
                // Persist new flight schedule plans and associated flight schedules in database first to ensure that id can be retrieved for query
                em.persist(newFlightSchedulePlan);
                
                for(FlightSchedule flightSchedule : newFlightSchedulePlan.getFlightSchedules())
                {
                    em.persist(flightSchedule);
                }
                
                em.flush();
                
                // Flight enabled attribute already checked in client
                if (!flight.getFlightSchedulePlans().isEmpty())
                {
                    List<Long> existingFlightSchedulePlanIds = new ArrayList<>();
                    
                    for (FlightSchedulePlan existingFlightSchedulePlan : flight.getFlightSchedulePlans())
                    {
                        existingFlightSchedulePlanIds.add(existingFlightSchedulePlan.getFlightSchedulePlanId());
                    }
                    
                    Query query = em.createQuery("SELECT fsp1.flightSchedulePlanId FROM FlightSchedulePlan fsp1, IN (fsp1.flightSchedules) fs1, FlightSchedulePlan fsp2, IN (fsp2.flightSchedules) fs2 " + 
                            "WHERE fsp1.flightSchedulePlanId IN :existingFlightSchedulePlanIds AND fsp2.flightSchedulePlanId = :newFlightSchedulePlanId AND (fs1.departureDateTime <= fs2.arrivalDateTime AND fs1.arrivalDateTime >= fs2.departureDateTime)");
                    query.setParameter("existingFlightSchedulePlanIds", existingFlightSchedulePlanIds);
                    query.setParameter("newFlightSchedulePlanId", newFlightSchedulePlan.getFlightSchedulePlanId());
                    
                    
                    List<Long> flightSchedulePlanIdsWithOverlap = query.getResultList();
                    
                    if (!flightSchedulePlanIdsWithOverlap.isEmpty())
                    {
                        // eJBContext.setRollbackOnly();
                        
                        throw new CreateNewFlightSchedulePlanException("Overlap with existing schedules " + flightSchedulePlanIdsWithOverlap.toString() + " for Flight Number : " + flightId + "\n");
                    }
                }
                
                // Associate new flight schedule plan with the flight number and persist flight schedule plan and associated flight schedules
                flight.getFlightSchedulePlans().add(newFlightSchedulePlan);
                return newFlightSchedulePlan.getFlightSchedulePlanId();
                
            }
            catch(FlightNotFoundException ex)
            {
                throw new CreateNewFlightSchedulePlanException(ex.getMessage());
            }
            catch (PersistenceException ex)
            {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) 
                {
                    throw new FlightSchedulePlanExistException("Flight Schedule Plan already exist");
                }
                else 
                {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        }
        else
        {
            throw new CreateNewFlightSchedulePlanException("Flight Schedule Plan Information not provided");
        }
      
    }
    
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans()
    {      
        Query query = em.createQuery("Select fsp FROM FlightSchedulePlan fsp");
        
        return query.getResultList();
    }
    
    @Override
    public FlightSchedulePlan retrieveFlightSchedulePlanByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException
    {
        FlightSchedulePlan flightSchedulePlan = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        
        if(flightSchedulePlan != null)
        {
            // Lazily Load to-Many associations
            flightSchedulePlan.getFlightSchedules();
            
            return flightSchedulePlan;
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan ID " + flightSchedulePlanId + " does not exist!");
        }
    }
    
    @Override
    public void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, UpdateFlightSchedulePlanException
    {
        if(flightSchedulePlan != null && flightSchedulePlan.getFlightSchedulePlanId() != null)
        {
            FlightSchedulePlan flightSchedulePlanToUpdate = retrieveFlightSchedulePlanByFlightSchedulePlanId(flightSchedulePlan.getFlightSchedulePlanId());
            
            //TODO: Update Fares and Dates
                
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan ID not provided for flight schedule plan to be updated, flight schedule plan does not exist");
        }
    }
    
    @Override
    public void deleteFlight(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException
    {
        /*FlightSchedulePlan flightSchedulePlanToRemove = retrieveFlightSchedulePlanByFlightSchedulePlanId(flightSchedulePlanId);
        
        if(flightSchedulePlanToRemove.getFlight() == null && flightSchedulePlanToRemove.getFlightSchedules() == null && flightSchedulePlanToRemove.getFare() == null)
        {
            em.remove(flightSchedulePlanToRemove);
        }
        else if(flightSchedulePlanToRemove.getFlight() == null && flightSchedulePlanToRemove.getFlightSchedules() == null && flightSchedulePlanToRemove.getFare() == null)
        {
            flightSchedulePlanToRemove.setEnabled(Boolean.FALSE);
            throw new DeleteFlightSchedulePlanException("Flight Schedule Plan ID " + flightSchedulePlanId + " has been disabled due to existing associations.");
        }
        else
        {
            throw new DeleteFlightSchedulePlanException("Flight Schedule Plan ID " + flightSchedulePlanId + " is associated with existing aircraft configurations, flight routes and flight schedule plans and cannot be deleted!");
        }*/
    }
}

