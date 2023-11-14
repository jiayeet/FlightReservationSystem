/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;

/**
 *
 * @author 65968
 */
@Remote
public interface AirportSessionBeanRemote {
    
    public Long createNewAirport(Airport newAirport);
    
    public List<Airport> retrieveAllAirports();
    
    public Airport retrieveAirportByAirportId(Long airportId) throws AirportNotFoundException;
    
    public Airport retrieveAirportByAirportName(String airportName) throws AirportNotFoundException;
}
