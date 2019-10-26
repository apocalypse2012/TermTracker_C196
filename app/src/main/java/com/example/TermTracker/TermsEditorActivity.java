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

import com.example.TermTracker.database.DateConverter;
import com.example.TermTracker.database.TermEntity;
import com.example.TermTracker.viewmodel.TermsEditorViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.EDITING_TERM_KEY;
import static com.example.TermTracker.utilities.Constants.TERM_ID_KEY;

public class TermsEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private TermsEditorViewModel mViewModel;
    private boolean mNewTerm, mEditing;
    private int currentTermId;
    private final int COURSES_LIST_REQUEST = 11;


    @BindView(R.id.termTitleEditText)
    EditText termTitleField;
    @BindView(R.id.termStartDateEditText)
    EditText termStartDateField;
    @BindView(R.id.termEndDateEditText)
    EditText termEndDateField;

    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_TERM_KEY);
        }

        setupDatePickers();
        initViewModel();
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(TermsEditorViewModel.class);

        mViewModel.mLiveTerm.observe(this, new Observer<TermEntity>() {
            @Override
            public void onChanged(@Nullable TermEntity termEntity) {
                if (termEntity != null && !mEditing) {
                    termTitleField.setText(termEntity.getTitle());
                    termStartDateField.setText(termEntity.getStart());
                    termEndDateField.setText(termEntity.getEnd());
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.term_name));
            mNewTerm = true;
        } else {
            setTitle(getString(R.string.term_name));
            int termId = extras.getInt(TERM_ID_KEY);
            this.currentTermId = termId;
            mViewModel.loadData(termId);
        }
    }

    private void setupDatePickers() {
        termStartDateField.setOnClickListener(this);
        termEndDateField.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termStartDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termEndDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    termStartDateDialog.show();
                }
            }
        });
    }

    private void _scheduleAlert(int id, String time, String title, String text) {
        long now = DateConverter.nowDate();
        long alertTime = DateConverter.toTimestamp(time);
        if (now <= DateConverter.toTimestamp(time)) {
            NotificationReceiver.scheduleTermAlarm(getApplicationContext(), id, alertTime,
                    text, title + ", occuring at: " + time);
        }
    }

    public void scheduleAlert(View view) {
        String dateText = termStartDateField.getText().toString();
        String text = "Term starts today!";
        String title = termTitleField.getText().toString();
        _scheduleAlert(currentTermId, dateText, title, text);

        dateText = termEndDateField.getText().toString();
        text = "Term ends today!";
        _scheduleAlert(currentTermId, dateText, title, text);
    }

    public void openCourseList(View view) {
        Intent intent = new Intent(this, CoursesListActivity.class);
        intent.putExtra(TERM_ID_KEY, this.currentTermId);
        startActivityForResult(intent, COURSES_LIST_REQUEST);
    }

    @Override
    public void onClick(View view) {
        if (view == termStartDateField) {
            termStartDateDialog.show();
        }
        if (view == termEndDateField) {
            termEndDateDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewTerm) {
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
            mViewModel.deleteTerm();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveData(termTitleField.getText().toString(),
                termStartDateField.getText().toString(),
                termEndDateField.getText().toString());
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_TERM_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
