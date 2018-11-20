package com.kawika.smart_survey.api_services;

import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.AvatarModel;
import com.kawika.smart_survey.models.BasicRetroCallModel;
import com.kawika.smart_survey.models.Categories;
import com.kawika.smart_survey.models.CommonModel;
import com.kawika.smart_survey.models.CountryCodeModel;
import com.kawika.smart_survey.models.CreateCategoriesModel;
import com.kawika.smart_survey.models.DepartmentModel;
import com.kawika.smart_survey.models.FollowedTopicsRawModel;
import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.GetQuestionsModel;
import com.kawika.smart_survey.models.PlayedLevelsModel;
import com.kawika.smart_survey.models.ProfileUpdateResponse;
import com.kawika.smart_survey.models.QuickChallengeCheckModel;
import com.kawika.smart_survey.models.RegistrationRawCreation;
import com.kawika.smart_survey.models.SubmitResultModel;
import com.kawika.smart_survey.models.SubmitResultsModel;
import com.kawika.smart_survey.models.TopPlayersModel;
import com.kawika.smart_survey.models.UserDetailsModel;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitService {


    class Factory {
        public static RetrofitService create() {

            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(AppConfiguration.TIMEOUT_VALUE, TimeUnit.MILLISECONDS);
            b.writeTimeout(AppConfiguration.TIMEOUT_VALUE, TimeUnit.MILLISECONDS);

            OkHttpClient client = b.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfiguration.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(RetrofitService.class);
        }
    }

    @POST("get-countries")
    Call<CountryCodeModel> getCountryCodes();

    @FormUrlEncoded
    @POST("get-departments")
    Call<DepartmentModel> getDepartment(@Field("language_id") int language_id);

    @POST("user-avatar")
    Call<AvatarModel> getAvatarList();

    @POST("user-registration")
    Call<AuthenticationModel> registerUser(@Body RegistrationRawCreation body);

    @FormUrlEncoded
    @POST("user-login")
    Call<AuthenticationModel> loginUser(@Field("email") String email,
                                        @Field("password") String password,
                                        @Field("device_id") long deviceId,
                                        @Field("language_id") int language_id);

    @FormUrlEncoded
    @POST("user-forgot-password")
    Call<CommonModel> forgetPassword(@Field("email") String email,
                                     @Field("phone") String phoneNo);

    @FormUrlEncoded
    @POST("user-otp-validation")
    Call<CommonModel> otpVerification(@Field("id") int id,
                                      @Field("otp") String otp,
                                      @Field("type") String type);

    @FormUrlEncoded
    @POST("user-resend-otp")
    Call<CommonModel> resendPin(@Field("id") int id,
                                @Field("type") String type);

    @FormUrlEncoded
    @POST("user-set-password")
    Call<CommonModel> setPassword(@Field("id") int id,
                                  @Field("newpassword") String type);

    @GET
    Call<ResponseBody> fetchImageFromServer(@Url String url);

    @FormUrlEncoded
    @POST("get-categories")
    Call<Categories> getCategories(@Header("Authorization") String authorization,
                                   @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("user-details")
    Call<UserDetailsModel> getUserDetails(@Header("Authorization") String authorization,
                                         @Field("user_id") String user_id);

    @POST("create-user-categories")
    Call<CreateCategoriesModel> createFollowedTopics(@Header("Authorization") String authorization,
                                                     @Body FollowedTopicsRawModel body);

    @FormUrlEncoded
    @POST("get-questions")
    Call<GetQuestionsModel> getQuestions(@Header("Authorization") String authorization,
                                         @Field("user_id") String user_id,
                                         @Field("category_id") int category_id,
                                         @Field("level_id") int level_id,
                                         @Field("step_id") int step_id);

    @POST("user-played-results")
    Call<SubmitResultsModel> submitResults(@Header("Authorization") String authorization,
                                           @Body SubmitResultModel body);

    @FormUrlEncoded
    @POST("user-played-levels")
    Call<PlayedLevelsModel> getLevel(@Header("Authorization") String authorization,
                                     @Field("user_id") String user_id,
                                     @Field("category_id") int category_id,
                                     @Field("level_id") int level_id);

    @FormUrlEncoded
    @POST("user-update-profile")
    Call<ProfileUpdateResponse> userUpdateProfile(@Header("Authorization") String authorization,
                                                  @Field("user_id") String user_id,
                                                  @Field("firstname") String firstname,
                                                  @Field("lastname") String lastName,
                                                  @Field("profile_image") String profile_image);

    @FormUrlEncoded
    @POST("user-logout")
    Call<CommonModel> logOut(@Header("Authorization") String authorization,
                             @Field("user_id") String id);

    @FormUrlEncoded
    @POST("top-players")
    Call<TopPlayersModel> getTopPlayers(@Header("Authorization") String authorization,
                                        @Field("user_id") String user_id,
                                        @Field("category_id") int category_id);

    @FormUrlEncoded
    @POST("push-notification")
    Call<BasicRetroCallModel> setPushPermission(@Header("Authorization") String authorization,
                                        @Field("user_id") String user_id,
                                        @Field("push_notify") int push_notify,
                                        @Field("push_token") String push_token,
                                        @Field("notification_type") String notification_type);

    @FormUrlEncoded
    @POST("challenge-questions")
    Call<GetQuestionsModel> getQuickChallenge(@Header("Authorization") String authorization,
                                        @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("daily-notification")
    Call<QuickChallengeCheckModel> checkQuickChallenge(@Header("Authorization") String authorization,
                                                       @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("deny-challenge")
    Call<BasicRetroCallModel> denyQuickChallenge(@Header("Authorization") String authorization,
                                                       @Field("user_id") String user_id);
}
