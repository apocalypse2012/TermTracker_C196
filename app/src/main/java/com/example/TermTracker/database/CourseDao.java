package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseEntity> courses);

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM courses WHERE id = :id")
    CourseEntity getCourseById(int id);

    @Query("SELECT * FROM courses WHERE term = :term")
    LiveData<List<CourseEntity>> getCoursesByTerm(int term);

    @Query("SELECT * FROM courses ORDER BY start DESC")
    LiveData<List<CourseEntity>> getAll();

    @Query("DELETE FROM courses")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM courses")
    int getCount();

    @Query("SELECT COUNT(*) FROM courses WHERE term = :term")
    int getCountByTerm(int term);

    @Query("SELECT COUNT(*) FROM courses WHERE term IS NOT NULL")
    int getCountByAnyTerm();

}
