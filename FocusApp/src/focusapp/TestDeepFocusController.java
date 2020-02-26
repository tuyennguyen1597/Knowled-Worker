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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author cryst
 */
public class TestDeepFocusController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Database db = new Database();
    PageSwitchHelper switchPage = new PageSwitchHelper();
    Stopwatch stopwatch = new Stopwatch();
    Task task = new Task();

    @FXML
    private ListView<Task> masterSideMenu;

    @FXML
    private Label title;

    @FXML
    private Label startTimePrompt;

    @FXML
    private Label dynamicLabel;

    @FXML
    private Label description;

    @FXML
    private Label digitalClock;

    @FXML
    private Label dateToday;

    @FXML
    private Circle start;

    @FXML
    private Circle stop;

    @FXML
    private Button riverFlute;

    @FXML
    private Button concentrationMusic;

    List<Task> list = new ArrayList<Task>();
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel1;
    

    @FXML
    private void gentlePiano(ActionEvent event) throws IOException {

        //System.out.println("Playing: relaxingpianomusic.mp3");
        if (MusicPlaybackHelper.playMusic("jellyfish.mp3")) {
        } else {
            MusicPlaybackHelper.globalMediaPlayer.stop();
        }
        //System.out.println("Playing: finished playing relaxingpianomusic.mp3");
    }

    @FXML
    private void riverFlute(ActionEvent event) throws IOException {

        if (MusicPlaybackHelper.playMusic("riverflute.mp3")) {
        } else {
            MusicPlaybackHelper.globalMediaPlayer.stop();
        }
    }

    @FXML
    private void concentrationMusic(ActionEvent event) throws IOException {

        if (MusicPlaybackHelper.playMusic("concentration.mp3")) {
        } else {
            MusicPlaybackHelper.globalMediaPlayer.stop();
        }
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
    public void aboutClick(ActionEvent event) throws IOException {
        switchPage.switcher(event, "About.fxml");
    }

    @FXML
    public void faqClick(ActionEvent event) throws IOException{
        switchPage.switcher(event, "FAQ.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            dynamicLabel.setText("Please select a task from the sidebar");
            start.setVisible(false);
            stop.setVisible(false);

            getTask();
            clock();
            date();
        } catch (SQLException ex) {
            Logger.getLogger(TestDeepFocusController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TestDeepFocusController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Task x : list) {
            masterSideMenu.getItems().add(x);

        }
    }

    @FXML
    private void userSidebarClick() {
        Task selectedTask = masterSideMenu.getSelectionModel().getSelectedItem();
        titleLabel.setVisible(true);
        title.setVisible(true);
        title.setText(selectedTask.getTaskName());

        dynamicLabel.setText("Click to start timer: ");

        start.setVisible(true);
        stop.setVisible(true);

        descriptionLabel1.setVisible(true);
        description.setVisible(true);
        description.setText(selectedTask.getDescription());

        task.setTaskID(selectedTask.getTaskID());
        task.setCatID(selectedTask.getCatID());

    }

    private void getTask() throws SQLException, ParseException {

        String x = "select * from task;";
        ResultSet rs = db.getResultSet(x);
        //add a new piechart data item for every row in the pieChart view
        while (rs.next()) {

            list.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8)));

        }

    }

    public void clock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            digitalClock.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void date() {
        dateToday.setText("Today's date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @FXML
    public void startTimer(MouseEvent event) throws IOException {
        start.setVisible(false);
        start.toBack();
        stop.toFront();
        stopwatch.start();
        dynamicLabel.setText("Click to Stop Timer: ");
        String captureTime = (String) digitalClock.getText();
        startTimePrompt.setText("Timer Started at : " + captureTime);

    }

    @FXML
    public void stopTimer(MouseEvent event) throws IOException, SQLException {
        start.setVisible(true);
        start.toFront();
        stopwatch.stop();
        dynamicLabel.setText("Click to start timer: ");
        startTimePrompt.setText("Data Captured");

        //take listview data and insert into entry with duration
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String insertEntry = "INSERT INTO entry (t_id, start, end, duration, cat_id, do_date) VALUES (" + task.getTaskID() + "," + stopwatch.getStartTime()
                + ", " + stopwatch.getEndTime() + ", " + stopwatch.getElapsedTime() + ", " + task.getCatID() + ",'"
                + date + "');";

        db.insertStatement(insertEntry);

    }

}
