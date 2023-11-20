/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentials;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author 65968
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(Partner newPartner);
    
    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException;

    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentials;
}
