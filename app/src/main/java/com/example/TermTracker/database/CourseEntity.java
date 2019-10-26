package com.example.TermTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = TermEntity.class,
                parentColumns = "id",
                childColumns = "term"))//,
//onDelete = ForeignKey.CASCADE))
public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String start;
    private String end;
    private String title;
    private String status;
    private int term;

    @Ignore
    public CourseEntity() {
    }

    public CourseEntity(int id, String start, String end, String title, String status, int term) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.status = status;
        this.term = term;
    }

    @Ignore
    public CourseEntity(String start, String end, String title, String status, int term) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.status = status;
        this.term = term;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", title='" + title + '\'' +
                ", term='" + term + '\'' +
                '}';
    }
}
