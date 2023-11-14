/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author 65968
 */
@Entity
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
    private Boolean enabled;
    
    @ManyToOne
    @JoinColumn(name = "originAirportId")
    private Airport airportOrigin;
    @ManyToOne
    @JoinColumn(name = "destinationAirportId")
    private Airport airportDestination;
    
    //Remember to encapsulate
    /*@OneToMany(mappedBy = "flightroutes")
    private List<Flight> flights;
    */
    
    public FlightRoute() {
        //flights = new ArrayList<>();
    }
    
    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    
    /**
     * @return the airportOrigin
     */
    public Airport getAirportOrigin() {
        return airportOrigin;
    }
    
    public String originIATA() {
        return airportOrigin.getIataAirportCode();
    }
    
    public String destinationIATA() {
        return airportOrigin.getIataAirportCode();
    }

    /**
     * @param airportOrigin the airportOrigin to set
     */
    public void setAirportOrigin(Airport airportOrigin) {
        this.airportOrigin = airportOrigin;
    }

    /**
     * @return the airportDestination
     */
    public Airport getAirportDestination() {
        return airportDestination;
    }

    /**
     * @param airportDestination the airportDestination to set
     */
    public void setAirportDestination(Airport airportDestination) {
        this.airportDestination = airportDestination;
    }

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightRouteId fields are not set
        if (!(object instanceof FlightRoute)) {
            return false;
        }
        FlightRoute other = (FlightRoute) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRoute[ id=" + flightRouteId + " ]";
    }
    
}
