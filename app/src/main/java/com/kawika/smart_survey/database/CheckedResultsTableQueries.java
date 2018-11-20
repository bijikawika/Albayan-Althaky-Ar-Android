package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CheckedResultsSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;

import java.util.ArrayList;


/*
 * Created by senthil on 22-02-2018.
 */
public class CheckedResultsTableQueries implements TableSchema {
    public static CheckedResultsTableQueries checkedResultsTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static CheckedResultsTableQueries sharedInstance(Context context) {

        if (checkedResultsTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            checkedResultsTableQueries = new CheckedResultsTableQueries();
            checkedResultsTableQueries.allColumns = new String[4];
            checkedResultsTableQueries.allColumns[0] = PRIMARY_ID;
            checkedResultsTableQueries.allColumns[1] = QUESTION_ID;
            checkedResultsTableQueries.allColumns[2] = CLICKED_ID;
            checkedResultsTableQueries.allColumns[3] = PASSED_RESULT;
            checkedResultsTableQueries.open();
        }
        return checkedResultsTableQueries;
    }

    public void deleteAllCheckedResults() {
        open();
        sqLiteDatabase.delete(CHECKED_RESULTS, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertCheckedResults(CheckedResultsSqliteModel checkedResultsSqliteModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_ID, checkedResultsSqliteModel.getQuestion_id());
        contentValues.put(CLICKED_ID, checkedResultsSqliteModel.getClicked_id());
        contentValues.put(PASSED_RESULT, checkedResultsSqliteModel.getPassed_result());
        try {
            sqLiteDatabase.insert(CHECKED_RESULTS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }
        return true;
    }

    public ArrayList<CheckedResultsSqliteModel> getAllAnswerResults() {
        open();
        ArrayList<CheckedResultsSqliteModel> checkedResultsSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CHECKED_RESULTS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CheckedResultsSqliteModel checkedResultsSqliteModel = cursorToEntity(selected);
                    checkedResultsSqliteModelArrayList.add(checkedResultsSqliteModel);
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
        return checkedResultsSqliteModelArrayList;
    }


    protected CheckedResultsSqliteModel cursorToEntity(Cursor cursor) {

        CheckedResultsSqliteModel checkedResultsSqliteModel = new CheckedResultsSqliteModel();
        int question_id;
        int clicked_id;
        int passed_result;

        if (cursor != null) {
            if (cursor.getColumnIndex(QUESTION_ID) != -1) {
                question_id = cursor.getColumnIndexOrThrow(QUESTION_ID);
                checkedResultsSqliteModel.setQuestion_id(cursor.getInt(question_id));
            }
            if (cursor.getColumnIndex(CLICKED_ID) != -1) {
                clicked_id = cursor.getColumnIndexOrThrow(CLICKED_ID);
                checkedResultsSqliteModel.setClicked_id(cursor.getInt(clicked_id));
            }
            if (cursor.getColumnIndex(PASSED_RESULT) != -1) {
                passed_result = cursor.getColumnIndexOrThrow(PASSED_RESULT);
                checkedResultsSqliteModel.setPassed_result(cursor.getString(passed_result));
            }
        }
        return checkedResultsSqliteModel;
    }
}
