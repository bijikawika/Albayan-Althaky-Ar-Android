package com.kawika.smart_survey.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.callbacks.OptionClickListener;
import com.kawika.smart_survey.callbacks.RecyclerClickHighlight;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.QuestionCountModel;
import com.kawika.smart_survey.views.QuizActivity;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.RecyclerViewHolders> {

    private List<CurrentlyPlayingAnswersSqliteModel> currentlyPlayingAnswersSqliteModelArrayList;
    private OptionClickListener optionClickListener = null;
    private Context mContext;
    private boolean enableClick = true;

    public OptionsAdapter(Context mContext, OptionClickListener optionClickListener, List<CurrentlyPlayingAnswersSqliteModel> currentlyPlayingAnswersSqliteModelArrayList) {
        this.mContext = mContext;
        this.optionClickListener = optionClickListener;
        this.currentlyPlayingAnswersSqliteModelArrayList = currentlyPlayingAnswersSqliteModelArrayList;
    }

    @Override
    public OptionsAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.options_row_item, parent, false);

        return new OptionsAdapter.RecyclerViewHolders(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        invisibleViews(holder);

        if (currentlyPlayingAnswersSqliteModelArrayList.get(position).getAnswer() != null)
            holder.optionTextView.setText(currentlyPlayingAnswersSqliteModelArrayList.get(position).getAnswer());

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.optionFrameLayout.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInDown)
                        .duration(700)
                        .playOn(holder.optionFrameLayout);
            }
        }, 300);

        holder.optionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enableClick) {
                    holder.throwView.setVisibility(View.VISIBLE);
                    optionClickListener.onOptionClick(position, holder.throwView, holder.optionView, holder.validRelativeLayout, holder.invalidRelativeLayout, holder.optionTextView);
                    enableClick = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return currentlyPlayingAnswersSqliteModelArrayList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder{

        //row click
        TextView optionTextView;
        View optionView, throwView;
        RelativeLayout validRelativeLayout, invalidRelativeLayout;
        FrameLayout optionFrameLayout;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            optionTextView = itemView.findViewById(R.id.optionTextView);
            optionView = itemView.findViewById(R.id.optionView);
            validRelativeLayout = itemView.findViewById(R.id.validRelativeLayout);
            invalidRelativeLayout = itemView.findViewById(R.id.invalidRelativeLayout);
            optionFrameLayout = itemView.findViewById(R.id.optionFrameLayout);
            throwView = itemView.findViewById(R.id.throwView);

        }

    }

    private void invisibleViews(RecyclerViewHolders holder){
        holder.optionFrameLayout.setVisibility(View.INVISIBLE);
        holder.validRelativeLayout.setVisibility(View.INVISIBLE);
        holder.invalidRelativeLayout.setVisibility(View.INVISIBLE);
    }

    public void enableClick() {
        this.enableClick = true;
    }
}
