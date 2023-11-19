/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import javax.ejb.Local;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author 65968
 */
@Local
public interface FlightScheduleSessionBeanLocal {
    
    public FlightSchedule retrieveFlightScheduleByFlightScheduleId(Long flightScheduleId) throws FlightScheduleNotFoundException;
    
}
