package com.kawika.smart_survey.adapters;

/**
 * Created by senthiljs on 12/02/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * Created by akhil on 19/10/17.
 */

public class LatestAchivementsAdapter extends RecyclerView.Adapter<LatestAchivementsAdapter.RecyclerViewHolders> {


    private Context mContext;
    private List<CategoriesSqliteModel> topicsModelList;
    private boolean notified;


    //constructor
    public LatestAchivementsAdapter(Context context, List<CategoriesSqliteModel> topicModelList) {
        this.mContext = context;
        this.topicsModelList = topicModelList;
    }

    @Override
    public LatestAchivementsAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_followed_topics_item, null);

        return new LatestAchivementsAdapter.RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final LatestAchivementsAdapter.RecyclerViewHolders holder, final int position) {
        final CategoriesSqliteModel topic = topicsModelList.get(position);
        holder.name.setText(topic.getCategory_name());
        Picasso.with(mContext)
                .load(topic.getImage_path())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);
    }


    @Override
    public int getItemCount() {
        return topicsModelList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        //row click
        TextView name;
        ImageView photo;
        RelativeLayout mainRelativeLayout;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            mainRelativeLayout = itemView.findViewById(R.id.mainRelativeLayout);
        }
    }
}