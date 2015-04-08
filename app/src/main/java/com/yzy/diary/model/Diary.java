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

    public Diary(){

    }

    public Diary(String label, String content) {
        this.label = label;
        this.content = content;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
