/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author 65968
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewAircraft(AircraftType newAircraftType) 
    {
        em.persist(newAircraftType);
        em.flush();
        
        return newAircraftType.getAircraftTypeId();
    }
    
    @Override
    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException
    {
        Query query = em.createQuery("SELECT act FROM AircraftType act WHERE act.name = :inName");
        query.setParameter("inName", aircraftTypeName);
        
        try 
        {
            return (AircraftType)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AircraftTypeNotFoundException("Aircraft Type Name " + aircraftTypeName + " does not exist!");
        }
    }
}
