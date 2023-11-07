/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentials;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 65968
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException;
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentials;
}
