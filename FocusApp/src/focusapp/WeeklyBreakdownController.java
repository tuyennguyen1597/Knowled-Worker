/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

/**
 *
 * @author User
 */
public class WeeklyBreakdownController implements Initializable {

    Database db = new Database();
    PageSwitchHelper switchPage = new PageSwitchHelper();
    List<Activity> list = new ArrayList<>();

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

    @FXML
    private Pane test;

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
        weeklyBDToggled.setBackground(Background.EMPTY);

    }
    
     
    @FXML
    public void aboutClick(ActionEvent event) throws IOException {
        switchPage.switcher(event, "About.fxml");
    }
    
    @FXML
    public void faqClick(ActionEvent event) throws IOException{
        switchPage.switcher(event, "FAQ.fxml");
    }
    

       @FXML
    private void deepFocusClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "TestDeepFocus.fxml");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources
    ) {
        try {

            loadBarChart();
        } catch (SQLException ex) {
            Logger.getLogger(WeeklyBreakdownController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(WeeklyBreakdownController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadBarChart() throws SQLException, ParseException {
        test.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Top 5 Tasks");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Hours");
        BarChart barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Typical hours spent per activity per week");

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        String x = "select c.name, SUM(e.DURATION/3600000)/(select strftime('%W', now) - min(strftime('%W', do_date)) "
                + "from WEEKLYTRENDS) as test, c.colour\n" 
                +"FROM ENTRY e, Category c\n" 
                +"WHERE c.Cat_ID = e.Cat_ID\n" 
                +"group by e.Cat_ID\n" 
		+ "order by test desc limit 5;";
                
                //"select title, duration from entry join task using (t_id) group by t_id order by duration desc limit 5;";

        //select * from activities;
        ResultSet rs1 = db.getResultSet(x);

        //need to group my data by a date range
        while (rs1.next()) {
            System.out.println((rs1.getDouble(2) / (1000 * 60 * 60)) % 24);


            list.add(new Activity(rs1.getString(1), rs1.getDouble(2)));
        }

        for (Activity a : list) {

            
                series.getData().add(new XYChart.Data<>(a.getCategory(), a.getDurationHours()));

            

        }

        barChart.getData().add(series);
        
        Node n = barChart.lookup(".data0.chart-bar");
        rs1 = db.getResultSet(x);
        int i = 1;
        while(rs1.next()){
            n.setStyle("-fx-bar-fill: #"+rs1.getString(3).substring(2));
            n = barChart.lookup(".data"+Integer.toString(i)+".chart-bar");
            i++;
        }
        barChart.setLegendVisible(false);
        test.getChildren().add(barChart);

    }
}
