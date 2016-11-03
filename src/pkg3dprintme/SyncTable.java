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
 *
 * @author Kieran
 */
public class SyncTable {
    
    private final HashMap<Host, Long> syncTable;
    
    public SyncTable() {
        syncTable = new HashMap<>();
    }

    public void add(Host host, long delay) {
        syncTable.put(host, delay);
    }
    
    public Long get(Host host) {
        return syncTable.get(host);
    }
    
}
