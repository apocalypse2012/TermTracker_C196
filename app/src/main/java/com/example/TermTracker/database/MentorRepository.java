package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.TermTracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorRepository {
    private static MentorRepository ourInstance;

    public LiveData<List<MentorEntity>> mMentors;

    public LiveData<List<MentorEntity>> tMentors;
    public Integer mentorCourse;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static MentorRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MentorRepository(context);
        }
        return ourInstance;
    }

    private MentorRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mMentors = getAllMentors();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.mentorDao().insertAll(SampleData.getMentors(mentorCourse));
            }
        });
    }

    private LiveData<List<MentorEntity>> getAllMentors() {
        return mDb.mentorDao().getAll();
    }

    public void deleteAllMentors() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.mentorDao().deleteAll();
            }
        });
    }

    public MentorEntity getMentorById(int mentorId) {
        return mDb.mentorDao().getMentorById(mentorId);
    }

    public void insertMentor(final MentorEntity mentor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.mentorDao().insertMentor(mentor);
            }
        });
    }

    public void deleteMentor(final MentorEntity mentor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.mentorDao().deleteMentor(mentor);
            }
        });
    }

    public LiveData<List<MentorEntity>> getMentorsByCourse(int mentorCourse) {
        this.mentorCourse = mentorCourse;
        tMentors = mDb.mentorDao().getMentorByCourse(mentorCourse);
        return tMentors;
    }

}
