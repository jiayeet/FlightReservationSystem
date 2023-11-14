/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author 65968
 */
@Remote
public interface FlightRouteSessionBeanRemote {
    
    public Long createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException;
    
    public FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId) throws FlightRouteNotFoundException;
    
    public List<FlightRoute> retrieveAllFlightRoutes();
    
    public void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException, DeleteFlightRouteException;
    
}
