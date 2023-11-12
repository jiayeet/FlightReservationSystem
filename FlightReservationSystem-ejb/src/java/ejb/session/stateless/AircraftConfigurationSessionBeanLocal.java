/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Local;
import util.exception.AircraftConfigurationNotFoundException;

/**
 *
 * @author wee shang
 */
@Local
public interface AircraftConfigurationSessionBeanLocal {

    public List<AircraftConfiguration> retrieveAllAircraftConfigurations();

    public AircraftConfiguration retrieveAircraftConfigurationByAircraftConfigurationId(Long aircraftConfigurationId) throws AircraftConfigurationNotFoundException;
    
}
