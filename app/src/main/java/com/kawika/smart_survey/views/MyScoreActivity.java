package com.kawika.smart_survey.views;

import android.animation.Animator;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.LatestAchivementsAdapter;
import com.kawika.smart_survey.adapters.TopicsStatiticsAdapter;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.speedometer.ImageSpeedometer;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.utils.MyValueFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import static com.kawika.smart_survey.database.UserDataTableQueries.user_logintable_queries;
import static com.kawika.smart_survey.utils.RevealEffect.BOTTOM_LEFT;
import static com.kawika.smart_survey.utils.RevealEffect.CENTER;
import static com.kawika.smart_survey.utils.RevealEffect.showWithCircularRevealAnimation;


public class MyScoreActivity extends BaseActivity {

    private TextView scoreTextView, userNameTextView, departmentTextView, nolatestAcheivementsTextView,
    startPointTextView, endPointTextView;
    private LinearLayout departmentLinearLayout;
    private ImageView profileImageView;
    private YoYo.YoYoString rope;
    private MyScoreActivityBinding binding;
    private RecyclerView topicsStatiticsRecyclerView;
    private ImageSpeedometer imageSpeedometer;
    private PieChart mChart;
    private List<CategoriesSqliteModel> followedTopics;
    private RecyclerView latestAcheivementsRecyclerView;
    private AppPreferences appPreferences;

    @Override
    int getContentViewId() {
        return R.layout.my_score_activity;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_score;
    }

    @Override
    MyScoreActivityBinding getMyScoreActivityBinding() {
        return binding = DataBindingUtil.setContentView(this, R.layout.my_score_activity);
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

        appPreferences = AppPreferences.getInstance(MyScoreActivity.this, AppConfiguration.SMART_SURVEY_PREFS);


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.vRevealEffect.post(() -> showWithCircularRevealAnimation(binding.vRevealEffect, CENTER, BOTTOM_LEFT, MyScoreActivity.this.getResources().getInteger(R.integer.anim_duration_medium)));
            }
        } catch (Exception e) {
            Log.e("Welcome activity", "PermissionM: ", e);
        }

        initializeToolbar();

        topicsStatiticsRecyclerView = findViewById(R.id.topicsStatiticsRecyclerView);
        imageSpeedometer = findViewById(R.id.imageSpeedometer);
        latestAcheivementsRecyclerView = findViewById(R.id.latestAcheivementsRecyclerView);
        nolatestAcheivementsTextView = findViewById(R.id.nolatestAcheivementsTextView);
        startPointTextView = findViewById(R.id.startPointTextView);
        endPointTextView = findViewById(R.id.endPointTextView);

        setupStatistics();

        showAnimation();


        loadLatestAcheivements();
    }

    private void showAnimation() {
        userNameTextView = findViewById(R.id.userNameTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        profileImageView = findViewById(R.id.profileImageView);
        departmentLinearLayout = findViewById(R.id.departmentLinearLayout);
        departmentTextView = findViewById(R.id.departmentTextView);

        profileImageView.setVisibility(View.INVISIBLE);
        departmentLinearLayout.setVisibility(View.INVISIBLE);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                profileImageView.setVisibility(View.VISIBLE);
                rope = YoYo.with(Techniques.ZoomIn)
                        .duration(300)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        departmentLinearLayout.setVisibility(View.VISIBLE);
                                        rope = YoYo.with(Techniques.BounceInLeft)
                                                .duration(400)
                                                .withListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                })
                                                .playOn(departmentLinearLayout);
                                    }
                                }, 0);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .playOn(profileImageView);


            }
        }, 300);

        setData();
    }

    private void setData() {
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(MyScoreActivity.this);
        userNameTextView.setText(userDataTableQueries.getName());
        departmentTextView.setText(userDataTableQueries.getDepartment());
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            scoreTextView.setText(userDataTableQueries.getMyScore());
            startPointTextView.setText("0");
            endPointTextView.setText("500");
        }else{
            scoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(userDataTableQueries.getMyScore()));
            startPointTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic("0"));
            endPointTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic("500"));
        }
        Picasso.with(MyScoreActivity.this)
                .load(userDataTableQueries.getProfileImagePath())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(profileImageView);
    }

    private void setupStatistics() {
        imageSpeedometer.setWithTremble(false);
        imageSpeedometer.setMinSpeed(0);
        imageSpeedometer.setMaxSpeed(500);
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            imageSpeedometer.speedTo(Float.parseFloat(user_logintable_queries.getMyScore()), 0);
        }else{
            imageSpeedometer.speedTo(Float.parseFloat(String.valueOf(500 - Integer.valueOf(user_logintable_queries.getMyScore()))), 0);
        }

        initialiseTopicChart();
    }

    private void setupPieChart() {

        mChart = findViewById(R.id.pieChart);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(false);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        setPieData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(12f);

        mChart.setUsePercentValues(false);
        mChart.getLegend().setEnabled(false);

        setPieData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getText(R.string.my_score));

    }

    private void initialiseTopicChart() {
        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(MyScoreActivity.this);
        followedTopics = followedTopicsTableQueries.getFollowedTopics();

        if (followedTopics.size() > 0) {
            GridLayoutManager myAccountGridLayoutManager = new GridLayoutManager(MyScoreActivity.this, 1);
            topicsStatiticsRecyclerView.setLayoutManager(myAccountGridLayoutManager);

            TopicsStatiticsAdapter topicsStatiticsAdapter = new TopicsStatiticsAdapter(MyScoreActivity.this, followedTopics);
            topicsStatiticsRecyclerView.setAdapter(topicsStatiticsAdapter);
        }

        if (followedTopics.size() > 0)
            setupPieChart();
    }

    private void setPieData() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colorListArray = new ArrayList<Integer>();

        for (int i = 0; i < followedTopics.size(); i++) {
                if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
                    entries.add(new PieEntry((float) 1, String.valueOf(followedTopics.get(i).getLevel_count())));
                }else{
                    entries.add(new PieEntry((float) 1, DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(followedTopics.get(i).getLevel_count()))));
                }

        }

        PieDataSet dataSet = new PieDataSet(entries, "Staitics Results");
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        for (int i = 0; i < followedTopics.size(); i++) {
            colorListArray.add(Color.parseColor(followedTopics.get(i).getCategory_color()));
        }
        dataSet.setColors(colorListArray);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();

    }

    private void loadLatestAcheivements() {

        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(MyScoreActivity.this);
        List<CategoriesSqliteModel> categoriesSqliteModels = followedTopicsTableQueries.getLatestAcheivments();
        if (categoriesSqliteModels.size() > 0) {
            latestAcheivementsRecyclerView.setVisibility(View.VISIBLE);
            nolatestAcheivementsTextView.setVisibility(View.GONE);
            LatestAchivementsAdapter latestAchivementsAdapter = new LatestAchivementsAdapter(MyScoreActivity.this, categoriesSqliteModels);
            GridLayoutManager lLayout = new GridLayoutManager(MyScoreActivity.this, 1,
                    LinearLayoutManager.HORIZONTAL, false);
            latestAcheivementsRecyclerView.setHasFixedSize(true);
            latestAcheivementsRecyclerView.setLayoutManager(lLayout);
            latestAcheivementsRecyclerView.setAdapter(latestAchivementsAdapter);

        } else {
            latestAcheivementsRecyclerView.setVisibility(View.GONE);
            nolatestAcheivementsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rope != null) {
            rope.stop(true);
        }
        try {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}