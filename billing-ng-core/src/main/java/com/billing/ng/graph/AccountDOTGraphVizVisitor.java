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

package com.billing.ng.graph;

import com.billing.ng.entities.Account;
import com.billing.ng.entities.structure.Visitor;

/**
 *
 * Example DOT graph:
 * <code>
 *      graph accounts {
 *          ACCOUNT1 [label="Bob Smith; Acme Furniture Inc."]
 *          SUB-ACCOUNT2 [label="Bob Smith; Acme Furniture Inc. (sales)"]
 *          SUB-ACCOUNT3 [label="Bob Smith; Acme Furniture Inc. (sales)(advertising)"]
 *          SUB-ACCOUNT4 [label="Bob Smith; Acme Furniture Inc. (office supplies)"]
 *          SUB-ACCOUNT5 [label="Bob Smith; Acme Furniture Inc. (information tech) *suspended"]
 *
 *          ACCOUNT1 -> SUB-ACCOUNT2
 *          ACCOUNT1 -> SUB-ACCOUNT4
 *          ACCOUNT1 -> SUB-ACCOUNT5 [style=dotted color=grey]
 *          SUB-ACCOUNT2 -> SUB-ACCOUNT-3
 *       }
 * <code>
 *
 * todo: for the UI, use this visitor and the Grappa http://www2.research.att.com/~john/Grappa/ library to render an image
 *
 * @author Brian Cowdery
 * @since 27-08-2010
 */
public class AccountDOTGraphVizVisitor implements Visitor<Account, String> {

    /**
     * Visits all sub-accounts of the given account and generates a GraphViz DOT
     * graph of the account structure.
     *
     * @param account account to visit
     * @return DOT graph as a string
     */
    public String visit(Account account) {
        return null;
    }
}
