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

import com.example.TermTracker.database.CourseEntity;
import com.example.TermTracker.database.DateConverter;
import com.example.TermTracker.viewmodel.CoursesEditorViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.TermTracker.utilities.Constants.EDITING_COURSE_KEY;
import static com.example.TermTracker.utilities.Constants.TERM_ID_KEY;

public class CoursesEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private CoursesEditorViewModel mViewModel;
    private boolean mNewCourse, mEditing;
    private int ASSESSMENTS_LIST_REQUEST = 22;
    private int MENTORS_LIST_REQUEST = 23;
    private Integer currentCourseId;
    Integer currentTermId;


    @BindView(R.id.courseTitleEditText)
    EditText courseTitleField;
    @BindView(R.id.courseStartDateEditText)
    EditText courseStartDateField;
    @BindView(R.id.courseEndDateEditText)
    EditText courseEndDateField;
    @BindView(R.id.courseStatusEditText)
    EditText courseStatusField;

    private DatePickerDialog courseStartDateDialog;
    private DatePickerDialog courseEndDateDialog;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_COURSE_KEY);
        }

        setupDatePickers();
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(CoursesEditorViewModel.class);

        mViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(@Nullable CourseEntity courseEntity) {
                if (courseEntity != null && !mEditing) {
                    courseTitleField.setText(courseEntity.getTitle());
                    courseStartDateField.setText(courseEntity.getStart());
                    courseEndDateField.setText(courseEntity.getEnd());
                    courseStatusField.setText(courseEntity.getStatus());
                    currentTermId = courseEntity.getTerm();
                    currentCourseId = courseEntity.getId();
                }
            }
        });

        setTitle(getString(R.string.course_name));
        Bundle extras = getIntent().getExtras();
        int course_id = extras.getInt(COURSE_ID_KEY);
        if (course_id == 0) {
            mNewCourse = true;
            this.currentTermId = extras.getInt(TERM_ID_KEY);
        } else {
            this.currentCourseId = course_id;
            mViewModel.loadData(currentCourseId);
        }
    }

    private void setupDatePickers() {
        courseStartDateField.setOnClickListener(this);
        courseEndDateField.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        courseStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                courseStartDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        courseEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                courseEndDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        courseStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    courseStartDateDialog.show();
                }
            }
        });
    }


    public void openAssessmentList(View view) {
        Intent intent = new Intent(this, AssessmentsListActivity.class);
        intent.putExtra(COURSE_ID_KEY, this.currentCourseId);
        startActivityForResult(intent, ASSESSMENTS_LIST_REQUEST);
    }

    public void openMentorList(View view) {
        Intent intent = new Intent(this, MentorsListActivity.class);
        intent.putExtra(COURSE_ID_KEY, this.currentCourseId);
        startActivityForResult(intent, MENTORS_LIST_REQUEST);
    }

    private void _scheduleAlert(int id, String time, String title, String text) {
        long now = DateConverter.nowDate();
        long alertTime = DateConverter.toTimestamp(time);
        if (now <= DateConverter.toTimestamp(time)) {
            NotificationReceiver.scheduleCourseAlarm(getApplicationContext(), id, alertTime,
                    text, title + ", occuring at: " + time);
        }
    }

    public void scheduleAlert(View view) {
        String dateText = courseStartDateField.getText().toString();
        String text = "Course starts today!";
        String title = courseTitleField.getText().toString();
        _scheduleAlert(currentCourseId, dateText, title, text);

        dateText = courseEndDateField.getText().toString();
        text = "Course ends today!";
        _scheduleAlert(currentCourseId, dateText, title, text);
    }

    @Override
    public void onClick(View view) {
        if (view == courseStartDateField) {
            courseStartDateDialog.show();
        }
        if (view == courseEndDateField) {
            courseEndDateDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewCourse) {
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
            mViewModel.deleteCourse();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveData(courseTitleField.getText().toString(),
                courseStartDateField.getText().toString(),
                courseEndDateField.getText().toString(),
                courseStatusField.getText().toString(),
                currentTermId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_COURSE_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
