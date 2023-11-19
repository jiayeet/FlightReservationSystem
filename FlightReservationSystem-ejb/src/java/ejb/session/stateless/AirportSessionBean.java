/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AirportNotFoundException;

/**
 *
 * @author 65968
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewAirport(Airport newAirport) 
    {
        em.persist(newAirport);
        em.flush();
        
        return newAirport.getAirportId();
    }
    
    @Override
    public List<Airport> retrieveAllAirports()
    {
        Query query = em.createQuery("SELECT a FROM Airport a");
        return query.getResultList();
    }
    
    @Override
    public Airport retrieveAirportByAirportName(String airportName) throws AirportNotFoundException
    {
        Query query = em.createQuery("SELECT a FROM Airport a WHERE a.airportName = :inAirportName");
        query.setParameter("inAirportName", airportName);
        
        try
        {
            return (Airport)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AirportNotFoundException("Airport name " + airportName + " does not exist!");
        }
    }
    
    @Override
    public Airport retrieveAirportByAirportId(Long airportId) throws AirportNotFoundException
    {
        Airport airport = em.find(Airport.class, airportId);
        
        if(airport != null)
        {
            return airport;
        }
        else
        {
            throw new AirportNotFoundException("Airport ID " + airport + " does not exist");
        }
    }
}
