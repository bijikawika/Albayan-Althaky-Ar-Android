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
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.TopPlayersSqliteModel;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopPlayersAdapter extends RecyclerView.Adapter<TopPlayersAdapter.RecyclerViewHolders> {


    private Context mContext;
    private List<TopPlayersSqliteModel> topPlayerList;
    private int selectedCategoryId;
    private AppPreferences appPreferences;

    //constructor
    public TopPlayersAdapter(Context context, List<TopPlayersSqliteModel> topPlayerList, int selectedCategoryId) {
        this.mContext = context;
        this.topPlayerList = topPlayerList;
        this.selectedCategoryId = selectedCategoryId;
        appPreferences = AppPreferences.getInstance(mContext, AppConfiguration.SMART_SURVEY_PREFS);
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = View.inflate(mContext, R.layout.top_players_row_item, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        String rank = mContext.getResources().getString(R.string.rank) + " " +
                String.valueOf(topPlayerList.get(position).getRank());
        String playerName = topPlayerList.get(position).getFirstname() + " " + topPlayerList.get(position).getLastname();
        if (topPlayerList.get(position).getUser_id() != Integer.valueOf(appPreferences.getData(AppConfiguration.USER_ID)))
            holder.playerNameTextView.setText(playerName);
        else {
            holder.playerNameTextView.setText(mContext.getResources().getString(R.string.you));
        }
        holder.rankBadgeTextView.setText(rank);
        holder.departmentTextView.setText(topPlayerList.get(position).getCategory_name());
        Picasso.with(mContext)
                .load(topPlayerList.get(position).getImage())
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(holder.topPlayersImageView);

        YoYo.with(Techniques.FlipInX)
                .duration(400)
                .playOn(holder.card_view);
    }

    @Override
    public int getItemCount() {
        return topPlayerList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView playerNameTextView, rankBadgeTextView, departmentTextView;
        ImageView topPlayersImageView;
        CardView card_view;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            rankBadgeTextView = itemView.findViewById(R.id.rankBadgeTextView);
            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            topPlayersImageView = itemView.findViewById(R.id.topPlayersImageView);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }

}