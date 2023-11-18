/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightTicket;
import javax.ejb.Local;
import util.exception.FlightTicketIdExistenceException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Local
public interface FlightTicketSessionBeanLocal {
    
    public Long createNewFlightTicket(FlightTicket newFlightTicket) throws FlightTicketIdExistenceException, UnknownPersistenceException;
    
}
