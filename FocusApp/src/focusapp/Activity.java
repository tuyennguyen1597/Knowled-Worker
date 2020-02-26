/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author cryst
 */
public class Activity {

    private String taskName;

    private long duration;
    private double durationHours;
    private Date doDate;

    private String category;
    private StringProperty cat;

    private Date sysDate;

    private Date weekAgoDate;
   
    
    private int weekNum;
    private double percentage;
    
    
    public Activity(String category, int weekNum, double percentage){
        this.category=category;
        this.weekNum=weekNum;
        this.percentage=percentage;
        
    }

    public Activity(String taskName, long duration, Date doDate, String category, Date sysDate, Date weekAgoDate) {
        this.taskName = taskName;
        this.duration = duration;
        this.category = category;
        this.doDate = doDate;
        this.sysDate = sysDate;
        this.weekAgoDate = weekAgoDate;
        
        
       
        
        
    }
    
    public Activity (String category, double durationHours){
        this.category=category;
        this.durationHours=durationHours;
    }
            
        
    public Activity(String cat){
        this.cat= new SimpleStringProperty(cat);
    }
    
    

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(double durationHours) {
        this.durationHours = durationHours;
    }
    
    

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getDoDate() {
        return doDate;
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate;
    }

    public Date getSysDate() {
        return sysDate;
    }

    public void setSysDate(Date sysDate) {
        this.sysDate = sysDate;
    }

    public Date getWeekAgoDate() {
        return weekAgoDate;
    }

    public void setWeekAgoDate(Date weekAgoDate) {
        this.weekAgoDate = weekAgoDate;
    }

    public StringProperty getCat() {
        return cat;
    }

    public void setCat(StringProperty cat) {
        this.cat = cat;
    }
    

}
