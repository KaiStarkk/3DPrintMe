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

import java.util.ArrayList;

/**
 * TODO: Write JavaDoc
 * @author Kieran
 */
public class HostList {
    
    private final ArrayList<Host> hostList;
    
    /**
     * TODO: Write JavaDoc
     */
    public HostList() {
        hostList = new ArrayList<>();
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
        return (!hostList.isEmpty() && hostList.stream().noneMatch((host) -> (!host.validate())));
    }

    public void add(Host host) {
        hostList.add(host);
    }
    
    public Host get(int index) {
        return hostList.get(index);
    }

    public ArrayList<Host> getAll() {
        return hostList;
    }
    
}
