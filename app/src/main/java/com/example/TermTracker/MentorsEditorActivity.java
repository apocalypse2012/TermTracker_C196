package com.example.TermTracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.TermTracker.database.MentorEntity;
import com.example.TermTracker.viewmodel.MentorsEditorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.TermTracker.utilities.Constants.EDITING_MENTOR_KEY;
import static com.example.TermTracker.utilities.Constants.MENTOR_ID_KEY;

public class MentorsEditorActivity extends AppCompatActivity {

    private MentorsEditorViewModel mViewModel;
    private boolean mNewMentor, mEditing;
    Integer currentCourseId;


    @BindView(R.id.mentorNameEditText)
    EditText mentorNameField;
    @BindView(R.id.mentorPhoneEditText)
    EditText mentorPhoneField;
    @BindView(R.id.mentorEmailEditText)
    EditText mentorEmailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_MENTOR_KEY);
        }
        initViewModel();
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(MentorsEditorViewModel.class);

        mViewModel.mLiveMentor.observe(this, new Observer<MentorEntity>() {
            @Override
            public void onChanged(@Nullable MentorEntity mentorEntity) {
                if (mentorEntity != null && !mEditing) {
                    mentorNameField.setText(mentorEntity.getName());
                    mentorPhoneField.setText(mentorEntity.getPhone());
                    mentorEmailField.setText(mentorEntity.getEmail());
                    currentCourseId = mentorEntity.getCourse();
                }
            }
        });

        setTitle(getString(R.string.mentor_name));
        Bundle extras = getIntent().getExtras();
        int mentor_id = extras.getInt(MENTOR_ID_KEY);
        if (mentor_id == 0) {
            mNewMentor = true;
            this.currentCourseId = extras.getInt(COURSE_ID_KEY);
        } else {
            mViewModel.loadData(mentor_id);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewMentor) {
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
            mViewModel.deleteMentor();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveData(mentorNameField.getText().toString(),
                mentorPhoneField.getText().toString(),
                mentorEmailField.getText().toString(),
                currentCourseId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_MENTOR_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
