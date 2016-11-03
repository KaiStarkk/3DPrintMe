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

import javafx.scene.image.Image;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

/**
 * The FXMLDocumentController class contains all of the front-end logic of the
 * application. Components and methods specified in the FXML GUI layout 
 * are injected into the controller using JavaFX FXML injection points.
 * Back-end logic is handled by the NetworkController object.
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
    private TableView<Host> hostTable;
    @FXML
    private TableColumn<Host, String> hostnameColumn;
    @FXML
    private TableColumn<Host, String> ipAddressColumn;
    @FXML
    private TableColumn<Host, String> statusColumn;
    @FXML
    private ImageView imageViewTopLeft;
    @FXML
    private ImageView imageViewTopRight;
    @FXML
    private ImageView imageViewBottomLeft;
    @FXML
    private ImageView imageViewBottomRight;
    
    /* -----------------------------
     * Non-injected class members.
     * -----------------------------
     */
    
    private static final String SEP = "--------\n";
    private static final ObservableList<String> LOG
            = FXCollections.observableArrayList();
    private static final NetworkController NETWORK_CONTROLLER 
            = new NetworkController();
    private static final DirectoryChooser CHOOSER 
           = new DirectoryChooser();
    private final ObservableList<Host> HOSTS_LIST
            = FXCollections.observableArrayList();
    
    /* -----------------------------
     * FXML method injection points.
     * -----------------------------
     */

    /**
     * The openDirectoryButtonAction is an injected method which will be called
     * whenever the open directory button on the interface is pressed. This 
     * function opens the save directory in an OS folder browser.
     * 
     * @author Yuexian Sun
     * @author Kieran Hannigan
     * @param event
     * @throws IOException 
     */
    @FXML
    private void openDirectoryButtonAction(ActionEvent event) {
        try {
            java.awt.Desktop.getDesktop().open(new File(pathTextField.getText()));
        } catch (Exception e) {
            LOG.addAll("Error: Attempting to open image directory failed. Please confirm that the correct directory has been set in the settings menu.", SEP);
        }
    }
    
    /**
     * The captureButtonAction is an injected method which will be called
     * whenever the capture button on the interface is pressed. This function
     * commands the network controller to captures all images from all
     * available image servers.
     * 
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void captureButtonAction(ActionEvent event) throws IOException {
        nameTextField.setEditable(false);
        mobileTextField.setEditable(false);
        dateDatePicker.setEditable(false);
        String name = nameTextField.getText();
        LocalDate date = dateDatePicker.getValue();
        String mobile = mobileTextField.getText();
        String path = pathTextField.getText();
        
        LOG.addAll("Name: " + name,
                   "Date: " + date,
                   "Mobile: " + mobile,
                   SEP
                   );
        
        try {
            NETWORK_CONTROLLER.capture(name, date, path);
        } catch (Exception e) {
            LOG.addAll("Error: Image capture failed. Please confirm that the ImageServers are available, and that a valid image directory is set.", SEP);
        }
    }
    
    /**
     * The browseButtonAction function is an injected method which will be called
     * whenever the browse button on the interface is pressed. This function
     * opens an OS folder browser dialogue to select the save location for the
     * images.
     * 
     * @author Yuexian Sun
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void browseButtonAction(ActionEvent event) {
        pathTextField.setText(CHOOSER.showDialog(null).getPath());
    }
    
    /**
     * The refreshHostTableButtonAction is an injected method which will be called
     * whenever the refresh button in the hosts section of the interface is pressed.
     * This function commands the network controller to refresh its list of hosts,
     * then updates the host table view.
     * 
     * @author Kieran Hannigan
     * @param event 
     */
    @FXML
    private void refreshHostTableButtonAction(ActionEvent event) {
        try {
            HOSTS_LIST.clear();
            HOSTS_LIST.addAll(NETWORK_CONTROLLER.getHosts().getAll());
        } catch (Exception e) {
            LOG.addAll("Error: Host updating failed. Please ensure that the ImageServers are available.", SEP);
        }
    }
    
    /**
     * The refreshImagesButtonAction is an injected method which will be called
     * whenever the refresh button in the images section of the interface is pressed.
     * This function requests the images from the image server selected in 
     * the host table.
     * 
     * @author Kieran Hannigan
     * @param event 
     */
    @FXML
    private void refreshImagesButton(ActionEvent event) {
        try {
            Host selected = hostTable.getSelectionModel().getSelectedItem();
            ImageSet imageSet = NETWORK_CONTROLLER.shoot(selected);
            ArrayList<Image> images = imageSet.get(selected.getAddress());
            imageViewTopLeft.setImage(images.get(0));
            imageViewTopLeft.setImage(images.get(1));
            imageViewTopLeft.setImage(images.get(2));
            imageViewTopLeft.setImage(images.get(3));
        } catch(Exception e) {
            LOG.addAll("Error: Image preview failed. Please ensure that the selected ImageServer is still connected and available.", SEP);
        }
        System.out.println(hostTable.getSelectionModel().getSelectedItem().getAddress());
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
        CHOOSER.setTitle("Select a directory.");
        CHOOSER.setInitialDirectory(new File("C:\\"));
    }
    
    /**
     * The purpose for the initialize function (by comparison to the 
     * constructor) is to perform all instantiation that requires access to
     * FXML injected members - these members are only injected after the
     * constructor is called.
     * 
     * @author Kieran Hannigan
     * @param url 
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateDatePicker.setValue(LocalDate.now());
        logListView.setItems(LOG);
        hostnameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        hostTable.setItems(HOSTS_LIST);
    }   
}
