
package com.example.demo.dtos;

import java.util.List;

/**
 * Represents the full API request payload.
 */
public class ApiRequestPayload {
    
    private String model;
    private List<Message> messages;
    private boolean stream;
    private boolean enable_caching;
    public String getModel() {
        return model;
    }
    public List<Message> getMessages() {
        return messages;
    }
    public boolean isStream() {
        return stream;
    }
    public boolean isEnable_caching() {
        return enable_caching;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    public void setStream(boolean stream) {
        this.stream = stream;
    }
    public void setEnable_caching(boolean enable_caching) {
        this.enable_caching = enable_caching;
    }


}