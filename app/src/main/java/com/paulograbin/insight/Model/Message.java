package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 15/07/15.
 */
public class Message {

    long id;
    String text;

    public Message() {

    }

    public Message(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
