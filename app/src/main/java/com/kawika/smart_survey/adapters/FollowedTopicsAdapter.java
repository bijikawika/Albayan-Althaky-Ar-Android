package com.kawika.smart_survey.adapters;

/**
 * Created by senthiljs on 12/02/18.
 */

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.RecyclerClickHighlight;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * Created by akhil on 19/10/17.
 */

public class FollowedTopicsAdapter extends RecyclerView.Adapter<FollowedTopicsAdapter.RecyclerViewHolders> {


    private Context mContext;
    private List<CategoriesSqliteModel> topicsModelList;
    private RecyclerClickHighlight listener = null;
    private boolean notified = false;
    private YoYo.YoYoString rope;


    //constructor
    public FollowedTopicsAdapter(Context context, RecyclerClickHighlight recyclerClick, List<CategoriesSqliteModel> topicModelList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.topicsModelList = topicModelList;
    }

    @Override
    public FollowedTopicsAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_followed_topics_item, null);

        return new FollowedTopicsAdapter.RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final FollowedTopicsAdapter.RecyclerViewHolders holder, final int position) {
        final CategoriesSqliteModel topic = topicsModelList.get(position);
        holder.name.setText(topic.getCategory_name());
        //image display
        Picasso.with(mContext)
                .load(topic.getImage_path())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);

        if (position == 0) {
            if (!notified)
                    listener.onRowClick(holder.mainRelativeLayout, position, topic.getEn_category_id(), topic.getImage_path(), topic.getCategory_name());
            else
                listener.languageChanged(topic.getCategory_name());
        }
        //card click, for select
        holder.mainRelativeLayout.setOnClickListener(view -> {
             rope = YoYo.with(Techniques.ZoomOut)
                    .duration(200)
                     .withListener(new Animator.AnimatorListener() {
                         @Override
                         public void onAnimationStart(Animator animator) {

                         }
                         @Override
                         public void onAnimationEnd(Animator animator) {
                             listener.onRowClick(holder.mainRelativeLayout, position, topic.getEn_category_id(), topic.getImage_path(), topic.getCategory_name());
                             rope = YoYo.with(Techniques.ZoomIn)
                                     .duration(200)
                                     .playOn(holder.mainRelativeLayout);
                         }

                         @Override
                         public void onAnimationCancel(Animator animator) {

                         }

                         @Override
                         public void onAnimationRepeat(Animator animator) {

                         }
                     })
                    .playOn(holder.mainRelativeLayout);


        });
    }


    @Override
    public int getItemCount() {
        return topicsModelList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        //row click
        TextView name;
        CircularImageView photo;
        RelativeLayout mainRelativeLayout;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            mainRelativeLayout = itemView.findViewById(R.id.mainRelativeLayout);
        }
    }

    public void notifyList(List<CategoriesSqliteModel> topicModelList) {
        this.topicsModelList = topicModelList;
        System.out.println("topicModelList = " + topicModelList);
        this.notified = true;
        notifyDataSetChanged();
    }
}