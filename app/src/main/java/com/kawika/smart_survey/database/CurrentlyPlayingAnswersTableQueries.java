package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;

import java.util.ArrayList;
import java.util.List;


/*
 * Created by senthil on 22-02-2018.
 */
public class CurrentlyPlayingAnswersTableQueries implements TableSchema {
    public static CurrentlyPlayingAnswersTableQueries currentlyPlayingAnswersTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static CurrentlyPlayingAnswersTableQueries sharedInstance(Context context) {

        if (currentlyPlayingAnswersTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            currentlyPlayingAnswersTableQueries = new CurrentlyPlayingAnswersTableQueries();
            currentlyPlayingAnswersTableQueries.allColumns = new String[6];
            currentlyPlayingAnswersTableQueries.allColumns[0] = PRIMARY_ID;
            currentlyPlayingAnswersTableQueries.allColumns[1] = STEPS;
            currentlyPlayingAnswersTableQueries.allColumns[2] = QUESTION_ID;
            currentlyPlayingAnswersTableQueries.allColumns[3] = ANSWER_ID;
            currentlyPlayingAnswersTableQueries.allColumns[4] = ANSWER;
            currentlyPlayingAnswersTableQueries.allColumns[5] = CORRECT_ANSWER;
            currentlyPlayingAnswersTableQueries.open();
        }
        return currentlyPlayingAnswersTableQueries;
    }

    public void deleteAllCurrentlyPlayingAnswers() {
        open();
        sqLiteDatabase.delete(CURRENTLY_PLAYING_ANSWERS, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertCurrentlyPlayingAnswers(CurrentlyPlayingAnswersSqliteModel currentlyPlayingAnswersSqliteModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS, currentlyPlayingAnswersSqliteModel.getSteps());
        contentValues.put(QUESTION_ID, currentlyPlayingAnswersSqliteModel.getQuestion_id());
        contentValues.put(ANSWER_ID, currentlyPlayingAnswersSqliteModel.getAnswer_id());
        contentValues.put(ANSWER, currentlyPlayingAnswersSqliteModel.getAnswer());
        contentValues.put(CORRECT_ANSWER, currentlyPlayingAnswersSqliteModel.getCorrect_answer());
        try {
            sqLiteDatabase.insert(CURRENTLY_PLAYING_ANSWERS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e.toString() = " + e.toString());
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public ArrayList<CurrentlyPlayingAnswersSqliteModel> getSelectedAnswerBasedById(int selectedCount) {
        open();
        ArrayList<CurrentlyPlayingAnswersSqliteModel> currentlyPlayingAnswersSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CURRENTLY_PLAYING_ANSWERS, allColumns, getWhereForQuestionId(selectedCount), null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CurrentlyPlayingAnswersSqliteModel currentlyPlayingAnswersSqliteModel = cursorToEntity(selected);
                    currentlyPlayingAnswersSqliteModelArrayList.add(currentlyPlayingAnswersSqliteModel);
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
        return currentlyPlayingAnswersSqliteModelArrayList;
    }

    protected CurrentlyPlayingAnswersSqliteModel cursorToEntity(Cursor cursor) {

        CurrentlyPlayingAnswersSqliteModel currentlyPlayingAnswersSqliteModel = new CurrentlyPlayingAnswersSqliteModel();
        int steps;
        int question_id;
        int answer_id;
        int answer;
        int correct_answer;

        if (cursor != null) {
            if (cursor.getColumnIndex(STEPS) != -1) {
                steps = cursor.getColumnIndexOrThrow(STEPS);
                currentlyPlayingAnswersSqliteModel.setSteps(cursor.getInt(steps));
            }
            if (cursor.getColumnIndex(QUESTION_ID) != -1) {
                question_id = cursor.getColumnIndexOrThrow(QUESTION_ID);
                currentlyPlayingAnswersSqliteModel.setQuestion_id(cursor.getInt(question_id));
            }
            if (cursor.getColumnIndex(ANSWER_ID) != -1) {
                answer_id = cursor.getColumnIndexOrThrow(ANSWER_ID);
                currentlyPlayingAnswersSqliteModel.setAnswer_id(cursor.getInt(answer_id));
            }
            if (cursor.getColumnIndex(ANSWER) != -1) {
                answer = cursor.getColumnIndexOrThrow(ANSWER);
                currentlyPlayingAnswersSqliteModel.setAnswer(cursor.getString(answer));
            }
            if (cursor.getColumnIndex(CORRECT_ANSWER) != -1) {
                correct_answer = cursor.getColumnIndexOrThrow(CORRECT_ANSWER);
                currentlyPlayingAnswersSqliteModel.setCorrect_answer(cursor.getInt(correct_answer));
            }

        }
        return currentlyPlayingAnswersSqliteModel;
    }


    private String getWhereForQuestionId(int questionId) {
        return QUESTION_ID + " = '" + questionId + "'";
    }
}
