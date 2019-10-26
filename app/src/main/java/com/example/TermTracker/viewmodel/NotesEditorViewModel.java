package com.example.TermTracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.TermTracker.database.NoteRepository;
import com.example.TermTracker.database.NoteEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesEditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> mLiveNote =
            new MutableLiveData<>();
    private NoteRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NotesEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = NoteRepository.getInstance(getApplication());
    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = mRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(String noteText, int Assessment) {
        NoteEntity note = mLiveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            note = new NoteEntity(new Date(), noteText.trim(), Assessment);
        } else {
            note.setText(noteText.trim());
            note.setAssessment(Assessment);
        }
        mRepository.insertNote(note);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
