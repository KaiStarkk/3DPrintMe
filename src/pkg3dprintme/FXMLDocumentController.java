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

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.Y;
import javafx.scene.text.Text;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.FileWriter;  
import java.io.IOException;
import static java.lang.Character.LINE_SEPARATOR;
import static javafx.scene.input.KeyCode.E;

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
    private Label resultsLabel;    
    
    @FXML
   
    private  TextField nameTextField;
    
    @FXML
    
   private TextField mobileTextField;
    
    @FXML
    private DatePicker dateDatePicker;
    
    @FXML
    private Text LogText;
    
    private int Seria_number=0;
 
    
    /* -----------------------------
     * Non-injected class members.
     * -----------------------------
     */
    private NetworkController networkController;
    
    /* -----------------------------
     * FXML method injection points.
     * -----------------------------
     */
    @FXML
    private void openDirectoryButtonAction(ActionEvent event) throws IOException{
        Seria_number += 1;
        String message ;
        String X ;
        String Y ;
        String M ;
        String Z;
        LocalDate A;
        Date B;
        Calendar cal = Calendar.getInstance();
        message = LogText.textProperty().get();
        X = nameTextField.getText();
        A = dateDatePicker.getValue();
        Y = mobileTextField.getText();
        B = cal.getTime();
        Z=Seria_number + "-----\n " + "Date And Time Is:  " + B + "\n" + "Name: "  + X + '\n' + "Mobile Number:  " + Y + '\n' + "Birth Day: " + A + '\n'+"\n";
       message += Z;
      LogText.textProperty().setValue(message);
      
      M = "E:\\3D/" + A + ".txt" ;
      
       FileWriter fw = new FileWriter(M,true); 
         fw.write(Z + "\r"); 
         fw.close();
         
         File file = new File("E:\\3D");
         Desktop.getDesktop().open(file);
         
    }
    /**
     * 
     * Comments here
     */
    @FXML
    private void exitButtonAction(ActionEvent event) {
     
        Platform.exit();
        System.exit(0);
    }

    /* ---------------------------
     * Non-injected class methods.
     * ---------------------------
     */
    
    /**
     * The initialize function performs all of the required instantiation.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        networkController = new NetworkController();
    }    

    /** 
     * The NetworkController class contains all of the back-end logic of the
     * application. An instance of this class is created by the application
     * controller, and that controller will call public methods in this class
     * as part of handling interface events.
     */
    private static class NetworkController {

        /**
         * The constructor for this class, which performs all of the required
         * instantiation.
         */
        public NetworkController() {
        }
        
        /**
         * The capture function captures images from all of the ImageServers
         * that are available, and them stores them to the file system. 
         * The parameters to this function are provided by the
         * interface controller which retrieves them from the layout and
         * performs sanitization before sending them here.
         * 
         * @param name the name of the client/job.
         * @param date the datetime of the job.
         * @param path the path to save the job.
         */
        public void capture(String name, ZonedDateTime date, String path) throws Exception {
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
         * @return an ArrayList of images.
         */
        public ArrayList<Image> getImages() throws Exception {
            HashMap<String, String> hostTable;
            ArrayList<Image> imageSet;
            imageSet = null;
            
            try {
                hostTable = statusQuery();
                if (!checkMap(hostTable))  {
                    throw new Exception();
                }
                imageSet = shoot(hostTable);
            } catch(Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
            
            return imageSet;
        }
        
        /**
         * This function queries all available ImageServers using the UDP
         * protocol broadcast address.
         * 
         * @return a map from String to String of all the hosts that were 
         *         detected, where the key is the name of the host and the 
         *         value is that host's IP address.
         */
        private HashMap<String, String> statusQuery() {
            HashMap<String, String> detectedHosts;
            detectedHosts = new HashMap<>();
            
            try {
                // TODO: Use UDP to scan for hosts and build the detectedHosts 
                //       table.
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
         * @param hostTable the host table to be checked.
         * @return a boolean representing success (true) or failure (false).
         */
        private boolean checkMap(HashMap<String, String> hostTable) {
            return (!hostTable.isEmpty() && hostTable.entrySet().stream().noneMatch((entry) -> (!checkEntry(entry))));
        }

        /**
         * This function checks a host entry for errors - errors include
         * such things as invalid host names, invalid IP addresses, clashes,
         * and mismatches.
         * 
         * @param entry the host entry to be checked.
         * @return a boolean representing success (true) or failure (false).
         */
        private boolean checkEntry(HashMap.Entry<String, String> entry) {
            // TODO: implement host entry checking
            return false;
        }

        /**
         * The shoot function coordinates requests to the ImageServers. It
         * first builds a table of delay adjustments, then issues a command
         * for the ImageServers to capture.
         * 
         * @param hostTable the table of ImageServers
         * @return the images captured.
         */
        private ArrayList<Image> shoot(HashMap<String, String> hostTable) {
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
         * @param imageSet the images to be saved.
         * @param savePath the path where the images should be saved.
         */
        private void storeImages(ArrayList<Image> imageSet, String savePath) {
            try {
                // TODO: implement image storing
            } catch (Exception e) {
                // TODO: Implement retries, error handling, and rethrowing
                throw e;
            }
        }

        /**
         * The sync function builds up a table of delays based on the time
         * that it takes to reach a group of hosts.
         * 
         * @param hostTable the hosts to be reached.
         * @return 
         */
        private HashMap<String, Integer> sync(HashMap<String, String> hostTable) {
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
         * @param syncTable the table of hosts and their addresses.
         * @param syncTable the table of host delays.
         * @return 
         */
        private ArrayList<Image> snap(HashMap<String, String> hostTable, 
                                        HashMap<String, Integer> syncTable) {
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
