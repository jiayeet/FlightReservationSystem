/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CreateNewAircraftConfigurationException;

/**
 *
 * @author wee shang
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations();
    
    public AircraftConfiguration retrieveAircraftConfigurationByAircraftConfigurationId(Long aircraftConfigurationId) throws AircraftConfigurationNotFoundException;
    
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration) throws AircraftTypeNotFoundException, AircraftTypeMaxSeatCapacityExceededException, CreateNewAircraftConfigurationException;
}
