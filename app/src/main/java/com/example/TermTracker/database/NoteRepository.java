package com.example.TermTracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.TermTracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteRepository {
    private static NoteRepository ourInstance;

    public LiveData<List<NoteEntity>> mNotes;

    public LiveData<List<NoteEntity>> tNotes;
    public Integer noteAssessment;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static NoteRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new NoteRepository(context);
        }
        return ourInstance;
    }

    private NoteRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mNotes = getAllNotes();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().insertAll(SampleData.getNotes(noteAssessment));
            }
        });
    }

    private LiveData<List<NoteEntity>> getAllNotes() {
        return mDb.noteDao().getAll();
    }

    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().deleteAll();
            }
        });
    }

    public NoteEntity getNoteById(int noteId) {
        return mDb.noteDao().getNoteById(noteId);
    }

    public void insertNote(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().insertNote(note);
            }
        });
    }

    public void deleteNote(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().deleteNote(note);
            }
        });
    }

    public LiveData<List<NoteEntity>> getNotesByAssessment(int noteAssessment) {
        this.noteAssessment = noteAssessment;
        tNotes = mDb.noteDao().getNoteByAssessment(noteAssessment);
        return tNotes;
    }
}
