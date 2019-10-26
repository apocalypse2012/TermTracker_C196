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

import com.example.TermTracker.MentorsEditorActivity;
import com.example.TermTracker.R;
import com.example.TermTracker.database.MentorEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.TermTracker.utilities.Constants.MENTOR_ID_KEY;

public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder> {

    private final List<MentorEntity> mMentors;
    private final Context mContext;

    public MentorsAdapter(List<MentorEntity> mMentors, Context mContext) {
        this.mMentors = mMentors;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mentor_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MentorEntity Mentor = mMentors.get(position);
        holder.mTitleView.setText(Mentor.getName());

        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MentorsEditorActivity.class);
                intent.putExtra(MENTOR_ID_KEY, Mentor.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMentors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mentor_text)
        TextView mTitleView;
        @BindView(R.id.fab)
        FloatingActionButton mFab;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
