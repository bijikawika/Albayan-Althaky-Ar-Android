package com.kawika.smart_survey.database;

/*
 * Created by senthil on 22/02/18.
 */

public interface TableSchema {
    /**
     * Table name
     */
    String USER_DETAILS = "user_details";
    String CATEGORIES = "categories";
    String CURRENTLY_PLAYING_MAIN = "currently_playing_main";
    String CURRENTLY_PLAYING_ANSWERS = "currently_playing_answers";
    String SCORES = "scores";
    String CHECKED_RESULTS = "checked_results";
    String TOP_PLAYERS = "top_players";
    String QUICK_CHALLENGE = "quick_challenge";

    /**
     * Columns of USER_DETAILS
     */
    String PRIMARY_ID = "primary_id";
    String DEPARTMENT_ID = "department_id";
    String DEPARTMENT_NAME = "department_name";
    String DEVICE_ID = "device_id";
    String EMAIL = "email";
    String FIRST_NAME = "firstname";
    String LANGUAGE_ID = "language_id";
    String LAST_NAME = "lastname";
    String PHONE = "phone";
    String ID = "id";
    String IMAGE = "image";

    /**
     * Columns of CATEGORIES
     */
    String EN_CATEGORY_ID = "en_category_id";
    String CATEGORY_COLOR = "category_color";
    String CATEGORY_NAME = "category_name";
    String IMAGE_PATH = "image_path";
    String IS_FOLLOWED = "is_followed";
    String CROWN = "crown";
    String CROWN_IMAGE = "crown_image";
    String LEVEL_COUNT = "level_count";

    /**
     * Columns of CURRENTLY_PLAYING_MAIN
     */
    String STEPS = "steps";
    String LEVEL = "level";
    String CATEGORY = "category";
    String QUESTION = "question";
    String QUESTION_ID = "question_id";
    String EXPLANATION = "explanation";

    /**
     * Columns of CURRENTLY_PLAYING_ANSWERS
     */
    String ANSWER_ID = "answer_id";
    String ANSWER = "answer";
    String CORRECT_ANSWER = "correct_answer";

    /**
     * Columns of SCORES
     */
    String TOTAL_MARK = "total_mark";
    String SCORED_MARK = "scored_mark";
    String FULL_SCORED = "full_scored";

    /**
     * Columns of CLICKED_RESULTS
     */
    String PASSED_RESULT = "passed_result";
    String CLICKED_ID = "clicked_id";

    /**
     * Columns of TOP_PLAYERS
     */
    String USER_ID = "user_id";
    String RANK = "rank";

    /**
     * Columns of QUICK_CHALLENGE
     */
    String IS_AVAILABLE = "is_available";
    String MESSAGE = "message";

    /**
     * Create table for CATEGORIES
     */
    String CREATE_USER_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + USER_DETAILS
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DEPARTMENT_ID + " TEXT , "
            + DEPARTMENT_NAME + " TEXT , "
            + DEVICE_ID + " TEXT , "
            + EMAIL + " TEXT , "
            + FIRST_NAME + " TEXT , "
            + LANGUAGE_ID + " TEXT , "
            + LAST_NAME + " TEXT , "
            + PHONE + " TEXT , "
            + TOTAL_MARK + " TEXT , "
            + ID + " TEXT , "
            + IMAGE + " TEXT "
            + ")";

    /**
     * Create table for CATEGORIES
     */
    String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS "
            + CATEGORIES
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EN_CATEGORY_ID + " TEXT , "
            + CATEGORY_COLOR + " TEXT , "
            + CATEGORY_NAME + " TEXT , "
            + CROWN + " TEXT , "
            + CROWN_IMAGE + " TEXT , "
            + LEVEL_COUNT + " INTEGER , "
            + IS_FOLLOWED + " INTEGER , "
            + IMAGE_PATH + " TEXT "
            + ")";

    /**
     * Create table for CURRENTLY_PLAYING
     */
    String CREATE_CURRENTLY_PLAYING_MAIN_TABLE = "CREATE TABLE IF NOT EXISTS "
            + CURRENTLY_PLAYING_MAIN
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STEPS + " TEXT , "
            + LEVEL + " TEXT , "
            + EN_CATEGORY_ID + " INTEGER , "
            + CATEGORY + " TEXT , "
            + QUESTION + " TEXT , "
            + QUESTION_ID + " TEXT , "
            + EXPLANATION + " INTEGER "
            + ")";

    /**
     * Create table for CURRENTLY_PLAYING_ANSWERS
     */
    String CREATE_CURRENTLY_PLAYING_ANSWERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + CURRENTLY_PLAYING_ANSWERS
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STEPS + " TEXT , "
            + QUESTION_ID + " TEXT , "
            + ANSWER_ID + " TEXT , "
            + ANSWER + " TEXT , "
            + CORRECT_ANSWER + " INTEGER "
            + ")";

    /**
     * Create table for SCORES
     */
    String CREATE_SCORES_TABLE = "CREATE TABLE IF NOT EXISTS "
            + SCORES
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CATEGORY_NAME + " TEXT , "
            + EN_CATEGORY_ID + " INTEGER , "
            + STEPS + " TEXT , "
            + LEVEL + " TEXT , "
            + TOTAL_MARK + " TEXT , "
            + SCORED_MARK + " TEXT , "
            + FULL_SCORED + " TEXT "
            + ")";

    /**
     * Create table for CLICKED_RESULTS
     */
    String CREATE_CHECKED_RESULTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + CHECKED_RESULTS
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + QUESTION_ID + " TEXT , "
            + CLICKED_ID + " TEXT , "
            + PASSED_RESULT + " TEXT "
            + ")";

    /**
     * Create table for TOP_PLAYERS
     */
    String CREATE_TOP_PLAYERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TOP_PLAYERS
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIRST_NAME + " TEXT , "
            + EN_CATEGORY_ID + " TEXT , "
            + LAST_NAME + " TEXT , "
            + USER_ID + " TEXT , "
            + TOTAL_MARK + " TEXT , "
            + RANK + " TEXT , "
            + IMAGE_PATH + " TEXT , "
            + CATEGORY_NAME + " TEXT "
            + ")";

    /**
     * Create table for TOP_PLAYERS
     */
    String CREATE_QUICK_CHALLENGE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + QUICK_CHALLENGE
            + " ("
            + PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IS_AVAILABLE + " TEXT , "
            + MESSAGE + " TEXT "
            + ")";

}
