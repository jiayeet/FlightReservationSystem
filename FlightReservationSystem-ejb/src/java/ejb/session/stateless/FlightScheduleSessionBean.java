/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author 65968
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public FlightSchedule retrieveFlightScheduleByFlightScheduleId(Long flightScheduleId) throws FlightScheduleNotFoundException
    {
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
        
        if(flightSchedule != null)
        {
            return flightSchedule;
        }
        else
        {
            throw new FlightScheduleNotFoundException("Flight Schedule ID " + flightScheduleId + " does not exist!");
        }
    }
}
