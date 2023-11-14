/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.CabinClassType;

/**
 *
 * @author wee shang
 */
@Entity
public class CabinClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClassType cabinClassType;
    @Column(nullable = false)
    private Integer numOfAisles;
    @Column(nullable = false)
    private Integer numOfRows;
    @Column(nullable = false)
    private Integer numOfSeatsAbreast;
    @Column(nullable = false, length = 5)
    private String seatConfiguration;

    
    
    public CabinClass() {
    }
    

    
    public Long getCabinClassId() {
        return cabinClassId;
    }

    public void setCabinClassId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassId != null ? cabinClassId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassId fields are not set
        if (!(object instanceof CabinClass)) {
            return false;
        }
        CabinClass other = (CabinClass) object;
        if ((this.cabinClassId == null && other.cabinClassId != null) || (this.cabinClassId != null && !this.cabinClassId.equals(other.cabinClassId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + cabinClassId + " ]";
    }

    /**
     * @return the numOfAisles
     */
    public Integer getNumOfAisles() {
        return numOfAisles;
    }

    /**
     * @param numOfAisles the numOfAisles to set
     */
    public void setNumOfAisles(Integer numOfAisles) {
        this.numOfAisles = numOfAisles;
    }

    /**
     * @return the numOfRows
     */
    public Integer getNumOfRows() {
        return numOfRows;
    }

    /**
     * @param numOfRows the numOfRows to set
     */
    public void setNumOfRows(Integer numOfRows) {
        this.numOfRows = numOfRows;
    }

    /**
     * @return the numOfSeatsAbreast
     */
    public Integer getNumOfSeatsAbreast() {
        return numOfSeatsAbreast;
    }

    /**
     * @param numOfSeatsAbreast the numOfSeatsAbreast to set
     */
    public void setNumOfSeatsAbreast(Integer numOfSeatsAbreast) {
        this.numOfSeatsAbreast = numOfSeatsAbreast;
    }

    /**
     * @return the seatConfiguration
     */
    public String getSeatConfiguration() {
        return seatConfiguration;
    }

    /**
     * @param seatConfiguration the seatConfiguration to set
     */
    public void setSeatConfiguration(String seatConfiguration) {
        this.seatConfiguration = seatConfiguration;
    }

    /**
     * @return the cabinClassType
     */
    public CabinClassType getCabinClassType() {
        return cabinClassType;
    }

    /**
     * @param cabinClassType the cabinClassType to set
     */
    public void setCabinClassType(CabinClassType cabinClassType) {
        this.cabinClassType = cabinClassType;
    }
    
}
