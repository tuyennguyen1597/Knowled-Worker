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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author jadenguyen
 */
public class AddTaskController implements Initializable {
    Database database = new Database();
    PageSwitchHelper pageSwitch = new PageSwitchHelper();
    
    @FXML
    private TextField nameTextArea;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button save;
    @FXML
    private MenuButton catMenu;
    @FXML
    private DatePicker doDate;
    @FXML
    private DatePicker estimatedDue;
    @FXML
    private RadioButton todayRadio;
    @FXML
    private ToggleGroup statusGroup;
    @FXML
    private RadioButton tomorrowRadio;
    @FXML
    private RadioButton daysRadio;
    String catInput;
    public Task task = new Task();
    public String nameTask;
    public String descriptionTask;
    public String catTask;
    public ArrayList<String> selectedDoDate = new ArrayList<>();
    public ArrayList<String> selectedDueDate = new ArrayList<>();
    public ArrayList<Integer> selectedStatus = new ArrayList<>();
    public ArrayList<Integer> selectedCat = new ArrayList<>();
    public int priority;
    
    @FXML
    private Button goBackButt;
    @FXML
    private Button otherButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField otherText;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label catLabel;
    @FXML
    private Slider priorityBar;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedStatus.add(1);
        try {
            catMenuButton();
        } catch (SQLException ex) {
            Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
        priorityBar.valueProperty().addListener( 
            new ChangeListener<Number>() { 

            public void changed(ObservableValue <? extends Number >  
                      observable, Number oldValue, Number newValue) 
            {
                    priority = newValue.intValue();
            } 
        }); 
    }
    
    @FXML
    public void doDatePicker(ActionEvent event) throws ParseException {
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
    public void dueDatePicker(ActionEvent event) {
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
    
    public void catMenuButton() throws SQLException {
        ResultSet rs = database.getResultSet("SELECT NAME FROM CATEGORY;");
        while(rs.next()) {
            MenuItem item = new MenuItem(rs.getString(1));
            catMenu.getItems().add(item);
            item.setOnAction(event1);
        }
    }
    
    @FXML
    public void clickedOther (ActionEvent event) throws IOException, SQLException {
        otherText.setVisible(true);
        otherText.setEditable(true);
        colorPicker.setVisible(true);
        
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
    public void statusSelected(ActionEvent event) throws SQLException {
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
    public void clickedBack(ActionEvent event) throws IOException {
        pageSwitch.switcher(event, "KanboardScreen.fxml");
    }
    
    public void clickedPriority() {
        
    }
    
    @FXML
    public void clickedSave(ActionEvent event) throws SQLException {
        
        String name = nameTextArea.getText();
        String description = descriptionArea.getText();
        
        if (name.equals("")) {
            nameLabel.setVisible(true);
        } else if (description.equals("")) {
            nameLabel.setVisible(false);
            descriptionLabel.setVisible(true);
        } else if (selectedStatus.size() == 0) {
            nameLabel.setVisible(false);
            descriptionLabel.setVisible(false);
            statusLabel.setVisible(true);
        } else if (selectedCat.size() == 0 && otherText.isEditable() == false) {
            nameLabel.setVisible(false);
            descriptionLabel.setVisible(false);
            statusLabel.setVisible(false);
            catLabel.setVisible(true);  
        } else if (selectedDoDate.size() == 0 || selectedDueDate.size() == 0) {
            nameLabel.setVisible(false);
            descriptionLabel.setVisible(false);
            statusLabel.setVisible(false);
            catLabel.setVisible(false);
            dateLabel.setVisible(true);
        } else {
            if (otherText.isEditable() == true && otherText.getText().isEmpty() == false ) {
                catInput = otherText.getText();
                String color = String.valueOf(colorPicker.getValue());
                String statement = "INSERT INTO CATEGORY (NAME, COLOUR) ";
                statement += "VALUES ('" + catInput + "', '" + color + "');";
                database.insertStatement(statement);
                ResultSet rs = database.getResultSet("SELECT CAT_ID FROM CATEGORY WHERE NAME = '" + catInput + "';");
                selectedCat.add(rs.getInt(1));
            }
            String doDate = returnDoDate(selectedDoDate);
            String dueDate = returnDueDate(selectedDueDate);
            int category = returnCat(selectedCat);
            int status = returnStatus(selectedStatus);
            
            System.out.println(priority + " pri");
            String statement = "INSERT INTO TASK (TITLE, DESCRIPTION, DO_DATE, DUE_DATE, CAT_ID, STATUS, PRIORITY) ";
            statement += "VALUES ('" + name + "', '" + description;
            statement += "', '" + doDate + "', '" + dueDate;
            statement += "', " + category + ", " + status + ", " + priority + ");";

            database.insertStatement(statement);
            nameLabel.setVisible(false);
            descriptionLabel.setVisible(false);
            statusLabel.setVisible(false);
            catLabel.setVisible(false);
            dateLabel.setVisible(false);
            errorLabel.setVisible(true);
        }
    }
}
