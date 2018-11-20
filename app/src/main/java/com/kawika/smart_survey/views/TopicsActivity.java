package com.kawika.smart_survey.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.GridMainCategoriesAdaptor;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.RecyclerClick;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CreateCategoriesModel;
import com.kawika.smart_survey.models.FollowedTopicsRawModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;

/**
 * Created by senthiljs on 09/02/18.
 */

public class TopicsActivity extends BaseActivity implements RecyclerClick, View.OnClickListener {

    private GridMainCategoriesAdaptor rcAdapter;
    private CategoriesTableQueries followedTopicsTableQueries;
    private AppPreferences appPreferences;
    private TextView doneTextView;
    private RecyclerView topicsRecyclerView;
    private RecyclerClick recyclerClick;

    @Override
    int getContentViewId() {
        return R.layout.catalog_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_topics;
    }

    @Override
    MyScoreActivityBinding getMyScoreActivityBinding() {
        return null;

    }
    @Override
    HomeActivityBinding getHomeBinding() {
        return null;
    }

    @Override
    NotificationActivityBinding getNotificationBinding() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPreferences = AppPreferences.getInstance(TopicsActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        recyclerClick = this;

        initiateToolBar();

        showTopics();

        doneTextView.setOnClickListener(this);

    }


    @Override
    public void onRowClick(int position, int count) {
        PlaySound.buttonClickSound(this, R.raw.button_click_sound);
        List<CategoriesSqliteModel> followedTopics = followedTopicsTableQueries.getFollowedTopics();
        List<Integer> followedTopicsList = rcAdapter.getFollowedTopicsIdLength();

        boolean result = true;
        if (followedTopics.size() == followedTopicsList.size()) {
            for (CategoriesSqliteModel categoriesSqliteModel : followedTopics) {
                if (!followedTopicsList.contains(categoriesSqliteModel.getEn_category_id())) {
                    result = false;
                    break;
                }
            }
        } else
            result = false;

        if (!result && count > 2) {
            doneTextView.setVisibility(View.VISIBLE);
            doneTextView.setAlpha(1f);
            doneTextView.setEnabled(true);
            doneTextView.setText(R.string.update);
        } else if(result){
            doneTextView.setVisibility(View.GONE);
        }
        else {
            doneTextView.setVisibility(View.VISIBLE);
            doneTextView.setText(R.string.select_topics);
            doneTextView.setAlpha(0.75f);
            doneTextView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.doneTextView) {

            PlaySound.buttonClickSound(this, R.raw.button_click_sound);

            List<CategoriesSqliteModel> selectedTopicsList = rcAdapter.getFollowedTopicsList();

            createFollowedTopics(selectedTopicsList);

        }
    }

    private void createFollowedTopics(List<CategoriesSqliteModel> selectedTopicsList) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<CreateCategoriesModel> categoriesCall = retrofitService.createFollowedTopics(appPreferences.getData(AppConfiguration.MY_TOKEN),
                jsonForFollowedTopics(selectedTopicsList));
        categoriesCall.enqueue(new Callback<CreateCategoriesModel>() {
            @Override
            public void onResponse(Call<CreateCategoriesModel> call, Response<CreateCategoriesModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        categoriesTableQueries = categoriesTableQueries.sharedInstance(TopicsActivity.this);
                        categoriesTableQueries.updateSelectedStatus(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            final CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
                            categoriesSqliteModel.setEn_category_id(response.body().getData().get(i).getEn_category_id());
                            categoriesSqliteModel.setCategory_color(response.body().getData().get(i).getCategory_color());
                            categoriesSqliteModel.setCategory_name(response.body().getData().get(i).getCategory_name());
                            categoriesSqliteModel.setCrown(response.body().getData().get(i).getCrown());
                            categoriesSqliteModel.setCrown_image(response.body().getData().get(i).getCrown_image());
                            categoriesSqliteModel.setLevel_count(response.body().getData().get(i).getLevel_count());
                            categoriesSqliteModel.setIs_followed(1);
                            categoriesSqliteModel.setImage_path(response.body().getData().get(i).getImage_path());

                            if (!categoriesTableQueries.updateToFollowedTopic(categoriesSqliteModel))
                                Toast.makeText(TopicsActivity.this, "Something went wrong. Please restart .", Toast.LENGTH_LONG).show();

                        }
                        startActivity(new Intent(TopicsActivity.this, HomeActivity.class));
                    } else {
                        AlertDialogCustom.commonAlertDialog(TopicsActivity.this, response.body().getMessage());

                    }
                }

                startActivity(new Intent(TopicsActivity.this, HomeActivity.class));

            }

            @Override
            public void onFailure(Call<CreateCategoriesModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(TopicsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    public FollowedTopicsRawModel jsonForFollowedTopics(List<CategoriesSqliteModel> selectedTopicsList) {
        try {
            List<FollowedTopicsRawModel.FollowedTopicsBean> followedTopicsBeanList = new ArrayList<>();
            for (CategoriesSqliteModel dataBean : selectedTopicsList) {
                FollowedTopicsRawModel.FollowedTopicsBean followedTopicsBean = new FollowedTopicsRawModel.FollowedTopicsBean();
                followedTopicsBean.setCategory_id(dataBean.getEn_category_id());
                followedTopicsBeanList.add(followedTopicsBean);
            }

            FollowedTopicsRawModel followedTopicsRawModel = new FollowedTopicsRawModel();
            followedTopicsRawModel.setUser_id(appPreferences.getData(AppConfiguration.USER_ID));
            followedTopicsRawModel.setFollowed_topics(followedTopicsBeanList);
            System.out.println("response.body() " + new Gson().toJson(followedTopicsRawModel).toString());

            return followedTopicsRawModel;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void showDialogForTopicDetails() {
        final Dialog dialog = new Dialog(TopicsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.topics_detail_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        ImageView cancelPopupImageView = dialog.findViewById(R.id.cancelPopupImageView);
        cancelPopupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void initiateToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        //back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //widget declaration
        topicsRecyclerView = findViewById(R.id.my_recycler_view);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        doneTextView = findViewById(R.id.doneTextView);
        doneTextView.setText(getString(R.string.update));
        toolbar_title.setText(getResources().getText(R.string.browse_topics));

    }

    private void showTopics() {
        followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(TopicsActivity.this);
        List<CategoriesSqliteModel> allTopics = followedTopicsTableQueries.getAllTopics();

        GridLayoutManager lLayout = new GridLayoutManager(TopicsActivity.this, 3);
        rcAdapter = new GridMainCategoriesAdaptor(TopicsActivity.this, recyclerClick, allTopics);

        topicsRecyclerView.setHasFixedSize(true);
        topicsRecyclerView.setLayoutManager(lLayout);
        topicsRecyclerView.setAdapter(rcAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}