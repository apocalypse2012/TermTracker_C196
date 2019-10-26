package com.example.TermTracker;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.TermTracker.database.AssessmentEntity;
import com.example.TermTracker.database.DateConverter;
import com.example.TermTracker.viewmodel.AssessmentsEditorViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.TermTracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.TermTracker.utilities.Constants.EDITING_ASSESSMENT_KEY;

public class AssessmentsEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private AssessmentsEditorViewModel mViewModel;
    private boolean mNewAssessment, mEditing;
    private int NOTES_LIST_REQUEST = 33;
    private Integer currentAssessmentId;
    Integer currentCourseId;


    @BindView(R.id.assessmentTitleEditText)
    EditText assessmentTitleField;
    @BindView(R.id.assessmentDateEditText)
    EditText assessmentDateField;


    private DatePickerDialog assessmentDateDialog;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_ASSESSMENT_KEY);
        }

        setupDatePickers();
        initViewModel();
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(AssessmentsEditorViewModel.class);

        mViewModel.mLiveAssessment.observe(this, new Observer<AssessmentEntity>() {
            @Override
            public void onChanged(@Nullable AssessmentEntity assessmentEntity) {
                if (assessmentEntity != null && !mEditing) {
                    assessmentTitleField.setText(assessmentEntity.getTitle());
                    assessmentDateField.setText(assessmentEntity.getDate());
                    currentCourseId = assessmentEntity.getCourse();
                    currentAssessmentId = assessmentEntity.getId();
                }
            }
        });

        setTitle(getString(R.string.assessment_name));
        Bundle extras = getIntent().getExtras();
        int assessment_id = extras.getInt(ASSESSMENT_ID_KEY);
        if (assessment_id == 0) {
            mNewAssessment = true;
            this.currentCourseId = extras.getInt(COURSE_ID_KEY);
        } else {
            this.currentAssessmentId = assessment_id;
            mViewModel.loadData(currentAssessmentId);
        }
    }

    private void setupDatePickers() {
        assessmentDateField.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        assessmentDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                assessmentDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        assessmentDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    assessmentDateDialog.show();
                }
            }
        });
    }

    private void _scheduleAlert(int id, String time, String title, String text) {
        long now = DateConverter.nowDate();
        long alertTime = DateConverter.toTimestamp(time);
        if (now <= DateConverter.toTimestamp(time)) {
            NotificationReceiver.scheduleAssessmentAlarm(getApplicationContext(), id, alertTime,
                    text, title + ", occuring at: " + time);
        }
    }

    public void scheduleAlert(View view) {
        String dateText = assessmentDateField.getText().toString();
        String time = "Assessment is today!";
        String title = assessmentTitleField.getText().toString();
        _scheduleAlert(currentAssessmentId, dateText, title, time);
    }

    public void openNotesList(View view) {
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra(ASSESSMENT_ID_KEY, this.currentAssessmentId);
        startActivityForResult(intent, NOTES_LIST_REQUEST);
    }

    @Override
    public void onClick(View view) {
        if (view == assessmentDateField) {
            assessmentDateDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewAssessment) {
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
            mViewModel.deleteAssessment();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveData(assessmentTitleField.getText().toString(),
                assessmentDateField.getText().toString(),
                currentCourseId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_ASSESSMENT_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
