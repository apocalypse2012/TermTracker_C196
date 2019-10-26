package com.example.TermTracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.Toast;

import com.example.TermTracker.database.TermEntity;
import com.example.TermTracker.ui.TermsAdapter;
import com.example.TermTracker.viewmodel.TermsListViewModel;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, TermsEditorActivity.class);
        startActivity(intent);
    }

    private List<TermEntity> TermsData = new ArrayList<>();
    private TermsAdapter mAdapter;
    private TermsListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {

        final Observer<List<TermEntity>> TermsObserver =
                new Observer<List<TermEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<TermEntity> TermEntities) {
                        TermsData.clear();
                        TermsData.addAll(TermEntities);

                        if (mAdapter == null) {
                            mAdapter = new TermsAdapter(TermsData,
                                    TermsListActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(TermsListViewModel.class);
        mViewModel.mTerms.observe(this, TermsObserver);
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
            deleteAllTerms();
            return true;
        } else if (id == R.id.action_go_home) {
            goToHome();
            return true;
        } else if (id == R.id.action_test_alert) {
            createTestAlarm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean createTestAlarm() {
        // Sets alarm for 5 seconds in the future
        Long time = new GregorianCalendar().getTimeInMillis() + 5000;

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Test Alarm");
        intent.putExtra("text", "This is a test alarm.");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT));
        Toast.makeText(this, "An alert was set to fire in 5 seconds.", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void goToHome() {
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void deleteAllTerms() {
        mViewModel.deleteAllTerms();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
