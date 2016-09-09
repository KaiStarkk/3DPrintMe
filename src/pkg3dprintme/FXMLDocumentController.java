/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dprintme;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * The FXMLDocumentController class contains the business logic of the 
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
    
    /* -----------------------------
     * Non-injected class members.
     * -----------------------------
     */
    private NetworkController networkController;
    
    /* -----------------------------
     * FXML method injection points.
     * -----------------------------
     */
    
    /**
     *
     * @param event 
     */
    @FXML
    private void captureButtonAction(ActionEvent event) {
        ZonedDateTime zdt;
        zdt = ZonedDateTime.now();
        boolean success = networkController.capture("Kieran", zdt, "C:\\");
        resultsLabel.setText(success ? "Success!" : "Failure!");
    }

    /* ---------------------------
     * Non-injected class methods.
     * ---------------------------
     */
    
    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        networkController = new NetworkController();
    }    

    private static class NetworkController {

        HashMap<String, Integer> hostTable;
        
        public NetworkController() {
        }
        
        public boolean capture(String name, ZonedDateTime date, String path) {
            hostTable = statusQuery();
            return (checkMap(hostTable));
        }
        
        private HashMap<String, Integer> statusQuery() {
            HashMap<String, Integer> hosts;
            hosts = new HashMap<>();
            return hosts;
        }
        
        private boolean checkMap(HashMap<String, Integer> hostTable) {
            return false;
        }

    }
    
}
