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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Brian Cowdery
 * @since 27-08-2010
 */
@Test(groups = { "entity", "quick" })
public class AccountTest {


    @DataProvider(name = "mock_account_hierarchy")
    public Object[][] createMockAccountHierarchy() {
        Account root = new Account();
        root.setId(1L);

        Account subAccount1 = new Account();
        subAccount1.setId(2L);

        Account subAccount2 = new Account();
        subAccount2.setId(3L);

        root.addSubAccount(subAccount1);
        root.addSubAccount(subAccount2);
        
        return new Object[][] {
                { root } 
        };
    }

    @Test
    public void testIsRootAccount() {
        Account root = new Account();

        assertTrue(root.isRootAccount());

        Account subAccount1 = new Account();
        Account subAccount2 = new Account();

        root.addSubAccount(subAccount1);
        root.addSubAccount(subAccount2);

        assertTrue(root.isRootAccount());
        assertFalse(subAccount1.isRootAccount());
        assertFalse(subAccount2.isRootAccount());
    }

    @Test
    public void testIsSubAccount() {
        Account root = new Account();

        Account subAccount1 = new Account();
        Account subAccount2 = new Account();

        assertFalse(subAccount1.isSubAccount());
        assertFalse(subAccount2.isSubAccount());

        root.addSubAccount(subAccount1);
        root.addSubAccount(subAccount2);

        assertFalse(root.isSubAccount());
        assertTrue(subAccount1.isSubAccount());
        assertTrue(subAccount2.isSubAccount());
    }
    
    @Test
    public void testHasSubAccounts() {
        Account root = new Account();

        assertFalse(root.hasSubAccounts());

        Account subAccount1 = new Account();
        Account subAccount2 = new Account();

        root.addSubAccount(subAccount1);
        root.addSubAccount(subAccount2);

        assertTrue(root.hasSubAccounts());
    }

    @Test(dataProvider = "mock_account_hierarchy")
    public void testAddNewRootParent(Account root) {
        assertTrue(root.isRootAccount());
        assertThat(root.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL));

        // set a new parent for the root
        Account parent = new Account();
        root.addParentAccount(parent);

        // new parent is now root
        assertTrue(parent.isRootAccount());
        assertTrue(parent.hasSubAccounts());
        assertThat(parent.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL));

        // old parent is now a sub-account, hierarchy level shifted down +1
        assertFalse(root.isRootAccount());
        assertThat(root.getParentAccount().getId(), is(parent.getId()));       
        assertThat(root.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));
    }

    @Test(dataProvider = "mock_account_hierarchy")
    public void testAddNewMiddleParent(Account root) {
        Account subAccount1 = root.getSubAccounts().get(0);
        Account subAccount2 = root.getSubAccounts().get(1);

        assertTrue(subAccount1.isSubAccount());
        assertThat(subAccount1.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));
                
        Account parent = new Account();
        parent.setId(4L);
        subAccount1.addParentAccount(parent);

        // root account is still root
        assertTrue(root.isRootAccount());

        // new parent is in level 1 under the root
        assertFalse(parent.isRootAccount());
        assertTrue(parent.isSubAccount());
        assertTrue(parent.hasSubAccounts());
        assertThat(parent.getParentAccount().getId(), is(root.getId()));
        assertThat(parent.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));

        // sub account moved to level 2 under the new parent
        assertFalse(subAccount1.isRootAccount());
        assertTrue(subAccount1.isSubAccount());
        assertFalse(subAccount1.hasSubAccounts());
        assertThat(subAccount1.getParentAccount().getId(), is(parent.getId()));
        assertThat(subAccount1.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 2));

        // sub account2 is still a sub-account of the root account
        assertFalse(subAccount2.isRootAccount());
        assertTrue(subAccount2.isSubAccount());
        assertFalse(subAccount2.hasSubAccounts());
        assertThat(subAccount2.getParentAccount().getId(), is(root.getId()));
        assertThat(subAccount2.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));
    }


    // todo: not supported yet
    @Test(dataProvider = "mock_account_hierarchy", enabled = false)
    public void testRemoveParent(Account root) {
        Account subAccount = root.getSubAccounts().get(0);

        assertFalse(subAccount.isRootAccount());
        assertTrue(subAccount.isSubAccount());
        assertThat(subAccount.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));

        subAccount.addParentAccount(null);
        
        // sub account has no parent, is now root
        assertTrue(subAccount.isRootAccount());
        assertFalse(subAccount.isSubAccount());
        assertThat(subAccount.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL));
    }

    @Test(dataProvider = "mock_account_hierarchy")
    public void testAddSubAccount(Account root) {
        Account subAccount3 = new Account();

        root.addSubAccount(subAccount3);

        assertTrue(root.hasSubAccounts());
        assertThat(root.getSubAccounts().size(), is(3));

        assertFalse(subAccount3.isRootAccount());
        assertTrue(subAccount3.isSubAccount());
        assertThat(subAccount3.getParentAccount().getId(), is(root.getId()));
        assertThat(subAccount3.getHierarchyLevel(), is(Account.ROOT_HIERARCHY_LEVEL + 1));
    }

    public void testVisitorTransversal() {

    }
}
