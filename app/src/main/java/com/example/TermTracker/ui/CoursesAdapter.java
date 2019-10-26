package com.example.TermTracker.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.TermTracker.R;
import com.example.TermTracker.CoursesEditorActivity;
import com.example.TermTracker.database.CourseEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.COURSE_ID_KEY;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private final List<CourseEntity> mCourses;
    private final Context mContext;

    public CoursesAdapter(List<CourseEntity> mCourses, Context mContext) {
        this.mCourses = mCourses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CourseEntity Course = mCourses.get(position);
        holder.mTitleView.setText(Course.getTitle());
        holder.mStartView.setText(Course.getStart());
        holder.mEndView.setText(Course.getEnd());
        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CoursesEditorActivity.class);
                intent.putExtra(COURSE_ID_KEY, Course.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_text)
        TextView mTitleView;
        @BindView(R.id.course_start)
        TextView mStartView;
        @BindView(R.id.course_end)
        TextView mEndView;
        @BindView(R.id.fab)
        FloatingActionButton mFab;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
