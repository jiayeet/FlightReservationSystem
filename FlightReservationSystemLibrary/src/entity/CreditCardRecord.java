/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 65968
 */
@Entity
public class CreditCardRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardRecordId;
    @Column(nullable = false, length = 16)
    @NotNull
    @Size(min = 16, max = 16)
    private String cardNumber;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 6, max = 32)
    private String pin;
    @Column(nullable = false, length = 1)
    @NotNull
    @Size(min = 1, max = 32)
    private String cardHolderName;
    @Column(nullable = false)
    @NotNull
    private Calendar expirationDate;
    @Column(nullable = false, length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    private String CVV;
    
    /**
     * @return the CVV
     */
    public String getCVV() {
        return CVV;
    }

    /**
     * @param CVV the CVV to set
     */
    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    
    
    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the cardHolderName
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * @param cardHolderName the cardHolderName to set
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * @return the expirationDate
     */
    public Calendar getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public CreditCardRecord() {
        
    }

    public Long getCreditCardRecordId() {
        return creditCardRecordId;
    }

    public void setCreditCardRecordId(Long creditCardRecordId) {
        this.creditCardRecordId = creditCardRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardRecordId != null ? creditCardRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the creditCardRecordId fields are not set
        if (!(object instanceof CreditCardRecord)) {
            return false;
        }
        CreditCardRecord other = (CreditCardRecord) object;
        if ((this.creditCardRecordId == null && other.creditCardRecordId != null) || (this.creditCardRecordId != null && !this.creditCardRecordId.equals(other.creditCardRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCardRecord[ id=" + creditCardRecordId + " ]";
    }
    
}
