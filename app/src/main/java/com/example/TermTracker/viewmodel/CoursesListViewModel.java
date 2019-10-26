package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.TermTracker.database.CourseEntity;
import com.example.TermTracker.database.CourseRepository;

import java.util.List;

public class CoursesListViewModel extends AndroidViewModel {

    public LiveData<List<CourseEntity>> mCourses;
    private CourseRepository mRepository;

    public CoursesListViewModel(@NonNull Application application) {
        super(application);

        mRepository = CourseRepository.getInstance(application.getApplicationContext());
        mCourses = mRepository.mCourses;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllCourses() {
        mRepository.deleteAllCourses();
    }

    public void setCurrentTerm(int currentTerm) {
        mCourses = mRepository.getCoursesByTerm(currentTerm);
    }

}

