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

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Kieran
 */
public class Host {
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final SimpleStringProperty status;

    public Host(String name, String address, String status) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    
    public boolean validate() {
        return (!this.getName().equals("") && !this.getAddress().equals(""));
    }
    
}
