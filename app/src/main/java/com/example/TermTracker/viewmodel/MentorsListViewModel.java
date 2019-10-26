package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.TermTracker.database.MentorEntity;
import com.example.TermTracker.database.MentorRepository;

import java.util.List;

public class MentorsListViewModel extends AndroidViewModel {

    public LiveData<List<MentorEntity>> mMentors;
    private MentorRepository mRepository;

    public MentorsListViewModel(@NonNull Application application) {
        super(application);

        mRepository = MentorRepository.getInstance(application.getApplicationContext());
        mMentors = mRepository.mMentors;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllMentors() {
        mRepository.deleteAllMentors();
    }

    public void setCurrentCourse(int currentCourse) {
        mMentors = mRepository.getMentorsByCourse(currentCourse);
    }

}

