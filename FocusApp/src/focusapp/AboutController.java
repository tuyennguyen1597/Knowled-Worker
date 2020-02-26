/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author moii1
 */
public class AboutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    PageSwitchHelper switchPage = new PageSwitchHelper(); 
    
    @FXML
    private Label copyrightLabel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        copyrightLabel.setWrapText(true);
    }    
    
    @FXML
    public void faqClick(ActionEvent event) throws IOException{
        switchPage.switcher(event, "FAQ.fxml");
    }
    
    @FXML
    public void kanboardClick(ActionEvent event) throws IOException {
        //welcomeHBox.setVisible(false);
        switchPage.switcher(event, "KanboardScreen.fxml");

    }

    @FXML
    private void pieChartClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "PieChart.fxml");

    }

    @FXML
    private void weeklyTrendsClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "WeeklyTrends.fxml");

    }

    @FXML
    private void dbClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "DailyBreakdown.fxml");

    }

    @FXML
    private void wbTrendsClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "WeeklyBreakdown.fxml");

    } 
    @FXML
    private void deepFocusClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "TestDeepFocus.fxml");

    }
}
