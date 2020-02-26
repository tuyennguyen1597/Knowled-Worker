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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author jadenguyen
 */
public class KanboardScreenController implements Initializable {

    PageSwitchHelper switchPage = new PageSwitchHelper();
    //DeepFocusScreenController deepScreen = new DeepFocusScreenController();
    Database database = new Database();
    public static Task task = new Task();
    @FXML
    private HBox hBox;
    @FXML
    private VBox todayVBox;
    @FXML
    private VBox completedVBox;
    @FXML
    private VBox tomorrowVBox;
    @FXML
    private VBox daysVBox;
    @FXML
    private ToggleButton kanboardToggled;

    @FXML
    private ToggleButton pieChartToggled;

    @FXML
    private ToggleButton weeklyTrendsToggled;


    @FXML
    private ToggleButton dailyBDToggled;

    @FXML
    private ToggleButton weeklyBDToggled;

    private Pane form;
    
    @FXML
    private ToggleButton deepFocus;
    @FXML
    private Button addTaskButt;
    
    
    @FXML
    private AnchorPane scene;
    @FXML
    private Font x1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            List<Date> dueDates = new ArrayList<>();
            LocalDate localDate = LocalDate.now();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            Date date1 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String dateString = format.format(date1);
            ResultSet rs = database.getResultSet("SELECT T_ID, TITLE, DUE_DATE, STATUS FROM Task;");
            
            while (rs.next()) {
                dueDates.add((Date) new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(3)));
                
                //if duedDate < sysdate, add to completed
                int index = dueDates.size() - 1;
                int id = rs.getInt(1);
                //compare date operator 
                if (dueDates.get(index).compareTo(date1) < 0) {
                    String statement = "UPDATE TASK SET STATUS = 4 WHERE T_ID = " + id + ";";
                    database.insertStatement(statement);
                    
                    
                    
                    TextArea taskText = createTextArea(String.valueOf(rs.getInt(1)));
                    taskText.setText(rs.getString(2));
                    //HBox completedHbox = new HBox();
                    completedVBox.getChildren().add(taskText);
                    //completedHbox.getChildren().addAll(task);
                    
                } else {
                    if (rs.getInt(4) == 1) {
                        TextArea taskText = createTextArea(String.valueOf(rs.getInt(1)));
                        taskText.setText(rs.getString(2));
                        //HBox todayHbox = new HBox();
                        todayVBox.getChildren().add(taskText);
                        //todayHbox.getChildren().addAll(task);
                    } else if (rs.getInt(4) == 2) {
                        TextArea taskText = createTextArea(String.valueOf(rs.getInt(1)));
                        taskText.setText(rs.getString(2));
                        //HBox tomorrowHbox = new HBox();
                        tomorrowVBox.getChildren().add(taskText);
                        //tomorrowHbox.getChildren().addAll(task);
                    } else if (rs.getInt(4) == 3) {
                        TextArea taskText = createTextArea(String.valueOf(rs.getInt(1)));
                        taskText.setText(rs.getString(2));
                        //HBox daysHbox = new HBox();
                        daysVBox.getChildren().add(taskText);
                        //daysHbox.getChildren().addAll(task);
                    } else if (rs.getInt(4) == 4) {
                        TextArea taskText = createTextArea(String.valueOf(rs.getInt(1)));
                        taskText.setText(rs.getString(2));
                        //HBox daysHbox = new HBox();
                        completedVBox.getChildren().add(taskText);
                        //daysHbox.getChildren().addAll(task);
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        todayVBox.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    handleDragDropped(event, todayVBox);
                } catch (SQLException ex) {
                    Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        tomorrowVBox.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    handleDragDropped(event, tomorrowVBox);
                } catch (SQLException ex) {
                    Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        daysVBox.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    handleDragDropped(event, daysVBox);
                } catch (SQLException ex) {
                    Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        completedVBox.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    handleDragDropped(event, completedVBox);
                } catch (SQLException ex) {
                    Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });

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
    public void kanboardClick(ActionEvent event) {
        //welcomeHBox.setVisible(false);
        hBox.setVisible(true);
        kanboardToggled.setBackground(Background.EMPTY);

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
    // Add today, tomorrow, completed, days Task textArea
    @FXML
    public void todayAddTask(ActionEvent event) throws IOException {
        switchPage.switcher(event, "AddTask.fxml");
    }



    public TextArea createTextArea(String id) {
        TextArea textArea = new TextArea();
        
        textArea.setWrapText(true);
        textArea.setMaxSize(125, 50);
        textArea.setEditable(false);
        textArea.setId(id);
        textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //KanboardScreenController kanban = new KanboardScreenController();
                if (textArea.isEditable() == false) {
                    try {
                        String id = textArea.getId();
                        String statement = "SELECT * FROM TASK WHERE T_ID = " + id + ";";
                        ResultSet rs = database.getResultSet(statement);
                        //﻿T_ID, TITLE, DESCRIPTION, DO_DATE, DUE_DATE, PRIORITY, Cat_ID, STATUS
                        task.setTaskID(rs.getInt(1));
                        task.setTaskName(rs.getString(2));
                        task.setDescription(rs.getString(3));
                        task.setDoDate(rs.getString(4));
                        task.setDueDate(rs.getString(5));
                        task.setPriority(rs.getInt(6));
                        task.setCatID(rs.getInt(7));
                        task.setStatus(rs.getInt(8));
                        System.out.println(task.getTaskName());
                        switchPage.switcher(event, "EditTask.fxml");
                    } catch (IOException ex) {
                        Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(KanboardScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });
        
        textArea.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard dragboard = textArea.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(id);
                System.out.println(id + " is dragging");
                dragboard.setContent(content);
                event.consume();
            }
        
        });
        return textArea;
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        /* data is dragged over the target */
                //System.out.println("onDragOver");
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                
                event.consume();
    }

    private void handleDragDropped(DragEvent event, VBox vBox) throws SQLException {
        /* data dropped */
        System.out.println("onDragDropped");
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        
        boolean success = false;
        if (db.hasString()) {
            System.out.println("Dagboard: " + db.getString());
            if (vBox == todayVBox) {
                todayVBox.getChildren().add((TextArea) scene.lookup("#" + db.getString()));
                String statement = "UPDATE TASK SET STATUS = 1 WHERE T_ID = " +db.getString() + ";";
                database.insertStatement(statement);
            } else if (vBox == tomorrowVBox) {
                tomorrowVBox.getChildren().add((TextArea) scene.lookup("#" + db.getString()));
                String statement = "UPDATE TASK SET STATUS = 2 WHERE T_ID = " +db.getString() + ";";
                database.insertStatement(statement);
            } else if (vBox == daysVBox) {
                daysVBox.getChildren().add((TextArea) scene.lookup("#" + db.getString()));
                String statement = "UPDATE TASK SET STATUS = 3 WHERE T_ID = " +db.getString() + ";";
                database.insertStatement(statement);
            } else {
                completedVBox.getChildren().add((TextArea) scene.lookup("#" + db.getString()));
                String statement = "UPDATE TASK SET STATUS = 4 WHERE T_ID = " +db.getString() + ";";
                database.insertStatement(statement);
            }
            success = true;
        }
        event.setDropCompleted(success);

        event.consume();
    }
}
