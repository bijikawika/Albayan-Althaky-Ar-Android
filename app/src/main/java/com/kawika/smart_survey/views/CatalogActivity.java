package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.GridFirstCategoriesAdaptor;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.RecyclerClick;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CreateCategoriesModel;
import com.kawika.smart_survey.models.FollowedTopicsRawModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.LocaleManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;

public class CatalogActivity extends BaseAppCompatActivity implements RecyclerClick, View.OnClickListener {

    private GridFirstCategoriesAdaptor rcAdapter;
    private RecyclerView topicsRecyclerView;
    private RecyclerClick recyclerClick;
    private AppPreferences appPreferences;
    private TextView doneTextView;
    private CategoriesTableQueries followedTopicsTableQueries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_catalog_layout);

        appPreferences = AppPreferences.getInstance(CatalogActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        toolbarInitiate();

        recyclerClick = this;
        doneTextView.setOnClickListener(this);

        showTopics();

    }

    private void showTopics(){
        followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(CatalogActivity.this);
        List<CategoriesSqliteModel> allTopics = followedTopicsTableQueries.getAllTopics();

        GridLayoutManager lLayout = new GridLayoutManager(CatalogActivity.this, 3);
        rcAdapter = new GridFirstCategoriesAdaptor(CatalogActivity.this, recyclerClick, allTopics);

        topicsRecyclerView.setHasFixedSize(true);
        topicsRecyclerView.setLayoutManager(lLayout);
        topicsRecyclerView.setAdapter(rcAdapter);
    }


    @Override
    public void onRowClick(int position, int count) {
        if (count > 2) {
            doneTextView.setVisibility(View.VISIBLE);
        } else {
            doneTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.doneTextView) {
            List<CategoriesSqliteModel> selectedTopicsList = rcAdapter.getSelectedTopicsList();

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
                        categoriesTableQueries = categoriesTableQueries.sharedInstance(CatalogActivity.this);

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            final CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
                            categoriesSqliteModel.setEn_category_id(response.body().getData().get(i).getEn_category_id());
                            categoriesSqliteModel.setCategory_color(response.body().getData().get(i).getCategory_color());
                            categoriesSqliteModel.setCategory_name(response.body().getData().get(i).getCategory_name());
                            categoriesSqliteModel.setCrown(response.body().getData().get(i).getCrown());
                            categoriesSqliteModel.setCrown(response.body().getData().get(i).getCrown_image());
                            categoriesSqliteModel.setLevel_count(response.body().getData().get(i).getLevel_count());
                            categoriesSqliteModel.setIs_followed(1);
                            categoriesSqliteModel.setImage_path(response.body().getData().get(i).getImage_path());

                            if (!categoriesTableQueries.updateToFollowedTopic(categoriesSqliteModel))
                                Toast.makeText(CatalogActivity.this, "Something went wrong. Please restart .", Toast.LENGTH_LONG).show();
                        }
                        appPreferences.saveBoolean(AppConfiguration.IS_GAME_MUSIC_SET, false);
                        appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, true);
                        appPreferences.saveBoolean(AppConfiguration.IS_SOUND_EFFECTS_SET, true);
                        startActivity(new Intent(CatalogActivity.this, HomeActivity.class));
                        finish();

                    }else if (response.body().getStatus() == 6004){
                        AlertDialogCustom.sessionExpiredDialog(CatalogActivity.this);
                    }else {
                        AlertDialogCustom.commonAlertDialog(CatalogActivity.this, response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<CreateCategoriesModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(CatalogActivity.this, getString(R.string.went_wrong_wait));
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

    private void toolbarInitiate() {
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        topicsRecyclerView = findViewById(R.id.my_recycler_view);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        doneTextView = findViewById(R.id.doneTextView);
        toolbar_title.setText(getResources().getText(R.string.catlogHeading));

    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
