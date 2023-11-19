/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlightException;

/**
 *
 * @author 65968
 */
@Local
public interface FlightSessionBeanLocal {
    
    public Long createNewFlight(Flight flight) throws FlightExistException, GeneralException;
    
    public List<Flight> retrieveAllFlights();
    
    public Flight retrieveFlightByFlightId(Long flightId) throws FlightNotFoundException;
    
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException;
    
    public void updateFlight(Flight flight) throws AircraftConfigurationNotFoundException, FlightNotFoundException, UpdateFlightException;
    
    public void deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException;
    
}
