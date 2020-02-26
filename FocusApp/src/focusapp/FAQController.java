/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author jadenguyen
 */
public class FAQController implements Initializable {
    PageSwitchHelper pageSwitch = new PageSwitchHelper();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void aboutClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "About.fxml");
    }

    @FXML
    private void logoutClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void kanbanClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "KanboardScreen.fxml");
    }

    @FXML
    private void deepFocusClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "TestDeepFocus.fxml");
    }

    @FXML
    private void pieChartClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "PieChart.fxml");
    }

    @FXML
    private void dailyClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "DailyBreakdown.fxml");
    }

    @FXML
    private void weeklyClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "WeeklyBreakdown.fxml");
    }

    @FXML
    private void trendClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "WeeklyTrends.fxml");
    }
    
}
