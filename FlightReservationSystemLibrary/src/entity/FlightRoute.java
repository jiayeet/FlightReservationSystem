/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

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
    @Column(nullable = false)
    @NotNull
    private Boolean enabled;
    @Column(nullable = false)
    @NotNull
    private Boolean isMain;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "originAirportId")
    private Airport airportOrigin;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "destinationAirportId")
    private Airport airportDestination;
    
    @OneToMany(mappedBy = "flightRoute")
    private List<Flight> flights;
    
    @OneToOne
    @JoinColumn(name = "complementaryFlightRouteId")
    private FlightRoute complementaryFlightRoute;
    
    public FlightRoute() {
        flights = new ArrayList<>();
    }
    
    /**
     * @return the complementaryFlightRoute
     */
    public FlightRoute getComplementaryFlightRoute() {
        return complementaryFlightRoute;
    }

    /**
     * @param complementaryFlightRoute the complementaryFlightRoute to set
     */
    public void setComplementaryFlightRoute(FlightRoute complementaryFlightRoute) {
        this.complementaryFlightRoute = complementaryFlightRoute;
    }
    
    /**
     * @return the flights
     */
    public Boolean getIsMain() {
        return isMain;
    }

    /**
     * @param flights the flights to set
     */
    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }
    
    /**
     * @return the flights
     */
    public List<Flight> getFlights() {
        return flights;
    }

    /**
     * @param flights the flights to set
     */
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
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
        return airportDestination.getIataAirportCode();
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
