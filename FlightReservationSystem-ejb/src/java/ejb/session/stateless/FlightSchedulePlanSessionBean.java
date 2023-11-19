/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.DuplicateFareBasisCodeException;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.FlightScheduleType;
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
    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;

    
    public FlightSchedulePlanSessionBean() 
    {
    }
    
    
    @Override
    public Long createNewFlightSchedulePlan(Long flightId, FlightSchedulePlan newFlightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException, CreateNewFlightSchedulePlanException
    {
        
        if (newFlightSchedulePlan != null && newFlightSchedulePlan.getEnabled()) 
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
    public Long createComplementaryFlightSchedulePlan(Long mainFlightSchedulePlanId, int layoverDuration, String returnFlightNumber) throws CreateNewFlightSchedulePlanException
    {
        List<FlightSchedule> complementaryFlightSchedules = new ArrayList<>();
        
        try
        {
            FlightSchedulePlan mainFlightSchedulePlan = retrieveFlightSchedulePlanByFlightSchedulePlanId(mainFlightSchedulePlanId);

            // TODO - Check whether returnFlightNumber is the complementary return flight of the flight associated with mainFlightSchedulePlan
            Flight returnFlight = flightSessionBeanLocal.retrieveFlightByFlightNumber(returnFlightNumber);

            FlightSchedulePlan newComplementaryFlightSchedulePlan = new FlightSchedulePlan();
            newComplementaryFlightSchedulePlan.setFlightNumber(returnFlightNumber);
            newComplementaryFlightSchedulePlan.setFlightScheduleType(mainFlightSchedulePlan.getFlightScheduleType());

            if (newComplementaryFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTNDAY)
            {
                newComplementaryFlightSchedulePlan.setnDay(mainFlightSchedulePlan.getnDay());
            }
            else if (newComplementaryFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTWEEKLY)
            {
                newComplementaryFlightSchedulePlan.setDayOfWeek(mainFlightSchedulePlan.getDayOfWeek());
            }

            
            if (newComplementaryFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTNDAY || newComplementaryFlightSchedulePlan.getFlightScheduleType() == FlightScheduleType.RECURRENTWEEKLY)
            {
                newComplementaryFlightSchedulePlan.setStartDateTime(mainFlightSchedulePlan.getStartDateTime());
                newComplementaryFlightSchedulePlan.setEndDate(mainFlightSchedulePlan.getEndDate());
            }

            
            if (!mainFlightSchedulePlan.getFlightSchedules().isEmpty())
            {
                for (FlightSchedule mainFlightSchedule : mainFlightSchedulePlan.getFlightSchedules())
                {
                    FlightSchedule complementaryFlightSchedule = new FlightSchedule();

                    GregorianCalendar complementaryDepartureDateTimeCalendar = new GregorianCalendar();
                    complementaryDepartureDateTimeCalendar.setTime(mainFlightSchedule.getArrivalDateTime());
                    complementaryDepartureDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, layoverDuration);
                    complementaryFlightSchedule.setDepartureDateTime(complementaryDepartureDateTimeCalendar.getTime());

                    complementaryFlightSchedule.setFlightDurationHours(mainFlightSchedule.getFlightDurationHours());
                    complementaryFlightSchedule.setFlightDurationMinutes(mainFlightSchedule.getFlightDurationMinutes());

                    GregorianCalendar complementaryArrivalDateTimeCalendar = new GregorianCalendar();
                    complementaryArrivalDateTimeCalendar.setTime(complementaryFlightSchedule.getArrivalDateTime());
                    complementaryArrivalDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, complementaryFlightSchedule.getFlightDurationHours());
                    complementaryArrivalDateTimeCalendar.add(GregorianCalendar.MINUTE, complementaryFlightSchedule.getFlightDurationMinutes());
                    complementaryFlightSchedule.setArrivalDateTime(complementaryArrivalDateTimeCalendar.getTime());

                    complementaryFlightSchedules.add(complementaryFlightSchedule);
                }

                
                // Associate and persist complementary flight schedule plan and flight schedules
                mainFlightSchedulePlan.setComplementaryFlightSchedulePlan(newComplementaryFlightSchedulePlan);
                newComplementaryFlightSchedulePlan.setMainFlightSchedulePlan(mainFlightSchedulePlan);
                
                newComplementaryFlightSchedulePlan.getFlightSchedules().addAll(complementaryFlightSchedules);
                em.persist(newComplementaryFlightSchedulePlan);

                for (FlightSchedule complementaryFlightSchedule : newComplementaryFlightSchedulePlan.getFlightSchedules())
                {
                    em.persist(complementaryFlightSchedule);
                }

                em.flush();

                
                // Check for overlaps between existing and new flight schedule plans of the return flight
                if (!returnFlight.getFlightSchedulePlans().isEmpty())
                {
                    List<Long> existingFlightSchedulePlanIds = new ArrayList<>();

                    for (FlightSchedulePlan existingFlightSchedulePlan : returnFlight.getFlightSchedulePlans())
                    {
                        existingFlightSchedulePlanIds.add(existingFlightSchedulePlan.getFlightSchedulePlanId());
                    }

                    Query query = em.createQuery("SELECT fsp1.flightSchedulePlanId FROM FlightSchedulePlan fsp1, IN (fsp1.flightSchedules) fs1, FlightSchedulePlan fsp2, IN (fsp2.flightSchedules) fs2 "
                            + "WHERE fsp1.flightSchedulePlanId IN :existingFlightSchedulePlanIds AND fsp2.flightSchedulePlanId = :newFlightSchedulePlanId AND (fs1.departureDateTime <= fs2.arrivalDateTime AND fs1.arrivalDateTime >= fs2.departureDateTime)");
                    query.setParameter("existingFlightSchedulePlanIds", existingFlightSchedulePlanIds);
                    query.setParameter("newFlightSchedulePlanId", newComplementaryFlightSchedulePlan.getFlightSchedulePlanId());

                    List<Long> flightSchedulePlanIdsWithOverlap = query.getResultList();

                    if (!flightSchedulePlanIdsWithOverlap.isEmpty())
                    {
                        // eJBContext.setRollbackOnly();

                        throw new CreateNewFlightSchedulePlanException("Overlap with existing complementary schedules " + flightSchedulePlanIdsWithOverlap.toString() + " for return Flight Number : " + returnFlight.getFlightNumber() + "\n");
                    }
                }

                // Associate new complementary flight schedule plan with the return flight number
                returnFlight.getFlightSchedulePlans().add(newComplementaryFlightSchedulePlan);
                
                return newComplementaryFlightSchedulePlan.getFlightSchedulePlanId();

            }
            else
            {
                throw new CreateNewFlightSchedulePlanException("Error when creating complementary flight schedule plan: Main flight schedule plan does not have any flight schedules.");
            }
        }
        catch(FlightSchedulePlanNotFoundException | FlightNotFoundException ex)
        {
            throw new CreateNewFlightSchedulePlanException("Error when creating complementary flight schedule plan: " + ex.getMessage() + "!");
        }
    }
    
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans()
    {      
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.mainFlightSchedulePlan IS NULL ORDER BY (SELECT MIN(fs.departureDateTime) FROM fsp.flightSchedules fs) ASC");
        
        return query.getResultList();
    }
    
    @Override
    public FlightSchedulePlan retrieveFlightSchedulePlanByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException
    {
        FlightSchedulePlan flightSchedulePlan = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        
        if(flightSchedulePlan != null)
        {
            // Lazily Load to-Many associations
            flightSchedulePlan.getFlightSchedules().size();
            return flightSchedulePlan;
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan ID " + flightSchedulePlanId + " does not exist!");
        }
    }
    
    
    @Override
    public List<FlightSchedule> retrieveFlightSchedulesByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException
    {
        FlightSchedulePlan flightSchedulePlan = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        
        if (flightSchedulePlan != null)
        {
            Query query = em.createQuery("SELECT fs FROM FlightSchedulePlan fsp JOIN fsp.flightSchedules fs WHERE fsp.flightSchedulePlanId = :inFlightSchedulePlanId ORDER BY fs.departureDateTime ASC");
            query.setParameter("inFlightSchedulePlanId", flightSchedulePlanId);
            
            return query.getResultList();
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan ID " + flightSchedulePlanId + " does not exist!");
        }
        
    }
    
    @Override
    public List<Fare> retrieveFaresByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException
    {
        FlightSchedulePlan flightSchedulePlan = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        
        if (flightSchedulePlan != null)
        {
            Query query = em.createQuery("SELECT fa FROM FlightSchedulePlan fsp JOIN fsp.fares fa JOIN fa.cabinClass cc WHERE fsp.flightSchedulePlanId = :inFlightSchedulePlanId ORDER BY cc.cabinClassType");
            query.setParameter("inFlightSchedulePlanId", flightSchedulePlanId);
            
            return query.getResultList();
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan Id " + flightSchedulePlanId + " does not exist!");
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
    
    @Override
    public void linkFlightSchedulePlanToFares(List<Fare> fares, Long flightSchedulePlanId, boolean isComplementary) throws DuplicateFareBasisCodeException, FlightNotFoundException, FlightSchedulePlanNotFoundException
    {
        FlightSchedulePlan flightSchedulePlan = retrieveFlightSchedulePlanByFlightSchedulePlanId(flightSchedulePlanId);
        Flight flight = flightSessionBeanLocal.retrieveFlightByFlightNumber(flightSchedulePlan.getFlightNumber());
        // List<CabinClass> cabinClasses = aircraftConfigurationSessionBeanLocal.retrieveCabinClassesByAircraftConfigurationId(flight.getAircraftConfiguration().getAircraftConfigurationId());
        
        List<String> fareBasisCodes = new ArrayList<>();
        
        for (Fare fare : fares)
        {
            String fareBasisCode = fare.getFareBasisCode();
            
            if (fareBasisCodes.contains(fareBasisCode))
            {
                throw new DuplicateFareBasisCodeException("Duplicate fare basis codes detected, fare basis codes must be different!");
            }
            
            fareBasisCodes.add(fareBasisCode);
        }
        
        if (!isComplementary)
        {
            for (Fare fare : fares)
            {
                em.persist(fare);
                flightSchedulePlan.getFares().add(fare);
            }
            
        } 
        else
        {
            for (Fare fare : fares)
            {
                Fare newComplementaryFare = new Fare();
                newComplementaryFare.setCabinClass(fare.getCabinClass());
                newComplementaryFare.setFareAmount(fare.getFareAmount());
                newComplementaryFare.setFareBasisCode(fare.getFareBasisCode());
                
                em.persist(newComplementaryFare);
                flightSchedulePlan.getFares().add(newComplementaryFare);
            }
        }
    }
}

