/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CreditCardRecord;
import javax.ejb.Local;
import util.exception.InputDataValidationException;

/**
 *
 * @author 65968
 */
@Local
public interface CreditCardSessionBeanLocal {
            
    public Long createNewCreditCard(CreditCardRecord newCreditCard) throws InputDataValidationException;
    
}
