/*
 * Copyright (C) 2016
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

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * The FXMLDocumentController class contains all of the front-end logic of the
 * application. Components and methods specified in the FXML GUI layout 
 * are injected into the controller using JavaFX FXML injection points.
 * 
 * @author Kieran Hannigan
 */
public class FXMLDocumentController implements Initializable {
    
    /* -----------------------------
     * FXML member injection points.
     * -----------------------------
     */
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField mobileTextField;
    
    @FXML
    private DatePicker dateDatePicker;
    
    @FXML
    private ListView logListView;
    
    @FXML
    private TextField pathTextField;
    
    @FXML
    private TableView hostTableView;
    
    @FXML
    private TableColumn hostColumn;
    
    @FXML
    private TableColumn addressColumn;
    
    @FXML
    private TableColumn statusColumn;
    
    @FXML
    private ImageView topLeftImageView;
    
    @FXML
    private ImageView topRightImageView;
    
    @FXML
    private ImageView bottomLeftImageView;
    
    @FXML
    private ImageView bottomRightImageView;
    
    /* -----------------------------
     * Non-injected class members.
     * -----------------------------
     */
    
    private static final String SEP = "--------\n";
    private static final ObservableList<String> LOG
            = FXCollections.observableArrayList();
    private static final ObservableList<Map> HOSTS
            = FXCollections.observableArrayList();
    
    public static final String hostColumnMapKey = "Host";
    public static final String addressColumnMapKey = "Address";
    public static final String statusColumnMapKey = "Status";
    
    private static final NetworkController networkController 
            = new NetworkController();
    
    /* -----------------------------
     * FXML method injection points.
     * -----------------------------
     */
    
    /**
     * The captureButtonAction is an injected method which will be called
     * whenever the capture button on the interface is pressed.
     * 
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void captureButtonAction(ActionEvent event) {
        nameTextField.setEditable(false);
        mobileTextField.setEditable(false);
        dateDatePicker.setEditable(false);
        
        LOG.addAll(SEP,
                   "Name: " + nameTextField.getText(),
                   "Date: " + dateDatePicker.getValue().toString(),
                   "Mobile: " + mobileTextField.getText(),
                   SEP
                   );
        try {
            // FileWriter fw;
            // fw = new FileWriter(pathTextField.getText(), false);
            // fw.close();
        } catch (Exception e) {
            // TODO: Display detailed error messages
        }
    }

    /**
     * TODO: This function needs a JavaDoc comment.
     * 
     * @author Yuexian Sun
     * @author Kieran Hannigan
     * @param event
     */
    @FXML
    private void openDirectoryButtonAction(ActionEvent event) {
        
    }
    
    /**
     * TODO: This function needs a JavaDoc comment.
     * 
     * @author Chaoyan Ye
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void refreshTableButtonAction(ActionEvent event) {
        // TODO: update the table with hosts from the network controller
        try {
            HashMap<String, String> hostMap = networkController.statusQuery();
            
            hostMap.entrySet().stream().map((host) -> {
                HashMap<String, String> row = new HashMap<>();
                row.put("Host", host.getKey());
                row.put("Address", host.getValue());
                return row;
            }).map((row) -> {
                row.put("Status", "Connected");
                return row;
            }).forEach((row) -> {
                HOSTS.add(row);
            });
            
        } catch (Exception e) {
            // TODO: error handling            
        }
        
    }
    
    /**
     * TODO: This function needs a JavaDoc comment.
     * 
     * @author Qiang Shen
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void refreshImagesButtonAction(ActionEvent event) {
        HashMap<String, String> entry = new HashMap<>();
        
        try {
            ArrayList<Image> images = networkController.snap(entry);            
            topLeftImageView.setImage(images.get(0));
            topRightImageView.setImage(images.get(1));
            bottomLeftImageView.setImage(images.get(2));
            bottomRightImageView.setImage(images.get(3));
        } catch (Exception e) {
            // TODO: error handling
        }
        
    }
     
    /**
     * The exitMenuItemAction is an injected method which will be called
     * whenever the exit menu item on the interface is pressed. This function
     * calls the JavaFX platform's exit function.
     * 
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void exitMenuItemAction(ActionEvent event) {
        Platform.exit();
    }

    /* ---------------------------
     * Non-injected class methods.
     * ---------------------------
     */
    
    /**
     * The constructor performs all instantiation for the class object that
     * does not require access to the FXML injected members.
     * 
     * @author Kieran Hannigan
     */
    public FXMLDocumentController() {
    }
    
