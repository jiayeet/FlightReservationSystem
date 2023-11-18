/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Passenger;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.PassengerPassportNumberExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Remote
public interface PassengerSessionBeanRemote {

    public Long createNewPassenger(Passenger newPassenger) throws InputDataValidationException;
    
}
