package com.kawika.smart_survey.adapters;

/*
 * Created by akhil on 07/09/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.models.CategoriesSqliteModel;

import java.util.ArrayList;
import java.util.List;


public class TopicsStatiticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<CategoriesSqliteModel> statisticsCategoriesArrayList = new ArrayList<>();

    public TopicsStatiticsAdapter(Context context, List<CategoriesSqliteModel> statisticsCategoriesArrayList) {
        this.context = context;
        this.statisticsCategoriesArrayList = statisticsCategoriesArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_statitics_item, parent, false);
        MyViewHolder view = new MyViewHolder(itemView);

        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        final MyViewHolder holder = (MyViewHolder) viewholder;
        holder.topicNameTextView.setText(statisticsCategoriesArrayList.get(position).getCategory_name());
        System.out.println("statisticsCategoriesArrayList.get(position).getCategory_name() = " + statisticsCategoriesArrayList.get(position).getCategory_name());
        holder.identifierView.setBackgroundColor(Color.parseColor(statisticsCategoriesArrayList.get(position).getCategory_color()));
    }

    @Override
    public int getItemCount() {
        return statisticsCategoriesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public View identifierView;
        public TextView topicNameTextView;

        public MyViewHolder(View view) {
            super(view);
            identifierView = itemView.findViewById(R.id.identifierView);
            topicNameTextView = itemView.findViewById(R.id.topicNameTextView);
        }


    }
}

