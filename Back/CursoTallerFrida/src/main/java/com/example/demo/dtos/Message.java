
package com.example.demo.dtos;

import java.util.List;

/**
 * Represents a message parameter for the API.
 */
public class Message {
    public String role;
    public List<Content> content;
    public String getRole() {
        return role;
    }
    public List<Content> getContent() {
        return content;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setContent(List<Content> content) {
        this.content = content;
    }

    
}