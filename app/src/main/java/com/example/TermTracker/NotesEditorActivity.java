package com.example.TermTracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.TermTracker.database.NoteEntity;
import com.example.TermTracker.viewmodel.NotesEditorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.TermTracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.TermTracker.utilities.Constants.EDITING_NOTE_KEY;
import static com.example.TermTracker.utilities.Constants.NOTE_ID_KEY;

public class NotesEditorActivity extends AppCompatActivity {

    private NotesEditorViewModel mViewModel;
    private boolean mNewNote, mEditing;
    Integer currentAssessmentId;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String shareString = "Course Notes: " + ": " + "\r\n" + mTextView.getText().toString();
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Notes");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, "Share Notes"));
    }

    @BindView(R.id.note_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_NOTE_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(NotesEditorViewModel.class);

        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                if (noteEntity != null && !mEditing) {
                    mTextView.setText(noteEntity.getText());
                    currentAssessmentId = noteEntity.getAssessment();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        int noteId = extras.getInt(NOTE_ID_KEY);
        if (noteId == 0) {
            setTitle(getString(R.string.new_note));
            mNewNote = true;
            this.currentAssessmentId = extras.getInt(ASSESSMENT_ID_KEY);
        } else {
            setTitle(getString(R.string.edit_note));
            mViewModel.loadData(noteId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewNote) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            mViewModel.deleteNote();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveNote(mTextView.getText().toString(),
                currentAssessmentId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_NOTE_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
