/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CreateNewAircraftConfigurationException;

/**
 *
 * @author wee shang
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;
    
    
    public AircraftConfigurationSessionBean() 
    {
    }

    
    @Override
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration) throws AircraftTypeNotFoundException, AircraftTypeMaxSeatCapacityExceededException, CreateNewAircraftConfigurationException
    {
        if (newAircraftConfiguration != null)
        {
            // Retrieving Aircraft Type name from AircraftConfiguration name (Boeing ***)
            AircraftType aircraftType = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByAircraftTypeName(newAircraftConfiguration.getName().substring(0, 10));

            Integer totalMaximumSeatCapacity = 0;

            for (CabinClass cabinClass : newAircraftConfiguration.getCabinClasses()) {
                totalMaximumSeatCapacity += cabinClass.getNumOfRows() * cabinClass.getNumOfSeatsAbreast();
            }

            if (totalMaximumSeatCapacity > aircraftType.getMaxSeatCapacity()) {
                throw new AircraftTypeMaxSeatCapacityExceededException("Total maximum seat capacity of Aircraft Configuration exceeds maximum seat capacity of Aircraft Type!");
            }

            // Associating entities and persisting new entity instances where necessary
            newAircraftConfiguration.setAircraftType(aircraftType);

            em.persist(newAircraftConfiguration);

            for (CabinClass cabinClass : newAircraftConfiguration.getCabinClasses()) {
                // Already associated aircraft configurations with cabin classes in client
                em.persist(cabinClass);
            }

            em.flush();

            return newAircraftConfiguration;

        }
        else 
        {
            throw new CreateNewAircraftConfigurationException("Aircraft Configuration information not provided");
        }

    }

    
    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations() 
    {
        Query query = em.createQuery("SELECT ac FROM AircraftConfiguration ac JOIN ac.aircraftType act ORDER BY act.name ASC, ac.name ASC");
        
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
            throw new AircraftConfigurationNotFoundException("Aircraft Configuration ID " + aircraftConfigurationId + " does not exist!");
        }
    }
    
    
}
