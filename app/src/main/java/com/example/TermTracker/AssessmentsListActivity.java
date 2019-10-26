package com.example.TermTracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.TermTracker.database.AssessmentEntity;
import com.example.TermTracker.ui.AssessmentsAdapter;
import com.example.TermTracker.viewmodel.AssessmentsListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.TermTracker.utilities.Constants.COURSE_ID_KEY;

public class AssessmentsListActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
            Intent intent = new Intent(this, AssessmentsEditorActivity.class);
            intent.putExtra(COURSE_ID_KEY, assessmentParentCourseField);
            startActivity(intent);
    }

    private List<AssessmentEntity> AssessmentsData = new ArrayList<>();
    private AssessmentsAdapter mAdapter;
    private AssessmentsListViewModel mViewModel;
    private Integer assessmentParentCourseField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
        fab = findViewById(R.id.fab);
        if (assessmentParentCourseField == null) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    private void initViewModel() {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            mViewModel = ViewModelProviders.of(this)
                    .get(AssessmentsListViewModel.class);
        } else {
            assessmentParentCourseField = extras.getInt(COURSE_ID_KEY);
            mViewModel = ViewModelProviders.of(this)
                    .get(AssessmentsListViewModel.class);
            mViewModel.setCurrentCourse(assessmentParentCourseField);
        }


        final Observer<List<AssessmentEntity>> AssessmentsObserver =
                new Observer<List<AssessmentEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<AssessmentEntity> AssessmentEntities) {
                        AssessmentsData.clear();
                        AssessmentsData.addAll(AssessmentEntities);

                        if (mAdapter == null) {
                            mAdapter = new AssessmentsAdapter(AssessmentsData,
                                    AssessmentsListActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(AssessmentsListViewModel.class);
        mViewModel.mAssessments.observe(this, AssessmentsObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem alertItem = menu.findItem(R.id.action_test_alert);
        alertItem.setVisible(false);
        MenuItem item = menu.findItem(R.id.action_add_sample_data);
        if (assessmentParentCourseField == null) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllAssessments();
            return true;
        } else if (id == R.id.action_go_home) {
            goToHome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToHome() {
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void deleteAllAssessments() {
        mViewModel.deleteAllAssessments();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
