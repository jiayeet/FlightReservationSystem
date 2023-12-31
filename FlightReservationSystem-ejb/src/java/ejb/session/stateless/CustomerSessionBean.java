/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentials;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerUsernameExistException, UnknownPersistenceException
    {
        try {
        em.persist(newCustomer);
        em.flush();
        
        return newCustomer.getCustomerId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new CustomerUsernameExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        
        if(customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentials {
        
        try
        {
            Customer customer = retrieveCustomerByUsername(username);
            
            if(customer.getPassword().equals(password))
            {               
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentials("Username does not exist or invalid password!");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentials("Username does not exist or invalid password!");
        }
    }
}
