/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author 65968
 */
@Local
public interface FlightReservationSessionBeanLocal {
    
    public Long createNewFlightReservation(FlightReservation newFlightReservation);
    
    public FlightReservation retrieveFlightReservationByFlightReservationId(Long flightReservationId) throws FlightReservationNotFoundException;
    
    public List<FlightReservation> retrieveAllFlightReservations();
    
}
