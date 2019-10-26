package com.example.TermTracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private static final int TERMS_LIST_REQUEST = 1;
    private static final int COURSES_LIST_REQUEST = 2;
    private static final int ASSESSMENTS_LIST_REQUEST = 3;
    private static final int MENTORS_LIST_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String channel_id = getString(R.string.channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void openTermList(View view) {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivityForResult(intent, TERMS_LIST_REQUEST);
    }

    public void openCourseList(View view) {
        Intent intent = new Intent(this, CoursesListActivity.class);
        startActivityForResult(intent, COURSES_LIST_REQUEST);
    }

    public void openAssessmentList(View view) {
        Intent intent = new Intent(this, AssessmentsListActivity.class);
        startActivityForResult(intent, ASSESSMENTS_LIST_REQUEST);
    }

    public void openMentorList(View view) {
        Intent intent = new Intent(this, MentorsListActivity.class);
        startActivityForResult(intent, MENTORS_LIST_REQUEST);
    }
}