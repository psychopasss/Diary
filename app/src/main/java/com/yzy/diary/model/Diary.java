package com.yzy.diary.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Diary implements Serializable {
    /**
     *
     */

    private int _id;
    private String label;
    private String content;
    private Timestamp date;
    private String weather;
    private String mood;

    public Diary() {

    }

    public Diary(String label, String content, String mood, String weather) {
        this.label = label;
        this.content = content;
        this.mood = mood;
        this.weather = weather;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
