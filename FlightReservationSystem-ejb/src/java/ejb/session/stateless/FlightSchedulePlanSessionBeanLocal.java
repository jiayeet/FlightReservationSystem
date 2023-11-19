/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.DuplicateFareBasisCodeException;
import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface FlightSchedulePlanSessionBeanLocal {
    
    public Long createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException;
    
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();
    
    public FlightSchedulePlan retrieveFlightSchedulePlanByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException;
    
    public void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, UpdateFlightSchedulePlanException;
    
    public void deleteFlight(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException;

    public Long createComplementaryFlightSchedulePlan(Long mainFlightSchedulePlanId, int layoverDuration, String returnFlightNumber) throws CreateNewFlightSchedulePlanException;

    public void linkFlightSchedulePlanToFares(List<Fare> fares, Long flightSchedulePlanId, boolean isComplementary) throws DuplicateFareBasisCodeException, FlightNotFoundException, FlightSchedulePlanNotFoundException;

    public List<FlightSchedule> retrieveFlightSchedulesByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException;

    public List<Fare> retrieveFaresByFlightSchedulePlanId(Long flightSchedulePlanId) throws FlightSchedulePlanNotFoundException;
}
