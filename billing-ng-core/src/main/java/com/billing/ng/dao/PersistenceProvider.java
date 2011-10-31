/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2011 Brian Cowdery

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

package com.billing.ng.dao;

import org.jboss.solder.core.ExtensionManaged;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * CDI producer that wraps the <code>javax.persistence.EntityManagerFactory</code> as a
 * Seam managed, conversation scoped bean allowing long running conversation scoped
 * persistence contexts.
 *
 * Injection of <code>javax.persistence.EntityManager</code> should be handled using the
 * <code>@Inject</code> annotation (do not use <code>@PersistenceContext</code>!).
 *
 * Example:
 * <pre>
 * {@code
 *     @Inject
 *     private EntityManager entityManager;
*  }
 * </pre>
 *
 * @author Brian Cowdery
 * @since 19-10-2011
 */
public class PersistenceProvider {

    @ExtensionManaged
    @Produces
    @PersistenceUnit
    @ConversationScoped
    EntityManagerFactory billingPersistenceUnit;
}
