/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Remote;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author 65968
 */
@Remote
public interface AircraftTypeSessionBeanRemote {
    
    public Long createNewAircraft(AircraftType newAircraftType);
    
    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException;
    
}