    /**
     * The purpose for the initialize function (by comparison to the 
     * constructor) is to perform all instantiation that requires access to
     * FXML injected members - these members are only injected after the
     * constructor is called. At present there is no instantiation that
     * requires FXML injected members but the function is still required
     * because it will be called by the platform.
     * 
     * @author Kieran Hannigan
     * @param url 
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateDatePicker.setValue(LocalDate.now());
        logListView.setItems(LOG);
        
        hostColumn.setCellValueFactory(new MapValueFactory(hostColumnMapKey));
        addressColumn.setCellValueFactory(new MapValueFactory(addressColumnMapKey));
        statusColumn.setCellValueFactory(new MapValueFactory(statusColumnMapKey));
        
        hostTableView.setItems(HOSTS);
        hostTableView.getColumns().setAll(hostColumn, addressColumn, statusColumn);
        
        /*
        * Example code taken from Example 12-12 available at
        * http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
        */
        Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
            }
        };
        
        hostColumn.setCellFactory(cellFactoryForMap);
        addressColumn.setCellFactory(cellFactoryForMap);
        statusColumn.setCellFactory(cellFactoryForMap);
        
    }
    
    /** 
     * The NetworkController class contains all of the back-end logic of the
     * application. An instance of this class is created by the application
     * controller, and that controller will call public methods in this class
     * to interface with external resources such as the ImageServers and the
     * client disk.
     */
    private static class NetworkController {

        /**
         * The constructor for this class performs all of the required
         * instantiation. Currently no instantiation is performed, but the
         * function is still required as it will be called when objects are
         * instantiated from this class.
         * 
         * @author Kieran Hannigan
         */
        public NetworkController() {
        }
        
        /**
         * The capture function captures images from all of the ImageServers
         * that are available, and then stores them to the file system. 
         * The parameters to this function are provided by the 
         * interface controller, which retrieves them from the layout, and
         * performs sanitization before sending them here.
         * 
         * @author Kieran Hannigan
         * @param name the name of the client/job.
         * @param date the datetime of the job.
         * @param path the path to save the job.
         */
        public void capture(String name, LocalDate date, String path) 
                                                            throws Exception {
            ArrayList<Image> images;
            
            try {
                images = getImages();
                storeImages(images, path);
            } catch(Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
        }
        
        /**
         * The getImages function captures images from all of the detected
         * ImageServers. This function is called by the capture function,
         * but can also be called by the interface controller for previewing.
         * 
         * @author Kieran Hannigan
         * @return an ArrayList of images.
         */
        public ArrayList<Image> getImages() throws Exception {
            HashMap<String, String> hostTable;
            ArrayList<Image> imageSet;
            imageSet = null;
            
            try {
                hostTable = statusQuery();
                imageSet = shoot(hostTable);
            } catch(Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
            return imageSet;
        }
        
        /**
         * This function queries all available ImageServers for their IP
         * addresses using a UDP protocol broadcast, then builds a host
         * table from the results.
         * 
         * @author Kieran Hannigan
         * @return a map from String to String of all the hosts that were 
         *         detected, where the key is the name of the host and the 
         *         value is that host's IP address.
         */
        public HashMap<String, String> statusQuery() throws Exception {
            HashMap<String, String> detectedHosts;
            detectedHosts = new HashMap<>();
            
            try {
                // TODO: Search for hosts
                if (!checkMap(detectedHosts))  {
                    throw new Exception();
                }
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            return detectedHosts;
        }
        
        /**
         * This function checks a host table for errors - it accomplishes
         * this by iterating through the table and checking each entry
         * individually.
         * 
         * @author Kieran Hannigan
         * @param hostTable the host table to be checked.
         * @return a boolean representing success (true) or failure (false).
         */
        private boolean checkMap(HashMap<String, String> hostTable) {
            return (!hostTable.isEmpty() && 
                    hostTable.entrySet().stream().noneMatch((entry) 
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

        /**
         * The shoot function coordinates requests to the ImageServers by 
         * first calling the sync function to build a table of delay 
         * adjustments, and then calling the snap function to issue a command 
         * for the ImageServers to capture.
         * 
         * @author Kieran Hannigan
         * @param hostTable the table of ImageServers
         * @return the images captured.
         */
        private ArrayList<Image> shoot(HashMap<String, String> hostTable) 
                                                            throws Exception {
            ArrayList<Image> capturedImages;
            capturedImages = new ArrayList<>();
            
            HashMap<String, Integer> syncTable;
            
            try {
                syncTable = sync(hostTable);
                capturedImages = snap(hostTable, syncTable);
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
            return capturedImages;
        }

        /**
         * The storeImages function saves images to the local file system.
         * 
         * @author Kieran Hannigan
         * @param imageSet the images to be saved.
         * @param savePath the path where the images should be saved.
         */
        private void storeImages(ArrayList<Image> imageSet, String savePath) 
                                                            throws Exception{
            try {
                // TODO: implement image storing
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
        }

        /**
         * The sync function builds up a table of delays based on the time
         * that it takes to reach each of a group of hosts.
         * 
         * @author Kiearn Hannigan
         * @param hostTable the hosts to be reached.
         * @return 
         */
        private HashMap<String, Integer> sync(HashMap<String, String> hostTable)
                                                            throws Exception {
            HashMap<String, Integer> observedDelays;
            observedDelays = null;
            
            try {
                // TODO: Build sync table using custom TCP handshake
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
            return observedDelays;
        }

        /**
         * The snap function commands a group of ImageServers to capture and
         * return their payload at a given unified time.
         * 
         * @author Kieran Hannigan
         * @param syncTable the table of hosts and their addresses.
         * @param syncTable the table of host delays.
         * @return 
         */
        public ArrayList<Image> snap(HashMap<String, String> hostTable, 
                                        HashMap<String, Integer> syncTable) 
                                                            throws Exception {
            ArrayList<Image> receivedImages;
            receivedImages = null;
            
            try {
                // TODO: Capture images from ImageServers
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
            return receivedImages;
        }

        private ArrayList<Image> snap(HashMap<String, String> host) {
            ArrayList<Image> receivedImages;
            receivedImages = null;

            try {
                // TODO: Capture images from ImageServers
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }

            return receivedImages;
        }
    }
}