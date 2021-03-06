package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskNode implements Comparable<TaskNode> {
    //класс заметок
    private String title;
    private String description;
    private int key;
    private boolean changed;
    private String calendar;
    private String type;

    public TaskNode() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskNode(String title, String description, int key, String calendar, String type) {
        this.title = title;
        this.description = description;
        //   this.date = date;
        this.key = key;
        this.calendar = calendar;
        this.type = type;
        changed = false;
    }

    public boolean getChanged() {
        return this.changed;
    }

    public void setChanged(boolean c) {
        this.changed = c;

    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }


    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }


    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int compareTo(TaskNode t) {
        Calendar calCur = Calendar.getInstance(), calTemp = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            calCur.setTime(dateFormat.parse(this.calendar));
            calTemp.setTime(dateFormat.parse(t.calendar));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calCur.compareTo(calTemp);
    }
}
