/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import javax.validation.constraints.Size;

/**
 *
 * @author 65968
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    @Column(length = 10)
    //@NotNull
    //@Size(min = 1, max = 10)
    private String flightNumber;
    //@Column(nullable = false)
    //@NotNull
    private Boolean enabled;
    //@Column(nullable = false)
    //@NotNull
    private Boolean isMain;
    
    @OneToOne
    @JoinColumn(name = "aircraftconfig_id")
    private AircraftConfiguration aircraftConfiguration;
    
    @ManyToOne
    @JoinColumn(name = "flightRouteId")
    private FlightRoute flightRoute;
    
    @OneToOne
    @JoinColumn(name = "complementaryFlight")
    private Flight complementaryFlight;
    
    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlan> flightSchedulePlans;
    
    public Flight() {
        flightSchedulePlans = new ArrayList<>();
    }
    
    /**
     * @return the complementaryFlight
     */
    public Flight getComplementaryFlight() {
        return complementaryFlight;
    }

    /**
     * @param complementaryFlight the complementaryFlight to set
     */
    public void setComplementaryFlight(Flight complementaryFlight) {
        this.complementaryFlight = complementaryFlight;
    }
    
    
    

    /**
     * @return the flightSchedulePlans
     */
    public List<FlightSchedulePlan> getFlightSchedulePlans() {
        return flightSchedulePlans;
    }

    /**
     * @param flightSchedulePlans the flightSchedulePlans to set
     */
    public void setFlightSchedulePlans(List<FlightSchedulePlan> flightSchedulePlans) {
        this.flightSchedulePlans = flightSchedulePlans;
    }
    
    
    /**
     * @return the aircraftConfiguration
     */
    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    /**
     * @param aircraftConfiguration the aircraftConfiguration to set
     */
    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }
    
    /**
     * @return the flightRoute
     */
    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    /**
     * @param flightRoute the flightRoute to set
     */
    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    } 
    
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightId fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightId == null && other.flightId != null) || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightId + " ]";
    }

    /**
     * @return the flightNumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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
     * @return the enabled
     */
    public Boolean getIsMain() {
        return isMain;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setIsMain(Boolean enabled) {
        this.isMain = enabled;
    }
    
}
