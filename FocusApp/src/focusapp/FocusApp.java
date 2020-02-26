/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author moii1
 */
public class FocusApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //automatic screen resizing based on screen dimensions
        /*GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight(); */
        loadDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("KanboardScreen.fxml"));

        //Scene scene = new Scene(root, width, height);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void loadDatabase() throws SQLException {
        //Database.openConnection();

        //TODO: Call Database methods here to create the database
        Database.createCategoryTable();
        Database.createTaskTable();
        Database.createStatusTable();
        Database.createEntryTable();
        Database.createViews();
        Database.createSampleData();
    }
}
