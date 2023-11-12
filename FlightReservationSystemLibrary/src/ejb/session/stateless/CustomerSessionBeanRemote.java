/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentials;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Remote
public interface CustomerSessionBeanRemote {

    public Long createNewCustomer(Customer newCustomer) throws CustomerUsernameExistException, UnknownPersistenceException;

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentials;
    
}
