package com.kawika.smart_survey.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.FollowedTopicsAdapter;
import com.kawika.smart_survey.adapters.TopPlayersAdapter;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.FetchCoursesInterface;
import com.kawika.smart_survey.callbacks.RecyclerClickHighlight;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.TopPlayersTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.TopPlayersModel;
import com.kawika.smart_survey.models.TopPlayersSqliteModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.services.FetchAllCourseService;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.GOLD_CROWN;
import static com.kawika.smart_survey.config.AppConfiguration.NO_CROWN;
import static com.kawika.smart_survey.config.AppConfiguration.SILVER_CROWN;
import static com.kawika.smart_survey.database.TopPlayersTableQueries.topPlayersTableQueries;
import static com.kawika.smart_survey.utils.RevealEffect.CENTER;
import static com.kawika.smart_survey.utils.RevealEffect.getViewToViewScalingAnimator;
import static com.kawika.smart_survey.utils.RevealEffect.showWithCircularRevealAnimation;


/*
 * Created by senthiljs on 08/02/18.
 */

public class HomeActivity extends BaseActivity implements RecyclerClickHighlight, FetchCoursesInterface {

    private TextView userNameTextView, doneTextView, scoreTextView, departmentTextView, selectedCategoryTitleTextView;
    private ImageView profileImageView, hiddenAnimatorImageView, targetImageView;
    private FollowedTopicsAdapter followedTopicsAdapter;
    private YoYo.YoYoString rope;
    private RecyclerView followedTopicsRecycleView, topPlayersRecyclerView;
    private RecyclerClickHighlight recyclerClick;
    private int selectedMaincategoryId;
    HomeActivityBinding binding;
    private RelativeLayout rootView;
    private View selectedHighlightImageView, shuttleView;
    public static final int ANIMATION_SPEED = 400;
    private FetchAllCourseService myService;
    private boolean bound = false;
    private boolean isPlaySound = false;
    private TopPlayersAdapter topPlayersAdapter;
    private AppPreferences appPreferences;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            FetchAllCourseService.LocalBinder binder = (FetchAllCourseService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(HomeActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            System.out.println(" service is disconnected= " );
            bound = false;
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPreferences = AppPreferences.getInstance(HomeActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        recyclerClick = this;

        topPlayersTableQueries = TopPlayersTableQueries.sharedInstance(HomeActivity.this);

        userNameTextView = findViewById(R.id.userNameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        hiddenAnimatorImageView = findViewById(R.id.shuttle);
        targetImageView = findViewById(R.id.selectedHighlightImageView);
        rootView = findViewById(R.id.rootView);
        selectedHighlightImageView = findViewById(R.id.selectedHighlightImageView);
        shuttleView = findViewById(R.id.shuttle);
        followedTopicsRecycleView = findViewById(R.id.my_recycler_view);
        scoreTextView = findViewById(R.id.scoreTextView);
        departmentTextView = findViewById(R.id.departmentTextView);
        topPlayersRecyclerView = findViewById(R.id.topPlayersRecyclerView);
        selectedCategoryTitleTextView = findViewById(R.id.selectedCategoryTitleTextView);

        initialiseToolbar();

        loadFollowedTopics();

        doneTextView.setOnClickListener(view -> {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            Intent quiz_start_intent = new Intent(HomeActivity.this, QuizTypeSelectActivity.class);
            quiz_start_intent.putExtra("selectedCategoryId", selectedMaincategoryId);
            startActivity(quiz_start_intent);

        });

        setData();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.vRevealEffect.post(() -> showWithCircularRevealAnimation(binding.vRevealEffect, CENTER, CENTER, HomeActivity.this.getResources().getInteger(R.integer.anim_duration_long)));
            }
        } catch (Exception e) {
            Log.e("Welcome activity", "PermissionM: ", e);
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("refreshedToken dcdcd= " + refreshedToken);

        Intent intent = new Intent(this, FetchAllCourseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    int getContentViewId() {
        return R.layout.home_activity;
    }

    @Override
    HomeActivityBinding getHomeBinding() {
        return binding = DataBindingUtil.setContentView(this, R.layout.home_activity);
    }

    @Override
    MyScoreActivityBinding getMyScoreActivityBinding() {
        return null;
    }

    @Override
    NotificationActivityBinding getNotificationBinding() {
        return null;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_home;
    }

    private void setData() {
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(HomeActivity.this);
        userNameTextView.setText(userDataTableQueries.getName());
        departmentTextView.setText(userDataTableQueries.getDepartment());
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            scoreTextView.setText(userDataTableQueries.getMyScore());
        }else{
            scoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(userDataTableQueries.getMyScore()));
        }
        if (userDataTableQueries.getProfileImagePath() != null) {
            Picasso.with(HomeActivity.this)
                    .load(userDataTableQueries.getProfileImagePath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileImageView);
        }
    }

    private void loadFollowedTopics() {

        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(HomeActivity.this);
        List<CategoriesSqliteModel> categoriesSqliteModels = followedTopicsTableQueries.getFollowedTopics();
        if (categoriesSqliteModels.size() > 0) {
            followedTopicsAdapter = new FollowedTopicsAdapter(HomeActivity.this, recyclerClick, categoriesSqliteModels);
            GridLayoutManager lLayout = new GridLayoutManager(HomeActivity.this, 1,
                    LinearLayoutManager.HORIZONTAL, false);
            followedTopicsRecycleView.setHasFixedSize(true);
            followedTopicsRecycleView.setLayoutManager(lLayout);
            followedTopicsRecycleView.setAdapter(followedTopicsAdapter);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(followedTopicsRecycleView);
        }
    }

    private void initialiseToolbar() {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getText(R.string.home));

        doneTextView = findViewById(R.id.doneTextView);

        doneTextView.setText(R.string.start_quiz);
        doneTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (rope != null) {
            rope.stop(true);
        }
//        try {
//            System.runFinalization();
//            Runtime.getRuntime().gc();
//            System.gc();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onRowClick(RelativeLayout view, int position, int selectedCategoryId, String topicUrl, String topicName) {
        if (isPlaySound) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
        }
        selectedCategoryTitleTextView.setText(topicName);
        isPlaySound = true;

        selectedMaincategoryId = selectedCategoryId;

        AppPreferences appPreferences = AppPreferences.getInstance(HomeActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
        loadTopPlayersOnline(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID), selectedCategoryId);

        Picasso.with(HomeActivity.this)
                .load(topicUrl)
                .placeholder(R.drawable.no_resource)
                .error(R.drawable.no_resource)
                .into(hiddenAnimatorImageView);

        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        view.getGlobalVisibleRect(fromRect);
        selectedHighlightImageView.getGlobalVisibleRect(toRect);

        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                shuttleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                shuttleView.setVisibility(View.GONE);
                Picasso.with(HomeActivity.this)
                        .load(topicUrl)
                        .placeholder(R.drawable.no_resource)
                        .error(R.drawable.no_resource)
                        .into(targetImageView);

                CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(HomeActivity.this);
                CategoriesSqliteModel courseDetailList = followedTopicsTableQueries.getSelectedCategoryBasedById(selectedCategoryId);

                ImageView crownImageView = findViewById(R.id.crownImageView);
                if (courseDetailList.getCrown() != null) {
                    if (courseDetailList.getCrown().equals(GOLD_CROWN)) {
                        Picasso.with(HomeActivity.this)
                                .load(courseDetailList.getCrown_image())
                                .into(crownImageView);
                    } else if (courseDetailList.getCrown().equals(SILVER_CROWN)) {
                        Picasso.with(HomeActivity.this)
                                .load(courseDetailList.getCrown_image())
                                .into(crownImageView);
                    } else if (courseDetailList.getCrown().equals(NO_CROWN)) {
                        crownImageView.setImageDrawable(null);
                    } else {
                        crownImageView.setImageDrawable(null);
                    }

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    public void languageChanged(String category_name) {
        selectedCategoryTitleTextView.setText(category_name);

    }

    private void loadTopPlayersOnline(String myToken, String userId, int selectedCategoryId) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            binding.topPlayersNotFoundTextView.setText(R.string.no_internet);
            return;
        }

        binding.topPlayersNotFoundTextView.setVisibility(View.VISIBLE);
        topPlayersRecyclerView.setVisibility(View.GONE);
        binding.topPlayersNotFoundTextView.setText(R.string.loading);
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<TopPlayersModel> categoriesCall = retrofitService.getTopPlayers(myToken, userId, selectedCategoryId);
        categoriesCall.enqueue(new Callback<TopPlayersModel>() {
            @Override
            public void onResponse(Call<TopPlayersModel> call, Response<TopPlayersModel> response) {
                if (response.code() == 200) {
                    System.out.println("response code check = " + response.body().getStatus());
                    if (response.body().getStatus() == 6000) {

                        ArrayList<TopPlayersSqliteModel> topPlayersSqliteModelArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            String firstName = response.body().getData().get(i).getPlayer_details().getFirstname();
                            String lastName = response.body().getData().get(i).getPlayer_details().getLastname();
                            int userId = response.body().getData().get(i).getUser_id();
                            int totalMark = response.body().getData().get(i).getTotal_mark();
                            int rank = response.body().getData().get(i).getRank();
                            String imagePath = response.body().getData().get(i).getPlayer_details().getImage_path();
                            String categoryName = response.body().getData().get(i).getPlayer_details().getCategory_name();

                            TopPlayersSqliteModel topPlayersSqliteModel = new TopPlayersSqliteModel();
                            topPlayersSqliteModel.setEn_category_id(selectedCategoryId);
                            topPlayersSqliteModel.setFirstname(firstName);
                            topPlayersSqliteModel.setLastname(lastName);
                            topPlayersSqliteModel.setUser_id(userId);
                            topPlayersSqliteModel.setTotal_mark(totalMark);
                            topPlayersSqliteModel.setRank(rank);
                            topPlayersSqliteModel.setImage(imagePath);
                            topPlayersSqliteModel.setCategory_name(categoryName);
                            topPlayersSqliteModelArrayList.add(topPlayersSqliteModel);


                        }
                        binding.topPlayersNotFoundTextView.setText(R.string.no_top_players_found);

                        if (topPlayersSqliteModelArrayList.size() > 0) {
                            binding.topPlayersNotFoundTextView.setVisibility(View.GONE);
                            topPlayersRecyclerView.setVisibility(View.VISIBLE);

                            if (selectedCategoryId == selectedMaincategoryId) {
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
                                topPlayersRecyclerView.setLayoutManager(gridLayoutManager);
                                topPlayersAdapter = new TopPlayersAdapter(HomeActivity.this, topPlayersSqliteModelArrayList, selectedCategoryId);
                                topPlayersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                topPlayersRecyclerView.setAdapter(topPlayersAdapter);
                            }
                        } else {
                            binding.topPlayersNotFoundTextView.setVisibility(View.VISIBLE);
                            topPlayersRecyclerView.setVisibility(View.GONE);
                        }
                    }else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(HomeActivity.this);
                    } else {
                        binding.topPlayersNotFoundTextView.setVisibility(View.VISIBLE);
                        binding.topPlayersNotFoundTextView.setText(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<TopPlayersModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(HomeActivity.this);
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            scoreTextView.setText(userDataTableQueries.getMyScore());
        }else{
            scoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(userDataTableQueries.getMyScore()));
        }
    }

    @Override
    public void coursesFetchingCompleted() {
        setData();
        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(HomeActivity.this);
        List<CategoriesSqliteModel> categoriesSqliteModelList = followedTopicsTableQueries.getFollowedTopics();
        if (categoriesSqliteModelList.size() > 0) {
            if (followedTopicsAdapter != null)
            followedTopicsAdapter.notifyList(categoriesSqliteModelList);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            myService.setCallbacks(null);
            unbindService(serviceConnection);
            bound = false;
        }
    }
}