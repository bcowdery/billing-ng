/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2010 Brian Cowdery

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see http://www.gnu.org/licenses/agpl-3.0.html
 */

package com.billing.ng.entities;

import com.billing.ng.util.HashUtils;
import org.hibernate.validator.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customer
 *
 * @author Brian Cowdery
 * @since 23-Apr-2010
 */
@MappedSuperclass
@XmlTransient
public class User extends BaseEntity {

    public enum Gender { MALE, FEMALE }

    public enum Salutation {
        MR  (Gender.MALE),
        MS  (Gender.FEMALE),
        MRS (Gender.FEMALE);

        private Gender gender;
        Salutation(Gender gender) { this.gender = gender; }
        public Gender getGender() { return gender; }

        public static List<Salutation> fromGender(Gender gender) {
            List<Salutation> salutations = new ArrayList<Salutation>();
            for (Salutation salutation : values())
                if (salutation.gender.equals(gender))
                    salutations.add(salutation);
            return salutations;
        }
    }

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
    @Column @Enumerated(EnumType.STRING)
    private Salutation salutation;
   
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

    public Salutation getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    @Transient
    public List<Salutation> getSalutations() {
        if (gender == null)
            return Collections.emptyList();
        return Salutation.fromGender(gender);
    }
}
