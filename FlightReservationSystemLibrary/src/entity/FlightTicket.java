/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 65968
 */
@Entity
public class FlightTicket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightTicketId;
    @Column(nullable = false, length = 4)
    @NotNull
    @Size(max = 4)
    private String seatNumber;
    
    @OneToOne
    @JoinColumn(name = "cabinClassId", nullable = false)
    private CabinClass cabinClass;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;
    
    @OneToOne
    @JoinColumn(name = "passenggerId", nullable = false)
    private Passenger passenger;
    
    @ManyToOne
    @JoinColumn(name = "flightReservationId", nullable = false)
    private FlightReservation flightReservation;

    public FlightTicket() {
    }
    
    
    /**
     * @return the flightReservation
     */
    public FlightReservation getFlightReservation() {
        return flightReservation;
    }

    /**
     * @param flightReservation the flightReservation to set
     */
    public void setFlightReservation(FlightReservation flightReservation) {
        this.flightReservation = flightReservation;
    }
    
    /**
     * @return the seatNumber
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * @param seatNumber the seatNumber to set
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * @return the cabinClass
     */
    public CabinClass getCabinClass() {
        return cabinClass;
    }

    /**
     * @param cabinClass the cabinClass to set
     */
    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    /**
     * @return the flightSchedule
     */
    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    /**
     * @param flightSchedule the flightSchedule to set
     */
    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    /**
     * @return the passenger
     */
    public Passenger getPassenger() {
        return passenger;
    }

    /**
     * @param passenger the passenger to set
     */
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Long getFlightTicketId() {
        return flightTicketId;
    }

    public void setFlightTicketId(Long flightTicketId) {
        this.flightTicketId = flightTicketId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getFlightTicketId() != null ? getFlightTicketId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightTicketId fields are not set
        if (!(object instanceof FlightTicket)) {
            return false;
        }
        FlightTicket other = (FlightTicket) object;
        if ((this.getFlightTicketId() == null && other.getFlightTicketId() != null) || (this.getFlightTicketId() != null && !this.flightTicketId.equals(other.flightTicketId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightTicket[ id=" + getFlightTicketId() + " ]";
    }
    
}
