package com.kawika.smart_survey.adapters;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.QuestionCountModel;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.views.QuizActivity;

import java.util.List;
import java.util.Locale;

public class QuestionsCountAdapter extends RecyclerView.Adapter<QuestionsCountAdapter.MyViewHolder> {

    private List<QuestionCountModel> moviesList;
    private Context mContext;
    private int question_count;
    private AppPreferences appPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView quesCountTextView;
        public View lineView;

        public MyViewHolder(View view) {
            super(view);
            quesCountTextView = view.findViewById(R.id.quesCountTextView);
            lineView = view.findViewById(R.id.lineView);
        }
    }


    public QuestionsCountAdapter(List<QuestionCountModel> moviesList, Context mContext, int question_count) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        this.question_count = question_count;
        appPreferences = AppPreferences.getInstance(mContext, AppConfiguration.SMART_SURVEY_PREFS);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_count_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        QuestionCountModel questionCountModel = moviesList.get(position);

        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            holder.quesCountTextView.setText(questionCountModel.getQuesCount());
        }else{
            holder.quesCountTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(questionCountModel.getQuesCount()));
        }

        if (position == moviesList.size()-1)
            holder.lineView.setVisibility(View.INVISIBLE);
        else
            holder.lineView.setVisibility(View.VISIBLE);

        if (moviesList.get(position).getHighlight() != -1) {
            holder.quesCountTextView.setTextColor(mContext.getResources().getColor(R.color.yellow));
            holder.quesCountTextView.setTypeface(holder.quesCountTextView.getTypeface(), Typeface.BOLD);
            holder.quesCountTextView.setTextSize(27f);

            YoYo.with(Techniques.ZoomIn)
                    .duration(600)
                    .playOn(holder.quesCountTextView);

        } else {
            holder.quesCountTextView.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.quesCountTextView.setTypeface(holder.quesCountTextView.getTypeface(), Typeface.NORMAL);
            holder.quesCountTextView.setTextSize(17f);
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void updateRecyclerView(int positionCount, List<QuestionCountModel> questionCountModelList) {
        this.moviesList = questionCountModelList;
        System.out.println("questionCountModelList.size() = " + questionCountModelList.size());
        questionCountModelList.get(positionCount).setHighlight(1);
        notifyDataSetChanged();
    }

}
