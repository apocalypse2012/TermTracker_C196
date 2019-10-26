package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.TermTracker.database.AssessmentEntity;
import com.example.TermTracker.database.AssessmentRepository;

import java.util.List;

public class AssessmentsListViewModel extends AndroidViewModel {

    public LiveData<List<AssessmentEntity>> mAssessments;
    private AssessmentRepository mRepository;

    public AssessmentsListViewModel(@NonNull Application application) {
        super(application);

        mRepository = AssessmentRepository.getInstance(application.getApplicationContext());
        mAssessments = mRepository.mAssessments;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllAssessments() {
        mRepository.deleteAllAssessments();
    }

    public void setCurrentCourse(int currentCourse) {
        mAssessments = mRepository.getAssessmentsByCourse(currentCourse);
    }

}

