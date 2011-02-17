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

package com.billing.ng.plugin;

/**
 * Interface to be implemented by plugins that require some one-time installation steps to be
 * performed before the plugin can be used.
 *
 * @author Brian Cowdery
 * @since 16/02/11
 */
public interface InstallablePlugin {

    /**
     * Returns true if the plugin has performed all necessary install checks. This method
     * must check the outcome of the installation and not rely on any member variables of
     * the current instance. A subsequent invocation of this method on a new instance should
     * return the same result.
     *
     * @return true if installed, false if not.
     */
    public boolean isInstalled();

    /**
     * Called to install the plugin if {@link #isInstalled()} returns false.
     */
    public void install();

    /**
     * Called to remove the plugin when it is removed from the systems plugin configuration.
     */
    public void remove();
}
