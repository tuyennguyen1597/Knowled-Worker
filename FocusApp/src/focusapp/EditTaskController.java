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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author jadenguyen
 */
public class EditTaskController implements Initializable {

    @FXML
    private TextField nameTextArea;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private MenuButton catMenu;
    @FXML
    private Button otherButton;
    @FXML
    private TextField otherText;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private RadioButton todayRadio;
    @FXML
    private ToggleGroup statusGroup;
    @FXML
    private RadioButton tomorrowRadio;
    @FXML
    private RadioButton daysRadio;
    @FXML
    private TextField priorityText;
    @FXML
    private DatePicker doDate;
    @FXML
    private DatePicker estimatedDue;
    @FXML
    private Button goBackButt;
    
    public String nameTask;
    public String descriptionTask;
    public String catTask;
    public ArrayList<String> selectedDoDate = new ArrayList<>();
    public ArrayList<String> selectedDueDate = new ArrayList<>();
    public ArrayList<Integer> selectedStatus = new ArrayList<>();
    public ArrayList<Integer> selectedCat = new ArrayList<>();
    public int priority;
    String doDate1;
    String dueDate;
    String description;
    int category;
    int status;
    
    Database database = new Database();
    KanboardScreenController kanban = new KanboardScreenController();
    PageSwitchHelper pageSwitch = new PageSwitchHelper();
    public int id;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Slider priorityBar;
    @FXML
    private Label editLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id = kanban.task.getTaskID();
        
