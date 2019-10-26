package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.TermTracker.database.CourseEntity;
import com.example.TermTracker.database.CourseRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CoursesEditorViewModel extends AndroidViewModel {

    public MutableLiveData<CourseEntity> mLiveCourse =
            new MutableLiveData<>();
    private CourseRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CoursesEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = CourseRepository.getInstance(getApplication());
    }

    public void loadData(final int CourseId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CourseEntity Course = mRepository.getCourseById(CourseId);
                mLiveCourse.postValue(Course);
            }
        });
    }

    public void saveData(String TitleText, String StartDate, String EndDate, String Status, int Term) {
        CourseEntity Course = mLiveCourse.getValue();

        if (Course == null) {
            if (TextUtils.isEmpty(TitleText.trim())) {
                return;
            }
            Course = new CourseEntity(StartDate.trim(), EndDate.trim(), TitleText.trim(), Status.trim(), Term);
        } else {
            Course.setTitle(TitleText.trim());
            Course.setStart(StartDate.trim());
            Course.setEnd(EndDate.trim());
            Course.setStatus(Status.trim());
            Course.setTerm(Term);
        }
        mRepository.insertCourse(Course);
    }

    public void deleteCourse() {
        mRepository.deleteCourse(mLiveCourse.getValue());
    }
}
