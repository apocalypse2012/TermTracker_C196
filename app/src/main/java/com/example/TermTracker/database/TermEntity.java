package com.example.TermTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "terms")
public class TermEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String start;
    private String end;
    private String title;

    @Ignore
    public TermEntity() {
    }

    public TermEntity(int id, String start, String end, String title) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
    }

    @Ignore
    public TermEntity(String start, String end, String title) {
        this.start = start;
        this.end = end;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", title='" + title + '\'' +
                '}';
    }
}
