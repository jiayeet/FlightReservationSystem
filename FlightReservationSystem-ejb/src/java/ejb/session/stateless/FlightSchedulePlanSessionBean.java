/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteFlightSchedulePlanException;
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

    @Override
    public Long createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException
    {
        try
        {   
            em.persist(flightSchedulePlan);
            
            em.flush();
            
            for(int i = 0; i < flightSchedulePlan.getFlightSchedules().size(); i++) {
                FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightSchedulePlan.getFlightSchedules().get(i).getFlightScheduleId());
                flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            }

            return flightSchedulePlan.getFlightSchedulePlanId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightSchedulePlanExistException("Flight Schedule Plan already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
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

