
package com.example.demo.dtos;


/**
 * Represents a content parameter for the API.
 */
public class Content {
    
    private String type;
    private String text; // solo se usa si type = "text"
    private ImageUrl image_url; // solo se usa si type = "image_url"
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public ImageUrl getImage_url() {
        return image_url;
    }
    public void setImage_url(ImageUrl image_url) {
        this.image_url = image_url;
    }

}