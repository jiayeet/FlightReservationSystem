/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Local;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author 65968
 */
@Local
public interface AircraftTypeSessionBeanLocal {

    public Long createNewAircraft(AircraftType newAircraftType);

    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException;
    
}
