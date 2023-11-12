/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;

/**
 *
 * @author wee shang
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    
    public AircraftConfigurationSessionBean() 
    {
    }

    
    
    
    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations() 
    {
        Query query = em.createQuery("SELECT ac FROM AircraftConfiguration ac, IN (ac.aircrafttype) at ORDER BY at.name ASC, ac.name ASC");
        
        return query.getResultList();
    }
    
    @Override
    public AircraftConfiguration retrieveAircraftConfigurationByAircraftConfigurationId(Long aircraftConfigurationId) throws AircraftConfigurationNotFoundException
    {
        AircraftConfiguration aircraftConfiguration = em.find(AircraftConfiguration.class, aircraftConfigurationId);
        
        if (aircraftConfiguration != null)
        {
            // Lazy load OneToMany Cabin Classes
            aircraftConfiguration.getCabinClasses().size();
            
            return aircraftConfiguration;
        }
        else 
        {
            throw new AircraftConfigurationNotFoundException("Aircraft Configuration ID " + aircraftConfigurationId + "does not exist!");
        }
    }
    
    
}
