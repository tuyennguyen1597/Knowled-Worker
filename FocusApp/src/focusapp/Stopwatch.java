/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

/**
 *
 * @author cryst
 */
public class Stopwatch {

    private long startTime;
    private long endTime;
    private boolean started;

    public Stopwatch() {

    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setStarted(boolean started) {
        this.started = started;

    }

    public boolean getStarted() {
        return this.started;
    }

    public Stopwatch(long startTime, long endTime, boolean started) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.started = started;
    }

    public boolean start() {
        if (this.startTime == 0) {
            this.startTime = System.currentTimeMillis();
            return true;
        } else {
            return false;

        }
    }

    public boolean stop() {
        if (this.startTime != 0) {
            this.endTime = System.currentTimeMillis();
            return true;
        } else {
            return false;

        }
    }

    public long getElapsedTime() {
        if (this.endTime == 0) {
            return 0;
        } else {
            return (this.endTime - this.startTime);
        }

    }
}
