/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.util.Date;

/**
 *
 * @author jadenguyen
 */
public class Task {
    private int taskID;
    private String taskName;
    private String description;
    private String doDate;
    private String dueDate;
    private int priority;
    private int catID;
    private int status;
    
    
    public Task() {}
    
    public Task(int taskID, String taskName){
        this.taskID = taskID;
        this.taskName = taskName;
    }

    public Task(int taskID, String taskName, String description, String doDate, String dueDate, int priority, int catID, int status) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.doDate = doDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.catID = catID;
        this.status = status;
    }
    
    public Task(int taskID, int catID){
        this.taskID=taskID;
        this.catID=catID;
    }
    
    public Task( String taskName, String description, String doDate, String dueDate, int priority, int catID, int status) {
        this.taskName = taskName;
        this.description = description;
        this.doDate = doDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.catID = catID;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return taskName;
    }
    
    
    
}
