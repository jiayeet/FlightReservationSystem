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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import util.enumeration.FlightType;
import util.enumeration.TripType;

/**
 *
 * @author 65968
 */
@Entity
public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripType tripType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightType flightType;
    
    @OneToMany
    private List<FlightTicket> flightTickets;
    
    @ManyToMany(mappedBy = "inBoundFlightReservations")
    private List<FlightSchedule> inBoundFlightSchedules;
    
    @ManyToMany(mappedBy = "outBoundFlightReservations")
    private List<FlightSchedule> outBoundFlightSchedules;
    
    @ManyToMany(mappedBy = "flightReservations")
    private List<CabinClass> cabinClasses;


    public FlightReservation() {
        flightTickets = new ArrayList<>();
        inBoundFlightSchedules = new ArrayList<>();
        outBoundFlightSchedules = new ArrayList<>();
        cabinClasses = new ArrayList();
    }
    
    /**
     * @return the tripType
     */
    public TripType getTripType() {
        return tripType;
    }

    /**
     * @param tripType the tripType to set
     */
    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    /**
     * @return the flightType
     */
    public FlightType getFlightType() {
        return flightType;
    }

    /**
     * @param flightType the flightType to set
     */
    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }
    
    /**
     * @return the flightTickets
     */
    public List<FlightTicket> getFlightTickets() {
        return flightTickets;
    }

    /**
     * @param flightTickets the flightTickets to set
     */
    public void setFlightTickets(List<FlightTicket> flightTickets) {
        this.flightTickets = flightTickets;
    }

    /**
     * @return the inBoundFlightSchedules
     */
    /*public List<FlightSchedule> getInBoundFlightSchedules() {
        return inBoundFlightSchedules;
    }

    /**
     * @param inBoundFlightSchedules the inBoundFlightSchedules to set
     */
    /*public void setInBoundFlightSchedules(List<FlightSchedule> inBoundFlightSchedules) {
        this.inBoundFlightSchedules = inBoundFlightSchedules;
    }

    /**
     * @return the outBoundFlightSchedules
     */
    /*public List<FlightSchedule> getOutBoundFlightSchedules() {
        return outBoundFlightSchedules;
    }

    /**
     * @param outBoundFlightSchedules the outBoundFlightSchedules to set
     */
    /*public void setOutBoundFlightSchedules(List<FlightSchedule> outBoundFlightSchedules) {
        this.outBoundFlightSchedules = outBoundFlightSchedules;
    }*/

    

    public Long getFlightReservationId() {
        return flightReservationId;
    }

    public void setFlightReservationId(Long flightReservationId) {
        this.flightReservationId = flightReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightReservationId != null ? flightReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightReservationId fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.flightReservationId == null && other.flightReservationId != null) || (this.flightReservationId != null && !this.flightReservationId.equals(other.flightReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightReservation[ id=" + flightReservationId + " ]";
    }
    
}
