package org.example.Messeging;

import java.sql.Timestamp;

public class Message {
    String Content;
    String From;
    Timestamp timestamp;

    public Message(String content, String from, Timestamp timestamp) {
        Content = content;
        From = from;
        this.timestamp = timestamp;
    }

    public Message(String content, String from) {
        Content = content;
        From = from;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String toString() {
        return this.Content;
    }

    public String getContent() {
        return Content;
    }

    public String getFrom() {
        return From;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
