package com.billing.ng.entities;

import com.billing.ng.util.HashUtils;
import org.hibernate.validator.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Customer
 *
 * @author Brian Cowdery
 * @since 23-Apr-2010
 */
@Entity
@XmlTransient
public class Customer extends BaseEntity {

    public enum Gender { MALE, FEMALE }

    @Id @GeneratedValue
    private Long id;

    @Column
    private String userName;
    @Column
    private String passwordHash;
    @Column
    private String hashSalt;
    
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String initial;
    @Column @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column @Email
    private String email;
    @Column
    private String homePhoneNumber;
    @Column
    private String mobilePhoneNumber;
    @Column
    private String workPhoneNumber;

    @Column
    private boolean useMailingAddress = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets the password for this user, converting the given password into a one-way hash
     * to be stored in the database.
     *
     * @see HashUtils#generateHash(String, String...)
     * @param password password to set
     */
    public void setPassword(String password) {
        setPasswordHash(HashUtils.generateHash(password, getHashSalt()));
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the hash salt for this user. If this user does not have a pre-determined salt, one will be generated.
     *
     * This salt is designed to be used for generating hashed passwords and unique identifier tokens. Hash salt's
     * should NEVER be changed after the initial creation since it will invalidate all hashes and identifiers
     * that use the salt as a base.
     *
     * @see HashUtils#generateHashSalt(int)
     * @return hash salt
     */
    public String getHashSalt() {
        if (hashSalt == null)
            hashSalt = HashUtils.generateHashSalt(10);

        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public boolean useMailingAddress() {
        return useMailingAddress;
    }

    public boolean getUseMailingAddress() {
        return useMailingAddress;
    }

    public void setUseMailingAddress(boolean useMailingAddress) {
        this.useMailingAddress = useMailingAddress;
    }
}
