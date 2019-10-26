package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity assessmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AssessmentEntity> assessments);

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessments WHERE id = :id")
    AssessmentEntity getAssessmentById(int id);

    @Query("SELECT * FROM assessments WHERE course = :course")
    LiveData<List<AssessmentEntity>> getAssessmentByCourse(int course);

    @Query("SELECT * FROM assessments ORDER BY date DESC")
    LiveData<List<AssessmentEntity>> getAll();

    @Query("DELETE FROM assessments")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM assessments")
    int getCount();

    @Query("SELECT COUNT(*) FROM assessments WHERE course = :course")
    int getCountByCourse(int course);

    @Query("SELECT COUNT(*) FROM assessments WHERE course IS NOT NULL")
    int getCountByAnyCourse();

}
