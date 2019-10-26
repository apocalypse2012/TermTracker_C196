package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.TermTracker.database.MentorEntity;
import com.example.TermTracker.database.MentorRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorsEditorViewModel extends AndroidViewModel {

    public MutableLiveData<MentorEntity> mLiveMentor =
            new MutableLiveData<>();
    private MentorRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorsEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = MentorRepository.getInstance(getApplication());
    }

    public void loadData(final int MentorId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MentorEntity Mentor = mRepository.getMentorById(MentorId);
                mLiveMentor.postValue(Mentor);
            }
        });
    }

    public void saveData(String name, String phone, String email, int Course) {
        MentorEntity Mentor = mLiveMentor.getValue();

        if (Mentor == null) {
            if (TextUtils.isEmpty(name.trim())) {
                return;
            }
            Mentor = new MentorEntity(name.trim(), phone.trim(), email.trim(), Course);
        } else {
            Mentor.setName(name.trim());
            Mentor.setPhone(phone.trim());
            Mentor.setEmail(email.trim());
            Mentor.setCourse(Course);
        }
        mRepository.insertMentor(Mentor);
    }

    public void deleteMentor() {
        mRepository.deleteMentor(mLiveMentor.getValue());
    }
}
