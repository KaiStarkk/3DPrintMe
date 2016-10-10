/*
 * Copyright (C) 2016 Kieran
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pkg3dprintme;

import java.util.HashMap;

/**
 * TODO: Write JavaDoc
 * @author Kieran
 */
class HostList {
    
    private final HashMap<String, String> hostList;
    
    /**
     * TODO: Write JavaDoc
     */
    public HostList() {
        hostList = new HashMap<>();
    }

    /**
     * This function checks the host list for errors - it accomplishes
     * this by iterating through the table and checking each entry
     * individually.
     * 
     * @author Kieran Hannigan
     * @return a boolean representing success (true) or failure (false).
     */
    public boolean validate() {
        return (!hostList.isEmpty() && 
                hostList.entrySet().stream().noneMatch((entry) 
                        -> (!checkEntry(entry))));
    }

    /**
     * This function checks a host entry for errors - errors include
     * such things as invalid host names, invalid IP addresses, clashes,
     * and mismatches.
     * 
     * @author Kieran Hannigan
     * @param entry the host entry to be checked.
     * @return a boolean representing success (true) or failure (false).
     */
    private boolean checkEntry(HashMap.Entry<String, String> entry) {
        // TODO: implement host entry checking
        return false;
    }
}
