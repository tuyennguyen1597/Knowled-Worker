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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import static javafx.scene.control.ContentDisplay.CENTER;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

public class DailyBreakdownController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    PageSwitchHelper pageSwitch = new PageSwitchHelper();
    Database database = new Database();
    
    @FXML
    private AnchorPane paneView;

    @FXML
    private ToggleButton dailyBDToggled;

    @FXML
    private void handlePieChartButtonAction(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "PieChart.fxml");

    }

    @FXML
    public void kanboardClick(ActionEvent event) throws IOException {
        //welcomeHBox.setVisible(false);
        pageSwitch.switcher(event, "KanboardScreen.fxml");

    }
    
    @FXML
    private void deepFocusClicked(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "TestDeepFocus.fxml");
    }
    
    @FXML
    private void weeklyTrendsClick(ActionEvent event) throws IOException, SQLException {
        pageSwitch.switcher(event, "WeeklyTrends.fxml");

    }

    @FXML
    private void dbClick(ActionEvent event) throws IOException, SQLException {
        dailyBDToggled.setBackground(Background.EMPTY);

    }
    
    @FXML
    private void wbTrendsClick(ActionEvent event) throws IOException, SQLException {
        pageSwitch.switcher(event, "WeeklyBreakdown.fxml");
    }
    
    @FXML 
    private void aboutClick(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "About.fxml");
    }

    @FXML
    public void faqClick(ActionEvent event) throws IOException{
        pageSwitch.switcher(event, "FAQ.fxml");
    }
     @Override

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
        loadData();
        }catch(SQLException ex){
            System.out.println("you messed up");
        }
    }

    private void loadData() throws SQLException {
        paneView.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Activities");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Hours Spent");
        BarChart dailyBreakdownChart = new BarChart(xAxis, yAxis);
        dailyBreakdownChart.setTitle("Average time spent per activity per day");
        
        //series.setName("Hours Spent");
        String request = "SELECT c.NAME, SUM(e.DURATION/3600000)/ (SELECT count(do_date) FROM Entry), c.COLOUR\n" +
                         "FROM ENTRY e, Category c\n" +
                         "WHERE c.Cat_ID = e.Cat_ID\n" +
                         "GROUP BY e.CAT_ID\n" +
                         "ORDER BY SUM(e.DURATION)/ (SELECT count(do_date) FROM Entry) DESC LIMIT 5;";
        ResultSet rs1 = database.getResultSet(request);
        XYChart.Series<String,Double> series = new XYChart.Series<>();
        while(rs1.next()){
            series.getData().add(new XYChart.Data(rs1.getString(1), Double.parseDouble(rs1.getString(2))));
        }
        
        dailyBreakdownChart.getData().add(series);
        
        Node n = dailyBreakdownChart.lookup(".data0.chart-bar");
        rs1 = database.getResultSet(request);
        int i = 1;
        while(rs1.next()){
            n.setStyle("-fx-bar-fill: #"+rs1.getString(3).substring(2));
            n = dailyBreakdownChart.lookup(".data"+Integer.toString(i)+".chart-bar");
            i++;
        }

       
 
        dailyBreakdownChart.setLegendVisible(false);
        paneView.getChildren().add(dailyBreakdownChart);
    }

    

}
