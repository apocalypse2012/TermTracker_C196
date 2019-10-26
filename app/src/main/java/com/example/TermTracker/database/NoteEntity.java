package com.example.TermTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ForeignKey;
import java.util.Date;


@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = AssessmentEntity.class,
                parentColumns = "id",
                childColumns = "assessment"))//,
//onDelete = ForeignKey.CASCADE))
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private String text;
    private int assessment;

    @Ignore
    public NoteEntity() {
    }

    public NoteEntity(int id, Date date, String text, int assessment) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.assessment = assessment;
    }

    @Ignore
    public NoteEntity(Date date, String text, int assessment) {
        this.date = date;
        this.text = text;
        this.assessment = assessment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
