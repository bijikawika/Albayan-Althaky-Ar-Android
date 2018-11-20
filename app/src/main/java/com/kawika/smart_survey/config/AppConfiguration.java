package com.kawika.smart_survey.config;

public abstract class AppConfiguration {
    public static String BASE_URL = "http://admine-learning.com/";
//    public static String BASE_URL = "http://e-learning.kawikatech.com/";
    public static final String APPLICATION_ID = "com.kawika.smart_survey";
    public static final String SMART_SURVEY_PREFS = "smart_survey_prefs";
    public static final String ABOUT_US_URL = "http://e-learning.kawikatech.com/";


    //prefs key
    public static final String LANGUAGE_ID = "language_id";
    public static final String MY_TOKEN = "my_token";
    public static final String USER_ID = "user_id";
    public static final String IS_LOGGED = "logged_status";
    public static final String IS_PUSH_NOTIFICATION_SET = "is_pushNotification_set";
    public static final String IS_SOUND_EFFECTS_SET = "is_sound_Effects_set";
    public static final String IS_GAME_MUSIC_SET = "is_game_music_set";

    //codes
    public static final int TIMEOUT_VALUE = 60000;
    public static final int CAMERA_AND_WRITE_REQUEST_CODE = 200;
    public static int IMAGE_CAPTURE = 1000;
    public static int PICK_IMAGE_REQUEST = 1001;
    public static final int PERMISSIONS_REQUEST_READ_PHONE = 7;
    public static final int READ_PHONE_ALERT = 1002;
    public static final int READ_AVATAR_RESULT = 1010;

    public static final int BEGINNER_TYPE = 1;
    public static final int INTERMEDIATE_TYPE = 2;
    public static final int EXPERT_TYPE = 3;
    public static final String DATA_URL = "dataUrl";
    public static final String FORGOT = "forgot";
    public static final String REGISTRATION = "registration";
    /**
     * page identifier valued
     * 1.page from Registration
     * 2.Forget Password
     */
    public static final String PAGE_IDENTIFIER = "pageIdentifier";
    public static final String NO_CROWN = "no crown";
    public static final String GOLD_CROWN = "Gold";
    public static final String SILVER_CROWN = "Silver";
    public static final String NONE = "none";
    public static final int NORMAL = 102;
    public static final int QUICK_CHALLENGE = 100;

}