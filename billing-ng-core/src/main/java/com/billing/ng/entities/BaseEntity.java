package com.billing.ng.entities;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * BaseEntity
 *
 * @author Brian Cowdery
 * @since 24-Apr-2010
 */
@XmlTransient
public abstract class BaseEntity implements Serializable {

    /**
     * Executes the Hibernate ClassValidator on this class, validating
     * all annotated fields and returning any invalid values encountered.
     *
     * @return invalid values (validation messages), empty if none
     */
    @Transient
    @SuppressWarnings("unchecked")
    public InvalidValue[] validate() {
        ClassValidator validator = new ClassValidator(this.getClass());
        return validator.getInvalidValues(this);
    }
}
