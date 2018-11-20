package com.kawika.smart_survey.adapters;

/**
 * Created by senthiljs on 12/02/18.
 */

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.RecyclerClick;
import com.kawika.smart_survey.models.Catalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by akhil on 19/10/17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.RecyclerViewHolders> {


    private Context mContext;
    private RecyclerClick listener = null;
    private List<Catalog> topicsModelList;
    private List<Catalog> selectedTopicsList = new ArrayList<>();
    private boolean isTopicFragment = false;

    //constructor
    public SettingsAdapter(Context context, RecyclerClick recyclerClick, List<Catalog> topicModelList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.topicsModelList = topicModelList;
    }

    public SettingsAdapter(Context context, RecyclerClick recyclerClick, boolean isTopicFragment, List<Catalog> topicModelList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.isTopicFragment = isTopicFragment;
        this.topicsModelList = topicModelList;
    }

    @Override
    public SettingsAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsAdapter.RecyclerViewHolders(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_item, null));
    }

    @Override
    public void onBindViewHolder(final SettingsAdapter.RecyclerViewHolders holder, final int position) {
        //single model
        final Catalog topic = topicsModelList.get(position);
        //name
        holder.name.setText(topic.getName());
        //image display
        Picasso.with(mContext)
                .load(topic.getImagePath())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);

        //card click, for select
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (!isTopicFragment) {
                        //view change
                        holder.tickImageLayout.setVisibility(View.VISIBLE);
                        //animation
                        viewAnimation(holder.card_view);
                        //add to list
                        addTopic(topic);
                    }
                    // click identifier
                    listener.onRowClick(position, selectedTopicsList.size());
                }
            }
        });

        //frame click, for deselect
        holder.tickImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (!isTopicFragment) {
                        //view change
                        holder.tickImageLayout.setVisibility(View.GONE);
                        //animation
//                unSelectAnimation(holder.tickImageLayout);
                        //remove from list
                        removeTopic(topic);
                    }
                    // click identifier
                    listener.onRowClick(position, selectedTopicsList.size());
                }
            }
        });
    }

    /**
     * animation for view click
     *
     * @param view view
     */
    private void viewAnimation(View view) {
        YoYo.with(Techniques.ZoomIn)
                .duration(100)
                .playOn(view);
    }

    private void unSelectAnimation(final View view) {
        YoYo.with(Techniques.ZoomOut)
                .duration(100)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(view);
    }

    /**
     * remove topic from list
     *
     * @param topic model
     */
    private void removeTopic(Catalog topic) {
        selectedTopicsList.remove(topic);
    }

    /**
     * add topic to temp list
     *
     * @param topic model
     */
    private void addTopic(Catalog topic) {
        selectedTopicsList.add(topic);
    }

    /**
     * get temp list
     *
     * @return model
     */
    public List<Catalog> getSelectedTopicsList() {
        return selectedTopicsList;
    }

    @Override
    public int getItemCount() {
        return topicsModelList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        //row click
        CardView card_view;
        TextView name;
        ImageView photo;
        FrameLayout tickImageLayout;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            tickImageLayout = itemView.findViewById(R.id.tickImage);
        }
    }
}