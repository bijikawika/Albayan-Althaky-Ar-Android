package com.kawika.smart_survey.adapters;

/*
 * Created by akhil on 19/10/17.
 */

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.RecyclerClick;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridMainCategoriesAdaptor extends RecyclerView.Adapter<GridMainCategoriesAdaptor.RecyclerViewHolders> {


    private Context mContext;
    private RecyclerClick listener = null;
    private List<CategoriesSqliteModel> allTopicsList;
    private List<CategoriesSqliteModel> followedTopicsList = new ArrayList<>();
    private List<Integer> followedTopicsId = new ArrayList<>();

    //constructor
    public GridMainCategoriesAdaptor(Context context, RecyclerClick recyclerClick, List<CategoriesSqliteModel> allTopicsList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.allTopicsList = allTopicsList;
    }

    @Override
    public GridMainCategoriesAdaptor.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = View.inflate(mContext, R.layout.catalog_item_grid, null);
        return new GridMainCategoriesAdaptor.RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final GridMainCategoriesAdaptor.RecyclerViewHolders holder, final int position) {
        //single model
        final CategoriesSqliteModel categories = allTopicsList.get(position);

        if (categories.getIs_followed() == 1) {
            followedTopicsList.add(allTopicsList.get(position));
            followedTopicsId.add(allTopicsList.get(position).getEn_category_id());
            holder.tickFrame.setVisibility(View.VISIBLE);
        } else {
            holder.tickFrame.setVisibility(View.INVISIBLE);
        }


        holder.name.setText(categories.getCategory_name());
        //image display
        Picasso.with(mContext)
                .load(categories.getImage_path())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);

        holder.tickFrame.setOnClickListener(view -> {
            if (listener != null) {
                removeTopic(allTopicsList.get(position));
                listener.onRowClick(position, followedTopicsList.size());

                YoYo.with(Techniques.ZoomOut)
                        .duration(100)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                holder.tickFrame.setVisibility(View.INVISIBLE);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .playOn(holder.tickFrame);

            }
        });

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (listener != null) {
                addTopic(allTopicsList.get(position));
                listener.onRowClick(position, followedTopicsList.size());
                holder.tickFrame.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.ZoomIn)
                        .duration(100)
                        .playOn(holder.tickFrame);
            }
        });

    }

    /**
     * remove topic from list
     *
     * @param topic model
     */
    private void removeTopic(CategoriesSqliteModel topic) {
        followedTopicsList.remove(topic);
        followedTopicsId.remove(Integer.valueOf(topic.getEn_category_id()));

    }

    /**
     * add topic to temp list
     *
     * @param topic model
     */
    private void addTopic(CategoriesSqliteModel topic) {
        followedTopicsList.add(topic);
        followedTopicsId.add(topic.getEn_category_id());
    }

    /**
     * get temp list
     *
     * @return model
     */


    public List<CategoriesSqliteModel> getFollowedTopicsList() {
        return followedTopicsList;
    }

    public List<Integer> getFollowedTopicsIdLength() {
        return followedTopicsId;
    }


    @Override
    public int getItemCount() {
        return allTopicsList.size();
    }


    class RecyclerViewHolders extends RecyclerView.ViewHolder {
        RelativeLayout mainRelativeLayout;
        TextView name;
        ImageView photo;
        FrameLayout tickFrame;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            mainRelativeLayout = itemView.findViewById(R.id.mainRelativeLayout);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            tickFrame = itemView.findViewById(R.id.tickFrame);
        }
    }
}