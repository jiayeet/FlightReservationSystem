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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 65968
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String lastName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String mobileNumber;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String email;
    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min = 1, max = 100)
    private String address;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 6, max = 32)
    private String username;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 6, max = 32)
    private String password;
    
    @OneToMany
    @JoinColumn(name = "customerId")
    private List<FlightReservation> flightReservations;
    
    @OneToOne
    @JoinColumn(name = "creditCardRecordId")
    private CreditCardRecord creditCardRecord;
    
    /*@OneToMany(mappedBy = "customer")
    private List<Passenger> passengers;*/


    public Customer() {
        flightReservations = new ArrayList<>();
        //passengers = new ArrayList();
    }
    
    /**
     * @return the passengers
     */
    /*public List<Passenger> getPassengers() {
        return passengers;
    }

    /**
     * @param passengers the passengers to set
     */
    /*public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
    
    
    /**
     * @return the creditCardRecord
     */
    public CreditCardRecord getCreditCardRecord() {
        return creditCardRecord;
    }

    /**
     * @param creditCardRecord the creditCardRecord to set
     */
    public void setCreditCardRecord(CreditCardRecord creditCardRecord) {
        this.creditCardRecord = creditCardRecord;
    }

    /**
     * @return the flightReservations
     */
    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    /**
     * @param flightReservations the flightReservations to set
     */
    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }


    public Customer(String firstName, String lastName, String mobileNumber, String address, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }
    
}
