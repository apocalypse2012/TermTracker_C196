package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.TermTracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseRepository {
    private static CourseRepository ourInstance;

    public LiveData<List<CourseEntity>> mCourses;

    public LiveData<List<CourseEntity>> tCourses;
    public Integer courseTerm;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static CourseRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CourseRepository(context);
        }
        return ourInstance;
    }

    private CourseRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mCourses = getAllCourses();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().insertAll(SampleData.getCourses(courseTerm));
            }
        });
    }

    private LiveData<List<CourseEntity>> getAllCourses() {
        return mDb.courseDao().getAll();
    }

    public void deleteAllCourses() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.assessmentDao().getCountByAnyCourse();
                numChildren += mDb.mentorDao().getCountByAnyCourse();
                if (numChildren == 0) {
                    mDb.courseDao().deleteAll();
                }
            }
        });
    }

    public CourseEntity getCourseById(int courseId) {
        return mDb.courseDao().getCourseById(courseId);
    }

    public void insertCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().insertCourse(course);
            }
        });
    }

    public void deleteCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.assessmentDao().getCountByCourse(course.getId());
                numChildren += mDb.mentorDao().getCountByCourse(course.getId());
                if (numChildren == 0) {
                    mDb.courseDao().deleteCourse(course);
                }
            }
        });
    }

    public LiveData<List<CourseEntity>> getCoursesByTerm(int courseTerm) {
        this.courseTerm = courseTerm;
        tCourses = mDb.courseDao().getCoursesByTerm(courseTerm);
        return tCourses;
    }

}
