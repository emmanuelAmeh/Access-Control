package com.example.android.accesscontrol;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "visitor_table")
public class Visitor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String surname;
    private String firstname;
    private int accessCode;
    private long timeGenerated;
    private long timeLoggedIn;
    private long timeLoggedOut;

    public Visitor(String surname, String firstname, int accessCode, long timeGenerated, long timeLoggedIn, long timeLoggedOut) {
        this.surname = surname;
        this.firstname = firstname;
        this.accessCode = accessCode;
        this.timeGenerated = timeGenerated;
        this.timeLoggedIn = timeLoggedIn;
        this.timeLoggedOut = timeLoggedOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getAccessCode() {
        return accessCode;
    }

    public long getTimeGenerated() {
        return timeGenerated;
    }

    public long getTimeLoggedIn() {
        return timeLoggedIn;
    }

    public long getTimeLoggedOut() {
        return timeLoggedOut;
    }
}
