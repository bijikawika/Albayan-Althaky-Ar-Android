package com.kawika.smart_survey.adapters;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.models.DepartmentModel;
import com.kawika.smart_survey.views.RegistrationActivity;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    private Dialog dialog;
    private List<DepartmentModel.DataBean> departmentDataList;
    private RegistrationActivity context;



    public DepartmentAdapter(List<DepartmentModel.DataBean> departmentDataList, RegistrationActivity context, Dialog dialog) {
        this.departmentDataList = departmentDataList;
        this.context = context;
        this.dialog = dialog;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DepartmentAdapter.ViewHolder holder, final int position) {

        holder.departmentTextView.setText(departmentDataList.get(position).getDepartment_name());
        holder.mainItemLayout.setOnClickListener(view -> {
            context.updateSelectedDepartment(departmentDataList.get(position).getDepartment_id(), departmentDataList.get(position).getDepartment_name());
            dialog.cancel();
        });


    }


    @Override
    public int getItemCount() {
        return departmentDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView departmentTextView;
        public LinearLayout mainItemLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            mainItemLayout = itemView.findViewById(R.id.mainItemLayout);
        }

    }
}
