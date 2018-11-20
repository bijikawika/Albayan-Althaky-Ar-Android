package com.kawika.smart_survey.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.kawika.smart_survey.database.TableSchema.CATEGORIES;
import static com.kawika.smart_survey.database.TableSchema.CHECKED_RESULTS;
import static com.kawika.smart_survey.database.TableSchema.CREATE_CATEGORIES_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_CHECKED_RESULTS_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_CURRENTLY_PLAYING_ANSWERS_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_CURRENTLY_PLAYING_MAIN_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_QUICK_CHALLENGE_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_SCORES_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_TOP_PLAYERS_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CREATE_USER_DETAILS_TABLE;
import static com.kawika.smart_survey.database.TableSchema.CURRENTLY_PLAYING_ANSWERS;
import static com.kawika.smart_survey.database.TableSchema.CURRENTLY_PLAYING_MAIN;
import static com.kawika.smart_survey.database.TableSchema.QUICK_CHALLENGE;
import static com.kawika.smart_survey.database.TableSchema.SCORES;
import static com.kawika.smart_survey.database.TableSchema.TOP_PLAYERS;
import static com.kawika.smart_survey.database.TableSchema.USER_DETAILS;


/*
 * Created by Kawika on 23-08-2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "albayan_althaky";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_DETAILS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_CURRENTLY_PLAYING_MAIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_CURRENTLY_PLAYING_ANSWERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_SCORES_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHECKED_RESULTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TOP_PLAYERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUICK_CHALLENGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CURRENTLY_PLAYING_MAIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CURRENTLY_PLAYING_ANSWERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCORES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHECKED_RESULTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TOP_PLAYERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUICK_CHALLENGE);
    }

}
