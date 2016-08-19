/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class Control {
    Stage stage;
     private static final Control Instance = new Control();
    public void setStage(Stage s){
        stage = s;
    }
    public static Control getInstance(){
        return Instance;
    }
    public Stage getStage(){
        return stage;
    }
    public void loadfxml(String filename){
        
        try {
            
            Parent root = FXMLLoader.load(getClass().getResource(filename));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private Control(){
        
    }
    
}
