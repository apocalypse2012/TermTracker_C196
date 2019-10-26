package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.TermTracker.database.TermEntity;
import com.example.TermTracker.database.TermRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermsEditorViewModel extends AndroidViewModel {

    public MutableLiveData<TermEntity> mLiveTerm =
            new MutableLiveData<>();
    private TermRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermsEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = TermRepository.getInstance(getApplication());
    }

    public void loadData(final int TermId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TermEntity Term = mRepository.getTermById(TermId);
                mLiveTerm.postValue(Term);
            }
        });
    }

    public void saveData(String TitleText, String StartDate, String EndDate) {
        TermEntity Term = mLiveTerm.getValue();

        if (Term == null) {
            if (TextUtils.isEmpty(TitleText.trim())) {
                return;
            }
            Term = new TermEntity(StartDate.trim(), EndDate.trim(), TitleText.trim());
        } else {
            Term.setTitle(TitleText.trim());
            Term.setStart(StartDate.trim());
            Term.setEnd(EndDate.trim());
        }
        mRepository.insertTerm(Term);
    }

    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerm.getValue());
    }
}
