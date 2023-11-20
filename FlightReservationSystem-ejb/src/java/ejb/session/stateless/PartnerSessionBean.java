/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentials;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author 65968
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPartner(Partner newPartner) 
    {
        em.persist(newPartner);
        em.flush();
        
        return newPartner.getPartnerId();
    }
    
    @Override
    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException
    {
        Partner partner = em.find(Partner.class, partnerId);
        
        if(partner != null)
        {
            return partner;
        }
        else
        {
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }
    
    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException
    {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Partner)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentials {
        
        try
        {
            Partner partner = retrievePartnerByUsername(username);
            
            if(partner.getPassword().equals(password))
            {               
                return partner;
            }
            else
            {
                throw new InvalidLoginCredentials("Username does not exist or invalid password!");
            }
        }
        catch(PartnerNotFoundException ex)
        {
            throw new InvalidLoginCredentials("Username does not exist or invalid password!");
        }
    }
}
