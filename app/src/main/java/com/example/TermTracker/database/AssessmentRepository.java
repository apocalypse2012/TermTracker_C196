package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.TermTracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentRepository {
    private static AssessmentRepository ourInstance;

    public LiveData<List<AssessmentEntity>> mAssessments;

    public LiveData<List<AssessmentEntity>> tAssessments;
    public Integer assessmentCourse;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AssessmentRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AssessmentRepository(context);
        }
        return ourInstance;
    }

    private AssessmentRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mAssessments = getAllAssessments();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDao().insertAll(SampleData.getAssessments(assessmentCourse));
            }
        });
    }

    private LiveData<List<AssessmentEntity>> getAllAssessments() {
        return mDb.assessmentDao().getAll();
    }

    public void deleteAllAssessments() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.noteDao().getCountByAnyAssessment();
                if (numChildren == 0) {
                    mDb.assessmentDao().deleteAll();
                }
            }
        });
    }

    public AssessmentEntity getAssessmentById(int assessmentId) {
        return mDb.assessmentDao().getAssessmentById(assessmentId);
    }

    public void insertAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDao().insertAssessment(assessment);
            }
        });
    }

    public void deleteAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.noteDao().getCountByAssessment(assessment.getId());
                if (numChildren == 0) {
                    mDb.assessmentDao().deleteAssessment(assessment);
                }
            }
        });
    }

    public LiveData<List<AssessmentEntity>> getAssessmentsByCourse(int assessmentCourse) {
        this.assessmentCourse = assessmentCourse;
        tAssessments = mDb.assessmentDao().getAssessmentByCourse(assessmentCourse);
        return tAssessments;
    }

}
