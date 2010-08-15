package com.billing.ng.dao.impl;

import com.billing.ng.dao.GenericDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * GenericDAO implementation, based heavily on Christian Bauer's generic DAO from the
 * Hibernate example Caveat Emptor application.
 *
 *  see https://www.hibernate.org/328.html
 *
 * This implementation relies on Container Managed Transactions (CMT) and Persistence
 * Context injection (also provided by the container). Some method's require Hibernate
 * as the underlying persistence provider and will throw an exception if the hibernate Session
 * could not be retrieved ({@link #getSession()}).
 *
 * @param <T>
 * @param <ID>
 *
 * @author Brian Cowdery
 * @since 2-Sep-2009
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext
    private EntityManager em;
    private transient Session session;

    private Class<T> entityBeanType;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.entityBeanType
                = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    protected EntityManager getEntityManager() {
        if (em == null)
            throw new IllegalStateException("EntityManager has not been set on DAO prior to usage");
        return em;
    }

    /**
     * Returns a hibernate Session from the EntityManager, which requires that Hibernate
     * be the underlying persistence provider - and that EntityManager#getDelegate() will
     * return a Session object in this case. JPA specifications on EntityManager#getDelegate()
     * are spotty and known to be problematic in some environments.
     *
     * @return Hibernate Session
     */
    protected Session getSession() {
        if (session == null || !session.isOpen()) {
            Object o  = getEntityManager().getDelegate();

            if (!(o instanceof Session))
                throw new UnsupportedOperationException("Hibernate is not the underlying Persistence provider,"
                                                        + " or EntityManager#getDelegate() unsupported.");
            session = (Session) o;
        }

        return session;
    }

    protected Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    /**
     * @see javax.persistence.EntityManager#flush()
     */
    protected void flush() {
        getEntityManager().flush();
    }

    /**
     * @see javax.persistence.EntityManager#clear()
     */
    protected void clear() {
        getEntityManager().clear();
    }

    /**
     * Persists the specified entity to the database, returning the newly persisted entity.
     *
     * @param entity entity to persist
     * @return T persisted entity
     * @see javax.persistence.EntityManager#persist(Object)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T persist(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    /**
     * Updates/merges the specified entity to the database, returning the newly updated entity.
     *
     * @param entity entity to update
     * @return T updated entity
     * @see javax.persistence.EntityManager#merge(Object)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T update(T entity) {
        return getEntityManager().merge(entity);
    }

    /**
     * Deletes/removes the specified entity from the database.
     *
     * @param entity entity to update
     * @see javax.persistence.EntityManager#remove(Object)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(T entity) {
        if (isManaged(entity)) {
            getEntityManager().remove(entity);
        } else {
            getEntityManager().remove(getEntityManager().merge(entity));
        }
    }

    /**
     * Returns true if the given entity is associated with the current persistence context.
     *
     * @param entity entity to check
     * @return true if entity is managed, false if not
     */
    public boolean isManaged(T entity) {
        return getEntityManager().contains(entity);
    }

    /**
     * Returns true if a persisted record exsits for the given id.
     *
     * This method requires an underlying Hibernate persistence provider.
     *
     * @param id primary key of entity
     * @return true if entity exists for id, false if entity does not exist
     */
    public boolean isIdPersisted(ID id) {
        Criteria criteria = createCritiera()
                .add(Restrictions.idEq(id))
                .setProjection(Projections.id());

        return !criteria.list().isEmpty();
    }

    /**
     * Finds an entity by its primary key, optionally locking the entity for
     * exclusive write access.
     *
     * @param id primary key of entity
     * @param lock whether or not to lock entity for exclusive write access
     * @return T entity of type T
     * @see javax.persistence.EntityManager#lock(Object, javax.persistence.LockModeType)
     */
    public T findById(ID id, boolean lock) {
        T entity = getEntityManager().find(getEntityBeanType(), id);

        if (lock)
            getEntityManager().lock(entity, LockModeType.WRITE);

        return entity;
    }

    /**
     * Returns a list of all entities of this type T present within the database.
     *
     * @return List<T> list of entities of type T
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getEntityManager().createQuery("from " + getEntityBeanType().getName()).getResultList();
    }

    /**
     * Finds similar entities to the provided example, excluding an explicitly
     * ignored properties (as strings).
     *
     * This method requires an underlying Hibernate persistence provider.
     *
     * @param exampleEntity enity to use as an example
     * @param excludedProperty array of excluded properties of the example
     * @return List<T> list of matching entities of type T
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleEntity, String[] excludedProperty) {
        Criteria criteria = createCritiera();
        Example example = Example.create(exampleEntity);

        for (String exclude : excludedProperty)
            example.excludeProperty(exclude);

        criteria.add(example);
        return criteria.list();
    }

    /**
     * Returns a list of all entities matching the given Criterion.
     *
     * @param criterion criteria to match
     * @return List<T> list of matching entities of type T
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(Criterion... criterion) {
        Criteria criteria = createCritiera();
        for (Criterion c : criterion)
            criteria.add(c);

        return criteria.list();
    }

    /**
     * Creates a Hibernate Criteria instance for the bound entity type T.
     *
     * This method requires an underlying Hibernate persistence provider.
     *
     * @return criteria
     */
    protected Criteria createCritiera() {
        return getSession().createCriteria(getEntityBeanType());
    }
}
