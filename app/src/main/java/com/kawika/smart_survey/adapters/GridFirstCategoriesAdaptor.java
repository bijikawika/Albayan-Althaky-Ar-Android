package com.kawika.smart_survey.adapters;

/*
 * Created by akhil on 19/10/17.
 */
import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.RecyclerClick;
import com.kawika.smart_survey.models.Categories;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridFirstCategoriesAdaptor extends RecyclerView.Adapter<GridFirstCategoriesAdaptor.RecyclerViewHolders> {


    private Context mContext;
    private RecyclerClick listener = null;
    private List<CategoriesSqliteModel> topicsModelList;
    private List<CategoriesSqliteModel> selectedTopicsList = new ArrayList<>();

    //constructor
    public GridFirstCategoriesAdaptor(Context context, RecyclerClick recyclerClick, List<CategoriesSqliteModel> topicModelList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.topicsModelList = topicModelList;
    }

    @Override
    public GridFirstCategoriesAdaptor.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = View.inflate(mContext, R.layout.catalog_item_grid, null);
        return new GridFirstCategoriesAdaptor.RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final GridFirstCategoriesAdaptor.RecyclerViewHolders holder, final int position) {
        //single model
        final CategoriesSqliteModel categories = topicsModelList.get(position);
        //name
        holder.name.setText(categories.getCategory_name());
        System.out.println("categories.getCategory_name() = " + categories.getCategory_name());
        //image display
        Picasso.with(mContext)
                .load(categories.getImage_path())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);

        holder.tickFrame.setVisibility(View.INVISIBLE);
        //card click, for select
        holder.card_view.setOnClickListener(view -> {
            if (listener != null) {
                    //view change
                    holder.tickFrame.setVisibility(View.VISIBLE);
                    //animation
                    YoYo.with(Techniques.ZoomIn)
                            .duration(100)
                            .playOn(holder.tickFrame);
                    //add to list
                    addTopic(categories);
                // click identifier
                listener.onRowClick(position, selectedTopicsList.size());
            }
        });

        //frame click, for deselect
        holder.tickFrame.setOnClickListener(view -> {
            if (listener != null) {
                    //view change
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
                    //remove from list
                    removeTopic(categories);

                // click identifier
                listener.onRowClick(position, selectedTopicsList.size());
            }
        });
    }

    /**
     * remove topic from list
     *
     * @param topic model
     */
    private void removeTopic(CategoriesSqliteModel topic) {
        selectedTopicsList.remove(topic);
    }

    /**
     * add topic to temp list
     *
     * @param topic model
     */
    private void addTopic(CategoriesSqliteModel topic) {
        selectedTopicsList.add(topic);
    }

    /**
     * get temp list
     *
     * @return model
     */
    public List<CategoriesSqliteModel> getSelectedTopicsList() {
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
        FrameLayout tickFrame;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            tickFrame = itemView.findViewById(R.id.tickFrame);
        }
    }
}