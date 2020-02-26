/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.*;

/**
 * FXML Controller class
 *
 * @author cryst
 */
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.PieChart;

public class PieChartController implements Initializable {

    PageSwitchHelper switchPage = new PageSwitchHelper();
    Database db = new Database();
    @FXML
    private AnchorPane apPie;

    @FXML
    private ToggleButton kanboardToggled;

    @FXML
    private ToggleButton pieChartToggled;

    @FXML
    private ToggleButton weeklyTrendsToggled;

    @FXML
    private ToggleButton deepFocusToggled;

    @FXML
    private ToggleButton dailyBDToggled;

    @FXML
    private ToggleButton weeklyBDToggled;

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    public void aboutClick(ActionEvent event) throws IOException {
        switchPage.switcher(event, "About.fxml");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            // TODO String query = "select (ttl_hours*100) AS percentage from database group by category;";
            //total hours spent, % by category, colour-coded
            //total hours spent can be expressed beneath as a label

            pieChart();
        } catch (SQLException ex) {
            Logger.getLogger(PieChartController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void pieChart() throws SQLException {
        /*apPie.getChildren().clear();

        String query = "select (ttl_hours*100) AS percentage from database group by category;";
        //total hours spent, % by category, colour-coded
        //total hours spent can be expressed beneath as a label

        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        list.add(new PieChart.Data("Cat1", 1000));
        list.add(new PieChart.Data("Cat2", 3000));
        list.add(new PieChart.Data("Cat3", 1000));
        list.add(new PieChart.Data("Cat4", 1000));
        list.add(new PieChart.Data("Cat5", 4231));
        PieChart pChart = new PieChart(list);
        apPie.getChildren().add(pChart);
         */
        apPie.getChildren().clear();
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        String x = "SELECT p.*, c.COLOUR FROM pieChart2 p, Category c " +
                   "WHERE c.Cat_ID = p.Cat_ID;";
        ResultSet rs1 = db.getResultSet(x);
        //add a new piechart data item for every row in the pieChart view
        while (rs1.next()) {
            list.add(new PieChart.Data(rs1.getString(2) + "(" + rs1.getString(4) + ")%", Double.parseDouble(rs1.getString(4))));
            System.out.println("Name: " + rs1.getString(1) + ", Duration: " + Double.parseDouble(rs1.getString(1)));

        }
        PieChart pchart = new PieChart(list);
        //pchart.setTitle("Pie Chart of my Life");
        pchart.setLegendVisible(false);
        apPie.getChildren().add(pchart);
        
        
        String[] colours = new String[5];
        int i = 0;
        rs1 = db.getResultSet(x);
        
        while(rs1.next()){
            if(i < 5){
                colours[i] = rs1.getString(5).substring(2);
                System.out.println(colours[i]);
                i++;
            }
        }
        
        i = 0;
        for(PieChart.Data data: list) {
            data.getNode().setStyle("-fx-pie-color: #"+colours[i]+";");
            i++;
        }
        
    }
}
