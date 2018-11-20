package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;

import java.util.ArrayList;
import java.util.List;


/*
 * Created by senthil on 22-02-2018.
 */
public class CurrentlyPlayingMainTableQueries implements TableSchema {
    public static CurrentlyPlayingMainTableQueries currentlyPlayingMainTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static CurrentlyPlayingMainTableQueries sharedInstance(Context context) {

        if (currentlyPlayingMainTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            currentlyPlayingMainTableQueries = new CurrentlyPlayingMainTableQueries();
            currentlyPlayingMainTableQueries.allColumns = new String[8];
            currentlyPlayingMainTableQueries.allColumns[0] = PRIMARY_ID;
            currentlyPlayingMainTableQueries.allColumns[1] = STEPS;
            currentlyPlayingMainTableQueries.allColumns[2] = LEVEL;
            currentlyPlayingMainTableQueries.allColumns[3] = EN_CATEGORY_ID;
            currentlyPlayingMainTableQueries.allColumns[4] = CATEGORY;
            currentlyPlayingMainTableQueries.allColumns[5] = QUESTION;
            currentlyPlayingMainTableQueries.allColumns[6] = QUESTION_ID;
            currentlyPlayingMainTableQueries.allColumns[7] = EXPLANATION;
            currentlyPlayingMainTableQueries.open();
        }
        return currentlyPlayingMainTableQueries;
    }

    public void deleteAllCurrentlyPlayingMain() {
        open();
        sqLiteDatabase.delete(CURRENTLY_PLAYING_MAIN, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertCurrentlyPlayingMain(CurrentlyPlayingMainSqliteModel currentlyPlayingMainSqliteModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS, currentlyPlayingMainSqliteModel.getSteps());
        contentValues.put(LEVEL, currentlyPlayingMainSqliteModel.getLevel());
        contentValues.put(EN_CATEGORY_ID, currentlyPlayingMainSqliteModel.getCategory_id());
        contentValues.put(CATEGORY, currentlyPlayingMainSqliteModel.getCategory());
        contentValues.put(QUESTION, currentlyPlayingMainSqliteModel.getQuestion());
        contentValues.put(QUESTION_ID, currentlyPlayingMainSqliteModel.getQuestion_id());
        contentValues.put(EXPLANATION, currentlyPlayingMainSqliteModel.getExplanation());
        try {
            sqLiteDatabase.insert(CURRENTLY_PLAYING_MAIN, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public ArrayList<CurrentlyPlayingMainSqliteModel> getAllMainQuestions() {
        open();
        ArrayList<CurrentlyPlayingMainSqliteModel> categoriesSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CURRENTLY_PLAYING_MAIN, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CurrentlyPlayingMainSqliteModel currentlyPlayingMainSqliteModel = cursorToEntity(selected);
                    categoriesSqliteModelArrayList.add(currentlyPlayingMainSqliteModel);
                    selected.moveToNext();
                }
                selected.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }
        return categoriesSqliteModelArrayList;
    }

    protected CurrentlyPlayingMainSqliteModel cursorToEntity(Cursor cursor) {

        CurrentlyPlayingMainSqliteModel currentlyPlayingMainSqliteModel = new CurrentlyPlayingMainSqliteModel();
        int steps;
        int level;
        int category_id;
        int category;
        int question;
        int question_id;
        int explanation;

        if (cursor != null) {
            if (cursor.getColumnIndex(STEPS) != -1) {
                steps = cursor.getColumnIndexOrThrow(STEPS);
                currentlyPlayingMainSqliteModel.setSteps(cursor.getInt(steps));
            }
            if (cursor.getColumnIndex(LEVEL) != -1) {
                level = cursor.getColumnIndexOrThrow(LEVEL);
                currentlyPlayingMainSqliteModel.setLevel(cursor.getString(level));
            }
            if (cursor.getColumnIndex(EN_CATEGORY_ID) != -1) {
                category_id = cursor.getColumnIndexOrThrow(EN_CATEGORY_ID);
                currentlyPlayingMainSqliteModel.setCategory_id(cursor.getInt(category_id));
            }
            if (cursor.getColumnIndex(CATEGORY) != -1) {
                category = cursor.getColumnIndexOrThrow(CATEGORY);
                currentlyPlayingMainSqliteModel.setCategory(cursor.getString(category));
            }
            if (cursor.getColumnIndex(QUESTION) != -1) {
                question = cursor.getColumnIndexOrThrow(QUESTION);
                currentlyPlayingMainSqliteModel.setQuestion(cursor.getString(question));
            }
            if (cursor.getColumnIndex(QUESTION_ID) != -1) {
                question_id = cursor.getColumnIndexOrThrow(QUESTION_ID);
                currentlyPlayingMainSqliteModel.setQuestion_id(cursor.getInt(question_id));
            }
            if (cursor.getColumnIndex(EXPLANATION) != -1) {
                explanation = cursor.getColumnIndexOrThrow(EXPLANATION);
                currentlyPlayingMainSqliteModel.setExplanation(cursor.getString(explanation));
            }

        }
        return currentlyPlayingMainSqliteModel;
    }

}