package com.ngobrol.Message;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by azlan on 7/6/16.
 */
public class Chat {
    private String uid;
    private String name;
    private String body;
    private long timestamp;

    public Chat() {
    }

    public Chat(String uid, String name, String body, long timestamp) {
        this.uid = uid;
        this.name = name;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getDate(){
        return new Date(this.getTimestamp());
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", getUid());
        result.put("name", getName());
        result.put("body", getBody());
        result.put("timestamp", getTimestamp());
        return result;
    }
}
