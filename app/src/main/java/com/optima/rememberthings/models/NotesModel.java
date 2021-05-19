package com.optima.rememberthings.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotesModel extends RealmObject {

    @PrimaryKey
    private String Id;
    private String Title;
    private String Body;
    private String Date;

    private byte[] ImageBytes;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public byte[] getImageBytes() {
        return ImageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        ImageBytes = imageBytes;
    }

}
