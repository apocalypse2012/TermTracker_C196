package com.example.TermTracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.TermTracker.database.NoteEntity;
import com.example.TermTracker.ui.NotesAdapter;
import com.example.TermTracker.viewmodel.NotesListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.TermTracker.utilities.Constants.ASSESSMENT_ID_KEY;


public class NotesListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, NotesEditorActivity.class);
        intent.putExtra(ASSESSMENT_ID_KEY, notesParentAssessmentField);
        startActivity(intent);
    }

    private List<NoteEntity> notesData = new ArrayList<>();
    private NotesAdapter mAdapter;
    private NotesListViewModel mViewModel;
    private Integer notesParentAssessmentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            mViewModel = ViewModelProviders.of(this)
                    .get(NotesListViewModel.class);
        } else {
            notesParentAssessmentField = extras.getInt(ASSESSMENT_ID_KEY);
            mViewModel = ViewModelProviders.of(this)
                    .get(NotesListViewModel.class);
            mViewModel.setCurrentAssessment(notesParentAssessmentField);
        }

        final Observer<List<NoteEntity>> notesObserver =
                new Observer<List<NoteEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                        notesData.clear();
                        notesData.addAll(noteEntities);

                        if (mAdapter == null) {
                            mAdapter = new NotesAdapter(notesData,
                                    NotesListActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(NotesListViewModel.class);
        mViewModel.mNotes.observe(this, notesObserver);
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
            deleteAllNotes();
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

    private void deleteAllNotes() {
        mViewModel.deleteAllNotes();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
