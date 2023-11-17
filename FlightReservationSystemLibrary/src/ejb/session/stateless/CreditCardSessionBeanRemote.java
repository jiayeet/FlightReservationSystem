/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.CreditCardRecord;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Remote
public interface CreditCardSessionBeanRemote {

    public Long createNewCreditCard(CreditCardRecord newCreditCard) throws InputDataValidationException;
    
}
