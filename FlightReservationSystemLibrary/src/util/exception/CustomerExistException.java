/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author 65968
 */
public class CustomerExistException extends Exception {
    
    public CustomerExistException()
    {
    }
    
    public CustomerExistException(String msg)
    {
        super(msg);
    }
}
