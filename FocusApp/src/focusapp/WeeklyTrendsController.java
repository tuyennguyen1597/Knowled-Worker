///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
package focusapp;

import com.sun.javafx.charts.Legend;
import static focusapp.Database.conn;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class WeeklyTrendsController implements Initializable {

    Database db = new Database();
    PageSwitchHelper switchPage = new PageSwitchHelper();
    //n is a counter to specify number of categories shown and how many series to create
    double weeks;
    int categories;
    List<ArrayList<XYChart.Series<String, Double>>> list = new ArrayList<ArrayList<XYChart.Series<String, Double>>>((int) weeks);
    List<Activity> activityList = new ArrayList<>();

    List<XYChart.Series<String, Double>> series = new ArrayList<>();
    ObservableList<String> xAxisWeeks = FXCollections.observableArrayList();

    @FXML
    private TableView<Activity> taskMenu;

    @FXML
    private TableColumn<Activity, String> categoryColumn;

    @FXML
    private AnchorPane apPie;

    @FXML
    private LineChart weeklyLineChart;


    @FXML
    private VBox vbox;


    @FXML
    private Button clear;

    //CategoryAxis xAxis = (CategoryAxis) weeklyLineChart.getXAxis();
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
    public void aboutClick(ActionEvent event) throws IOException {
        switchPage.switcher(event, "About.fxml");
    }
    
    @FXML
    public void faqClick(ActionEvent event) throws IOException{
        switchPage.switcher(event, "About.fxml");
    }
    
        @FXML
    private void deepFocusClick(ActionEvent event) throws IOException, SQLException {
        switchPage.switcher(event, "TestDeepFocus.fxml");

    } 
   

  
    public void getConnection() throws SQLException {
      conn = DriverManager.getConnection("jdbc:sqlite:focusAppDB.db");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getConnection();


           loadChart();
        } catch (SQLException ex) {
            Logger.getLogger(WeeklyTrendsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadChart() {
        CategoryAxis xAxis = (CategoryAxis) weeklyLineChart.getXAxis();

        System.out.println("160");

        weeklyLineChart.setTitle("All Categories");
        try {

            //ResultSet rs2 = db.getResultSet("select title, name, weeknumb, strftime('%W', now) sysWeekNumb, percentage from weeklytrends2 order by weeknumb desc;");
            //number of categories in total
            ResultSet rs = conn.createStatement().executeQuery("select count(name) from (select distinct name from WEEKLYTRENDS2);");
            System.out.println("175");

            ResultSet rs3 = conn.createStatement().executeQuery("select distinct name from weeklytrends2;");
            System.out.println("178");

            int counter = 0;
            //creating an array (categories or graphs) of arraylists (lines) which will contain series
            while (rs.next()) {
                categories = rs.getInt(1);
            }

            System.out.println("190");

            ArrayList<Activity>[] arrayChart = new ArrayList[categories];

            ObservableList<String> categoryList = FXCollections.observableArrayList();

            //create category names and insert into an arrayList
            //create add a new line/series for each category that exists
            System.out.println("202");

            while (rs3.next()) {
                categoryList.add(rs3.getString(1));
                series.add(new XYChart.Series<String, Double>());

            }

            System.out.println("210");

            //Add category values into the xAxis field -- this is incorrect, change to weeks
            ResultSet rs5 = conn.createStatement().executeQuery("select min(weeknumb), max(weeknumb) from WEEKLYTRENDS2;");

            while (rs5.next()) {

                for (int i = rs5.getInt(1); i <= rs5.getInt(2); i++) {
                    xAxisWeeks.add("Week " + i);
                }

            }

            xAxis.setCategories(xAxisWeeks);

            System.out.println("215");

            //Iterate through the category list
            //for each row, if category name = name from weeklytrends2, add this data into arrayChart[i]
            int arrayChartCount = 0;
            int i = 0;

            System.out.println(categoryList.size());

            for (String a : categoryList) {
                System.out.println("iteration: " + i + ", " + a);
                arrayChart[i] = new ArrayList<Activity>();
                ResultSet rs4 = conn.createStatement().executeQuery("select name, weeknumb, percentage from weeklytrends2");

                {
                    while (rs4.next()) {
                        System.out.println(rs4.getString(1));

                        if (a.equals(rs4.getString(1))) {

                            arrayChart[i].add(new Activity(rs4.getString(1), rs4.getInt(2), rs4.getDouble(3)));
                            System.out.println(arrayChart[i]);

                        } else if (!a.equals(rs4.getString(1))) {
                            continue;
                        } else {
                            System.out.println("failed");
                            break;
                        }
                    }
                }
                i++;

            }

            System.out.println("230 SERIES SIZE" + series.size());
            //series.clear();
            //Add data (activities created) to the series objects creates 
            for (int b = 0; b < arrayChart.length; b++) {

                System.out.println("fail1");
                for (int d = 0; d < arrayChart[b].size(); d++) {
                    System.out.println("test");
                    System.out.println(arrayChart[b].get(d).getCategory() + " " + arrayChart[b].get(d).getPercentage());
                    //why is the below statement casting a boolean? 
                    System.out.println(series.get(b));

                    series.get(b).getData().add(new XYChart.Data(("Week " + arrayChart[b].get(d).getWeekNum()), arrayChart[b].get(d).getPercentage()));
                    System.out.println(series.get(b));
                    series.get(b).setName(arrayChart[b].get(d).getCategory());
                    System.out.println("fail2");
                }
                
                weeklyLineChart.getData().add(series.get(b));

            }
            System.out.println("fail3");

        } catch (SQLException ex) {
            Logger.getLogger(WeeklyTrendsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
