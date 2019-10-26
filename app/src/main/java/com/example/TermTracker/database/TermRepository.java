package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.TermTracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermRepository {
    private static TermRepository ourInstance;

    public LiveData<List<TermEntity>> mTerms;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static TermRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TermRepository(context);
        }
        return ourInstance;
    }

    private TermRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTerms = getAllTerms();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insertAll(SampleData.getTerms());
            }
        });
    }

    private LiveData<List<TermEntity>> getAllTerms() {
        return mDb.termDao().getAll();
    }

    public void deleteAllTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.courseDao().getCountByAnyTerm();
                if (numChildren == 0) {
                    mDb.termDao().deleteAll();
                }
            }
        });
    }

    public TermEntity getTermById(int termId) {
        return mDb.termDao().getTermById(termId);
    }

    public void insertTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insertTerm(term);
            }
        });
    }

    public void deleteTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int numChildren = mDb.courseDao().getCountByTerm(term.getId());
                if (numChildren == 0) {
                    mDb.termDao().deleteTerm(term);
                }
            }
        });
    }
}
