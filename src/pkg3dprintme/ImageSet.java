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

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kieran
 */
public class ImageSet {
    
    private final HashMap<String,ArrayList<Image>> imageSet;
    
    public ImageSet() {
        imageSet = new HashMap<>();
    }
    
    public void add(String address, ArrayList<Image> images) {
        imageSet.put(address, images);
    }

    public ArrayList<Image> get(String address) {
        return imageSet.get(address);
    }
    
}
