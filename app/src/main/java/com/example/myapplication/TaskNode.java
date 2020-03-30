package com.example.myapplication;

import java.util.GregorianCalendar;

public class TaskNode {
    private String title;
    private String description;
    private String date;
    private int key;
    private boolean changed;

    public TaskNode() {

    }

    public TaskNode(String title, String description, String date, int key) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.key = key;
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

    public String getDate() {
        return this.date;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoneNumber(String number) {
        this.date = number;
    }
}
