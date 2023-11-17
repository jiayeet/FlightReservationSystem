/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightTicket;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.FlightTicketIdExistenceException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightTicketSessionBean implements FlightTicketSessionBeanRemote, FlightTicketSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewFlightTicket(FlightTicket newFlightTicket)
    {
        em.persist(newFlightTicket);
        em.flush();
        
        return newFlightTicket.getFlightTicketId();
    }
}
