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
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;

/**
 * The FXMLDocumentController class contains all of the front-end logic of the
 * application. Components and methods specified in the FXML GUI layout 
 * are injected into the controller using JavaFX FXML injection points.
 * Back-end logic is handled by the NetworkController object.
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
    
    /* -----------------------------
     * FXML method injection points.
     * -----------------------------
     */

    /**
     * TODO: This function needs a JavaDoc comment.
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
            // TODO: Display detailed error messages
        }
    }
    
    /**
     * The captureButtonAction is an injected method which will be called
     * whenever the capture button on the interface is pressed.
     * 
     * @author Kieran Hannigan
     * @param event the internal event which triggers this handler.
     */
    @FXML
    private void captureButtonAction(ActionEvent event) throws IOException {
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
            // TODO: Perform capture functions on the NetworkController and
            // handle the results
        } catch (Exception e) {
            // TODO: Display detailed error messages
        }
    }
    
    /**
     * TODO: Write JavaDoc comment for this function
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
    }   
}
