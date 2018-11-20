package com.kawika.smart_survey.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.models.CountryCodeModel;
import com.kawika.smart_survey.views.RegistrationActivity;


import java.util.List;

public class CountryCodesAdapter extends RecyclerView.Adapter<CountryCodesAdapter.ViewHolder> {

    private Dialog dialog;
    private List<CountryCodeModel.DataBean> countryCodeDataList;
    private RegistrationActivity context;



    public CountryCodesAdapter(List<CountryCodeModel.DataBean> countryCodeDataList, RegistrationActivity context, Dialog dialog) {
        this.countryCodeDataList = countryCodeDataList;
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_code_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CountryCodesAdapter.ViewHolder holder, final int position) {

        holder.countryCodeTextView.setText("+"+countryCodeDataList.get(position).getPhonecode());
        holder.countryNameTextView.setText(countryCodeDataList.get(position).getCountry_name());
        holder.mainItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.updateSelectedCountryCode("+"+countryCodeDataList.get(position).getPhonecode());
                dialog.cancel();
            }
        });


    }


    @Override
    public int getItemCount() {
        return countryCodeDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView countryCodeTextView, countryNameTextView;
        public LinearLayout mainItemLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            countryCodeTextView = itemView.findViewById(R.id.countryCodeTextView);
            countryNameTextView = itemView.findViewById(R.id.countryNameTextView);
            mainItemLayout = itemView.findViewById(R.id.mainItemLayout);
        }

    }
}
