package com.kawika.smart_survey.views;
/*
 * Created by akhil on 21/02/18.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.AvatarDisplayAdaptor;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.RecyclerClickAvatar;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.AvatarModel;
import com.kawika.smart_survey.models.Categories;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.utils.CustomProgress;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarListActivity extends AppCompatActivity implements RecyclerClickAvatar {
    private RecyclerView recyclerView;
    private RecyclerClickAvatar recyclerClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_catalog_layout);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        //back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        //widget declaration
        recyclerView = findViewById(R.id.my_recycler_view);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        //declaration
        recyclerClick = this;
        //title
        toolbar_title.setText(getResources().getString(R.string.select_a_avatar));

        //catalog fetcher
        avatarFetchService();
    }

    private void avatarFetchService() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        //progress dialog custom method
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        // Web service contact
        // #library : Retrofit
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        //call object
        Call<AvatarModel> categoriesCall = retrofitService.getAvatarList();

        //method
        categoriesCall.enqueue(new Callback<AvatarModel>() {
            @Override
            public void onResponse(Call<AvatarModel> call, Response<AvatarModel> response) {
                //progress bar dismiss
                progressDialog.dismiss();
                //web call success ,200
                if (response.code() == 200) {
                    //Api response success ,6000
                    if (response.body().getStatus() == 6000) {
                        //data refresh
                        listDataRefresh(response.body().getData());
                    } else {
                        //fail alert
                        AlertDialogCustom.commonAlertDialog(AvatarListActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<AvatarModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                //progress bar dismiss
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(AvatarListActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    /**
     * list view data refresh
     *
     * @param data list
     */
    private void listDataRefresh(List<AvatarModel.DataBean> data) {
        GridLayoutManager lLayout = new GridLayoutManager(this, 4);
        AvatarDisplayAdaptor rcAdapter = new AvatarDisplayAdaptor(this, recyclerClick, data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(rcAdapter);
    }

    /**
     * recycler item row click
     *
     * @param position selected position
     * @param data     selected data
     */
    @Override
    public void onRowClick(int position, String data) {
        //success
        Intent returnIntent = new Intent();
        returnIntent.putExtra(AppConfiguration.DATA_URL, data);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * cancel the event
     */
    private void activityResultFail() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * home click
     *
     * @param item menu
     * @return option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //fail
            activityResultFail();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * back press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //fail
        activityResultFail();
    }
}
