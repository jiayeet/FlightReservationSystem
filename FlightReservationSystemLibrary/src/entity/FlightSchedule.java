/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author 65968
 */
@Entity
public class FlightSchedule implements Serializable {

     //Remember to encapsulate classes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date departureDateTime;
    @Column(nullable = false)
    private int flightDurationHours;
    @Column(nullable = false)
    private int flightDurationMinutes;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date arrivalDateTime;
    @ManyToOne
    @JoinColumn(name = "flightSchedulePlanId")
    private FlightSchedulePlan flightSchedulePlan;
    
    @ManyToMany
    private List<FlightReservation> inBoundFlightReservations;
    
    @ManyToMany
    private List<FlightReservation> outBoundFlightReservations;

    public FlightSchedule() {
        inBoundFlightReservations = new ArrayList<>();
        outBoundFlightReservations = new ArrayList<>();
    }
    
    /**
     * @return the inBoundFlightReservations
     */
    public List<FlightReservation> getInBoundFlightReservations() {
        return inBoundFlightReservations;
    }

    /**
     * @param inBoundFlightReservations the inBoundFlightReservations to set
     */
    public void setInBoundFlightReservations(List<FlightReservation> inBoundFlightReservations) {
        this.inBoundFlightReservations = inBoundFlightReservations;
    }

    /**
     * @return the outBoundFlightReservations
     */
    public List<FlightReservation> getOutBoundFlightReservations() {
        return outBoundFlightReservations;
    }

    /**
     * @param outBoundFlightReservations the outBoundFlightReservations to set
     */
    public void setOutBoundFlightReservations(List<FlightReservation> outBoundFlightReservations) {
        this.outBoundFlightReservations = outBoundFlightReservations;
    }
    
    /**
     * @return the flightSchedulePlan
     */
    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    /**
     * @param flightSchedulePlan the flightSchedulePlan to set
     */
    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleId + " ]";
    }

    /**
     * @return the departureDateTime
     */
    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * @param departureDateTime the departureDateTime to set
     */
    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    /**
     * @return the flightDurationHours
     */
    public int getFlightDurationHours() {
        return flightDurationHours;
    }

    /**
     * @param flightDurationHours the flightDurationHours to set
     */
    public void setFlightDurationHours(int flightDurationHours) {
        this.flightDurationHours = flightDurationHours;
    }

    /**
     * @return the flightDurationMinutes
     */
    public int getFlightDurationMinutes() {
        return flightDurationMinutes;
    }

    /**
     * @param flightDurationMinutes the flightDurationMinutes to set
     */
    public void setFlightDurationMinutes(int flightDurationMinutes) {
        this.flightDurationMinutes = flightDurationMinutes;
    }

    /**
     * @return the arrivalDateTime
     */
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    /**
     * @param arrivalDateTime the arrivalDateTime to set
     */
    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
    
}
