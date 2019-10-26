package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.TermTracker.database.NoteRepository;
import com.example.TermTracker.database.NoteEntity;

import java.util.List;

public class NotesListViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotes;
    private NoteRepository mRepository;

    public NotesListViewModel(@NonNull Application application) {
        super(application);

        mRepository = NoteRepository.getInstance(application.getApplicationContext());
        mNotes = mRepository.mNotes;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllNotes() {
        mRepository.deleteAllNotes();
    }

    public void setCurrentAssessment(int currentAssessment) {
        mNotes = mRepository .getNotesByAssessment(currentAssessment);
    }
}
