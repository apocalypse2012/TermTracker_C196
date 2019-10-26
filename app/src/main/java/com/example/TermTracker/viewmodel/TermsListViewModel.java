package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.TermTracker.database.TermRepository;
import com.example.TermTracker.database.TermEntity;

import java.util.List;

public class TermsListViewModel extends AndroidViewModel {

    public LiveData<List<TermEntity>> mTerms;
    private TermRepository mRepository;

    public TermsListViewModel(@NonNull Application application) {
        super(application);

        mRepository = TermRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllTerms() {
        mRepository.deleteAllTerms();
    }
}
