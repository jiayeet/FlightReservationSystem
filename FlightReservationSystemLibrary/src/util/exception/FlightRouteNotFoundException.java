/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author 65968
 */
public class FlightRouteNotFoundException extends Exception
{    
    public FlightRouteNotFoundException()
    {
    }
    
    public FlightRouteNotFoundException(String msg)
    {
        super(msg);
    }
}
