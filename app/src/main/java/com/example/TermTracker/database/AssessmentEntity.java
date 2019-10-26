package com.example.TermTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = CourseEntity.class,
                parentColumns = "id",
                childColumns = "course"))//,
//onDelete = ForeignKey.CASCADE))
public class AssessmentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String title;
    private int course;

    @Ignore
    public AssessmentEntity() {
    }

    public AssessmentEntity(int id, String date, String title, int course) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.course = course;
    }

    @Ignore
    public AssessmentEntity(String date, String title, int course) {
        this.date = date;
        this.title = title;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
