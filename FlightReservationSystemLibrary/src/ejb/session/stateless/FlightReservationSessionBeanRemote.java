/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author 65968
 */
@Remote
public interface FlightReservationSessionBeanRemote {

    public Long createNewFlightReservation(FlightReservation newFlightReservation);

    public FlightReservation retrieveFlightReservationByFlightReservationId(Long flightReservationId) throws FlightReservationNotFoundException;
    
    public List<FlightReservation> retrieveAllFlightReservations();
    
}
