package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.TermTracker.database.AssessmentEntity;
import com.example.TermTracker.database.AssessmentRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentsEditorViewModel extends AndroidViewModel {

    public MutableLiveData<AssessmentEntity> mLiveAssessment =
            new MutableLiveData<>();
    private AssessmentRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentsEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AssessmentRepository.getInstance(getApplication());
    }

    public void loadData(final int AssessmentId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AssessmentEntity Assessment = mRepository.getAssessmentById(AssessmentId);
                mLiveAssessment.postValue(Assessment);
            }
        });
    }

    public void saveData(String TitleText, String Date, int Course) {
        AssessmentEntity Assessment = mLiveAssessment.getValue();

        if (Assessment == null) {
            if (TextUtils.isEmpty(TitleText.trim())) {
                return;
            }
            Assessment = new AssessmentEntity(Date.trim(), TitleText.trim(), Course);
        } else {
            Assessment.setTitle(TitleText.trim());
            Assessment.setDate(Date.trim());
            Assessment.setCourse(Course);
        }
        mRepository.insertAssessment(Assessment);
    }

    public void deleteAssessment() {
        mRepository.deleteAssessment(mLiveAssessment.getValue());
    }
}