        try {
            catMenuButton();
        } catch (SQLException ex) {
            Logger.getLogger(EditTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            String statement = "SELECT T_ID, TITLE, DESCRIPTION, DO_DATE, DUE_DATE, PRIORITY, CAT_ID, (SELECT name from Category where Cat_ID = (select Cat_ID from task where t_id = " + id + ")) AS 'Cat_Name' , Status from task where t_id = " + id + ";";
            ResultSet rs = database.getResultSet(statement);
            doDate1 = rs.getString(4);
            dueDate = rs.getString(5);
            category = rs.getInt(7);
            
            nameTextArea.setText(rs.getString(2));
            descriptionArea.setText(rs.getString(3));
            String doDateData = rs.getString(4);
            LocalDate doDateLD = LocalDate.parse(doDateData);
            doDate.setValue(doDateLD);
            String dueDateData = rs.getString(5);
            LocalDate dueDateLD = LocalDate.parse(dueDateData);
            estimatedDue.setValue(dueDateLD);
            priority = rs.getInt(6);
            priorityText.setText(String.valueOf(rs.getInt(6)));
            priorityText.setEditable(false);
            priorityText.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    priorityBar.setVisible(true);
                }
                
            });
            priorityBar.valueProperty().addListener( 
                new ChangeListener<Number>() { 

                public void changed(ObservableValue <? extends Number >  
                          observable, Number oldValue, Number newValue) 
                {
                        priority = newValue.intValue();
                } 
            });
            otherText.setVisible(true);
            otherText.setText(rs.getString(8));
            otherText.setEditable(false);
            status = rs.getInt(9);
            if (status == 2) {
                tomorrowRadio.setSelected(true);
            } else if (status == 3) {
                daysRadio.setSelected(true);
            } else {
                todayRadio.setSelected(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public void catMenuButton() throws SQLException {
        ResultSet rs = database.getResultSet("SELECT NAME FROM CATEGORY;");
        while(rs.next()) {
            MenuItem item = new MenuItem(rs.getString(1));
            catMenu.getItems().add(item);
            item.setOnAction(event1);
        }
    }
    
    //Pop up the user's selection from the menu buttonxs
    EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() { 
        @Override
        public void handle(ActionEvent e) 
        { 
            try {
                otherText.setVisible(true);
                otherText.setEditable(false);
                colorPicker.setVisible(false);
                otherText.setText(((MenuItem)e.getSource()).getText());
                catTask = ((MenuItem)e.getSource()).getText();
                //select the category ID using category name
                //then add it into the Task's object
                ResultSet rs = database.getResultSet("SELECT CAT_ID FROM CATEGORY WHERE NAME = '" + catTask + "';");
                selectedCat.add(rs.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    };
    
    @FXML
    public void clickedOther (ActionEvent event) throws IOException, SQLException {
        otherText.setVisible(true);
        otherText.setEditable(true);
        colorPicker.setVisible(true);
        
    }

    @FXML
    private void statusSelected(ActionEvent event) throws SQLException {
        String status;
        if(todayRadio.isSelected()) {
            status = todayRadio.getText();
        } else if (tomorrowRadio.isSelected()) {
            status = tomorrowRadio.getText();
        } else {
            status = daysRadio.getText();
        }
        ResultSet rs = database.getResultSet("SELECT STATUS_ID FROM STATUS WHERE NAME = '" + status + "';");
        selectedStatus.add(rs.getInt(1));
    }

    @FXML
    private void doDatePicker(ActionEvent event) {
        String pattern = "yyyy-MM-dd";
        doDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override 
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        selectedDoDate.add(((TextField)doDate.getEditor()).getText());
    }

    @FXML
    private void dueDatePicker(ActionEvent event) {
        String pattern = "yyyy-MM-dd";
        estimatedDue.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override 
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        selectedDueDate.add(((TextField)estimatedDue.getEditor()).getText());
    }
    public int returnStatus(ArrayList status) {
        int lastStatus;
        if (status.size() < 1) {
            lastStatus = 1;
        } else {
            lastStatus = (int) status.get(status.size() - 1);
        }
        return lastStatus;
    }
    
    public String returnDoDate(ArrayList doDateList) {
        String lastDoDate;
        lastDoDate = (String) doDateList.get(doDateList.size() - 1);
        return lastDoDate;
    }
    
    public String returnDueDate(ArrayList dueDateList) {
        String lastDueDate;
        lastDueDate = (String) dueDateList.get(dueDateList.size() - 1);
        return lastDueDate;
    }
    
    public int returnCat(ArrayList catList) {
        int lastCat;
        lastCat = (int) catList.get(catList.size() - 1);
        return lastCat;
    }

    @FXML
    private void clickedBack(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "KanboardScreen.fxml");
    }
    
    @FXML
    private void clickedEdit(ActionEvent event) throws IOException, SQLException {
        
        String name = nameTextArea.getText();
        System.out.println(name + " task name");
        description = descriptionArea.getText();
        if (selectedStatus.size() > 0) {
            status = returnStatus(selectedStatus);
        }
        if (selectedCat.size() > 0 && otherText.isEditable() == false) {
            category = returnCat(selectedCat);
        } else if (otherText.isEditable() == true && otherText.getText().isEmpty() == false) {
            String cat = otherText.getText();
            String color = String.valueOf(colorPicker.getValue());
            String statement = "INSERT INTO CATEGORY (NAME, COLOUR) ";
            statement += "VALUES ('" + cat + "', '" + color + "');";
            database.insertStatement(statement);

            ResultSet rs = database.getResultSet("SELECT CAT_ID FROM CATEGORY WHERE NAME = '" + cat + "';");
            selectedCat.add(rs.getInt(1));
            category = returnCat(selectedCat);
        }
        if (selectedDoDate.size() > 0 ) {
            doDate1 = returnDoDate(selectedDoDate);
        }
        if ( selectedDueDate.size() > 0) {
            dueDate = returnDueDate(selectedDueDate);
        }
        
        String statement = "UPDATE TASK SET ";
        statement += "TITLE = '" + name + "', ";
        statement += "DESCRIPTION = '" + description + "', ";
        statement += "DO_DATE = '" + doDate1 + "', ";
        statement += "DUE_DATE = '" + dueDate + "', ";
        statement += "PRIORITY = '" + priority + "', ";
        statement += "CAT_ID = '" + category + "', ";
        statement += "STATUS = '" + status + "' WHERE T_ID = " + id;
        database.insertStatement(statement);
        editLabel.setVisible(true);
    }

    @FXML
    private void clickedDelete(ActionEvent event) throws SQLException, IOException {
        String statement = "DELETE FROM TASK WHERE T_ID = " + id + ";";
        database.insertStatement(statement);
        pageSwitch.switcher(event, "KanboardScreen.fxml");
    }
}
