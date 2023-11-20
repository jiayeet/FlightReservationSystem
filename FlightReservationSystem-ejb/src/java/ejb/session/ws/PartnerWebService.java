/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentials;

/**
 *
 * @author 65968
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public void partnerLogin(@WebParam(name = "username") String username,
                         @WebParam(name = "password") String password) throws InvalidLoginCredentials {
        //try {
            partnerSessionBeanLocal.partnerLogin(username, password);
        /*}
        catch(InvalidLoginCredentials ex) {
            throw new InvalidLoginCredentials("Username does not exist or password is invalid!");
        }*/
    }
}
