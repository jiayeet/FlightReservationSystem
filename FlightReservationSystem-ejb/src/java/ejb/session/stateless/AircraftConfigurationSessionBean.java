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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CabinClassMaxSeatCapacityExceededException;
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
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration) throws AircraftTypeNotFoundException, AircraftTypeMaxSeatCapacityExceededException, CreateNewAircraftConfigurationException, CabinClassMaxSeatCapacityExceededException
    {
        if (newAircraftConfiguration != null)
        {
            // Retrieving Aircraft Type name from AircraftConfiguration name (Boeing ***)
            AircraftType aircraftType = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByAircraftTypeName(newAircraftConfiguration.getName().substring(0, 10));

            int totalMaximumSeatCapacity = 0;

            for (CabinClass cabinClass : newAircraftConfiguration.getCabinClasses()) {
                int totalCabinClassSeatCapacity = cabinClass.getNumOfRows() * cabinClass.getNumOfSeatsAbreast();
                
                if (totalCabinClassSeatCapacity > cabinClass.getMaxCapacity())
                {
                    throw new CabinClassMaxSeatCapacityExceededException("Total seats exceed maximum seat capacity of a cabin class!");
                }
                
                totalMaximumSeatCapacity += totalCabinClassSeatCapacity;
            }

            System.out.println("Aircraft Type Max Seat Capacity: " + aircraftType.getMaxSeatCapacity());
            
            if (totalMaximumSeatCapacity > aircraftType.getMaxSeatCapacity()) {
                throw new AircraftTypeMaxSeatCapacityExceededException("Total seat capacity of all cabin classes exceeds maximum seat capacity of Aircraft Type!");
            }
            else if (totalMaximumSeatCapacity > newAircraftConfiguration.getMaximumCapacity())
            {
                throw new AircraftTypeMaxSeatCapacityExceededException("Total seat capacity of all cabin classes exceeds maximum seat capacity of Aircraft Configuration!");
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

    @Override
    public List<CabinClass> retrieveCabinClassesByAircraftConfigurationId(Long aircraftConfigurationId)
    {
        Query query = em.createQuery("SELECT cc FROM AircraftConfiguration ac, IN (ac.cabinClasses) cc WHERE ac.aircraftConfigurationId = :inAircraftConfigurationId");
        query.setParameter("inAircraftConfigurationId", aircraftConfigurationId);
        
        return query.getResultList();
    }
    
    public AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException
    {
        Query query = em.createQuery("SELECT a FROM AircraftConfiguration a WHERE a.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (AircraftConfiguration)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AircraftConfigurationNotFoundException("Aircraft Configuration name " + name + " does not exist!");
        }
    }
    
}
