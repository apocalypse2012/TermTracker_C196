package com.example.TermTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "mentors",
        foreignKeys = @ForeignKey(entity = CourseEntity.class,
                parentColumns = "id",
                childColumns = "course"))//,
                //onDelete = ForeignKey.CASCADE))
public class MentorEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phone;
    private String email;
    private int course;

    @Ignore
    public MentorEntity() {
    }

    public MentorEntity(int id, String name, String phone, String email, int course) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.course = course;
    }

    @Ignore
    public MentorEntity(String name, String phone, String email, int course) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "MentorEntity{" +
                "id=" + id +
                ", name=" + name +
                ", phone='" + phone + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
