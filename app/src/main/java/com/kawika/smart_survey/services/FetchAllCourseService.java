package com.kawika.smart_survey.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.FetchCoursesInterface;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.UserDetailsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.views.HomeActivity;
import com.kawika.smart_survey.views.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.database.TopPlayersTableQueries.topPlayersTableQueries;

/**
 * Created by senthil on 23/10/17.
 */

public class FetchAllCourseService extends Service {

    NetworkCheck networkCheck;
    private final IBinder binder = new LocalBinder();
    private FetchCoursesInterface serviceCallbacks;
    private Handler myHandler = new Handler();
    private AppPreferences appPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        appPreferences = AppPreferences.getInstance(FetchAllCourseService.this, AppConfiguration.SMART_SURVEY_PREFS);
        networkCheck = new NetworkCheck(this);

        if (networkCheck.isConnectingToInternet()) {
            categoriesTableQueries = CategoriesTableQueries.sharedInstance(FetchAllCourseService.this);
            SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
            RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

            Call<UserDetailsModel> userDetailsModelCall = retrofitService.getUserDetails(appPreferences.getData(AppConfiguration.MY_TOKEN),
                    appPreferences.getData(AppConfiguration.USER_ID));
            userDetailsModelCall.enqueue(new Callback<UserDetailsModel>() {
                @Override
                public void onResponse(Call<UserDetailsModel> call, Response<UserDetailsModel> response) {
                    if (response.code() == 200) {
                        if (response.body().getStatus() == 6000) {
//                            AsyncTask.execute(new Runnable() {
//                                @Override
//                                public void run() {
                                    onDataFetchSuccess(response.body().getData());
                                    final Runnable updateRunnable = new Runnable() {
                                        public void run() {
                                            if (serviceCallbacks != null)
                                                serviceCallbacks.coursesFetchingCompleted();
                                            onUnbind(new Intent(FetchAllCourseService.this, FetchAllCourseService.class));
                                        }
                                    };
                                    myHandler.post(updateRunnable);
                                }
//                            });
//                        }
                    }

                }

                @Override
                public void onFailure(Call<UserDetailsModel> call, Throwable t) {
                    System.out.println("t = " + t.getMessage());
                }

            });

        } else {
            onUnbind(new Intent(FetchAllCourseService.this, FetchAllCourseService.class));
        }
        return binder;
    }

    public void setCallbacks(FetchCoursesInterface callbacks) {
        serviceCallbacks = callbacks;
    }

    public class LocalBinder extends Binder {
        public FetchAllCourseService getService() {
            return FetchAllCourseService.this;
        }
    }

    public void onDataFetchSuccess(UserDetailsModel.DataBean data) {
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
        userDataTableQueries.deleteAllUserDetails();

        userDataTableQueries.insertDetails(data.getDepartment_id(), data.getDepartment(), data.getDevice_id(),
                data.getEmail(), data.getFirstname(), data.getLanguage_id(), data.getLastname(), data.getPhone(),
                data.getUser_mark(), data.getId(), data.getImage_path());

        categoriesTableQueries = categoriesTableQueries.sharedInstance(FetchAllCourseService.this);
//        topPlayersTableQueries = topPlayersTableQueries.sharedInstance(FetchAllCourseService.this);

        categoriesTableQueries.deleteAllCategories();
//        topPlayersTableQueries.deleteAllTopPlayers();


        for (int i = 0; i < data.getUser_categories().size(); i++) {
            final CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
            categoriesSqliteModel.setEn_category_id(data.getUser_categories().get(i).getEn_category_id());
            categoriesSqliteModel.setCategory_color(data.getUser_categories().get(i).getCategory_color());
            categoriesSqliteModel.setCategory_name(data.getUser_categories().get(i).getCategory_name());
            categoriesSqliteModel.setCrown(data.getUser_categories().get(i).getCrown());
            categoriesSqliteModel.setCrown_image(data.getUser_categories().get(i).getCrown_image());
            categoriesSqliteModel.setLevel_count(data.getUser_categories().get(i).getLevel_count());
            categoriesSqliteModel.setIs_followed(data.getUser_categories().get(i).getIs_followed());
            categoriesSqliteModel.setImage_path(data.getUser_categories().get(i).getImage_path());

            System.out.println("categoriesSqliteModel = " + data.getUser_categories().get(i).getImage_path());

            categoriesTableQueries.insertAllTopics(categoriesSqliteModel);

//            if (data.getUser_categories().get(i).getTop_players().size() > 0) {
//                for (int j = 0; j < data.getUser_categories().get(i).getTop_players().size(); j++) {
//                    int categorId = data.getUser_categories().get(i).getEn_category_id();
//                    String firstName = data.getUser_categories().get(i).getTop_players().get(j).getFirstname();
//                    String lastName = data.getUser_categories().get(i).getTop_players().get(j).getLastname();
//                    int userId = data.getUser_categories().get(i).getTop_players().get(j).getUser_id();
//                    int totalMark = data.getUser_categories().get(i).getTop_players().get(j).getTotal_mark();
//                    int rank = data.getUser_categories().get(i).getTop_players().get(j).getRank();
//                    String imagePath =  data.getUser_categories().get(i).getTop_players().get(j).getImage_path();
//                    String categoryName = data.getUser_categories().get(i).getTop_players().get(j).getCategory_name();
//                    topPlayersTableQueries.insertTopPlayers(categorId, firstName, lastName, userId, totalMark, rank, imagePath, categoryName);
//
//                }
//            }

        }
        if (data.getChallengeNotification() != null){
            quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());
            quickChallengeTableQueries.deleteAllQuickChallenge();
            System.out.println("data.getChallengeNcwefweotification().getIs_available() = " + data.getChallengeNotification().getIs_available());
            quickChallengeTableQueries.insertQuickChallenge(data.getChallengeNotification().getIs_available(), data.getChallengeNotification().getMessage());
        }

        if (data.getChallengeNotification().getPush_enable() == 1)
            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, true);
        else
            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, false);


    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println(" service unbined= " );
        return super.onUnbind(intent);
    }

}
