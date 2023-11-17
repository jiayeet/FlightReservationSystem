/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Passenger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.PassengerPassportNumberExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Stateless
public class PassengerSessionBean implements PassengerSessionBeanRemote, PassengerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public Long createNewPassenger(Passenger newPassenger)
    {
        em.persist(newPassenger);
        em.flush();
        
        return newPassenger.getPassengerId();
    }
}
