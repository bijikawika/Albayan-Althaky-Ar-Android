package com.kawika.smart_survey.adapters;
/*
 * Created by akhil on 21/02/18.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.RecyclerClickAvatar;
import com.kawika.smart_survey.models.AvatarModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AvatarDisplayAdaptor extends RecyclerView.Adapter<AvatarDisplayAdaptor.RecyclerViewHolders> {


    private Context mContext;
    private RecyclerClickAvatar listener = null;
    private List<AvatarModel.DataBean> avatarModelList;

    //constructor
    public AvatarDisplayAdaptor(Context context, RecyclerClickAvatar recyclerClick,
                                List<AvatarModel.DataBean> avatarList) {
        this.mContext = context;
        this.listener = recyclerClick;
        this.avatarModelList = avatarList;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = View.inflate(mContext, R.layout.avatar_select_item, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        //single model
        final AvatarModel.DataBean avatarList = avatarModelList.get(position);
        //image display
        Picasso.with(mContext)
                .load(avatarList.getImage_path())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.photo);

        //card click, for select
        holder.card_view.setOnClickListener(view -> {
            if (listener != null) {
                // click identifier
                listener.onRowClick(position, avatarList.getImage_path());
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarModelList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        //row click
        CardView card_view;
        ImageView photo;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            photo = itemView.findViewById(R.id.photo);

        }
    }
}