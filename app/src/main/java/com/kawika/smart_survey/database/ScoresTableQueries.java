package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CheckedResultsSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.ScoresSqliteModel;

import java.util.ArrayList;


/*
 * Created by senthil on 22-02-2018.
 */
public class ScoresTableQueries implements TableSchema {
    public static ScoresTableQueries scoresTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static ScoresTableQueries sharedInstance(Context context) {

        if (scoresTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            scoresTableQueries = new ScoresTableQueries();
            scoresTableQueries.allColumns = new String[8];
            scoresTableQueries.allColumns[0] = PRIMARY_ID;
            scoresTableQueries.allColumns[1] = CATEGORY_NAME;
            scoresTableQueries.allColumns[2] = EN_CATEGORY_ID;
            scoresTableQueries.allColumns[3] = STEPS;
            scoresTableQueries.allColumns[4] = LEVEL;
            scoresTableQueries.allColumns[5] = TOTAL_MARK;
            scoresTableQueries.allColumns[6] = SCORED_MARK;
            scoresTableQueries.allColumns[7] = FULL_SCORED;
            scoresTableQueries.open();
        }
        return scoresTableQueries;
    }

    public void deleteAllScores() {
        open();
        sqLiteDatabase.delete(SCORES, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertScores(ScoresSqliteModel scoresSqliteModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, scoresSqliteModel.getCategory_name());
        contentValues.put(EN_CATEGORY_ID, scoresSqliteModel.getCategory_id());
        contentValues.put(STEPS, scoresSqliteModel.getStep_id());
        contentValues.put(LEVEL, scoresSqliteModel.getLevel_id());
        contentValues.put(TOTAL_MARK, scoresSqliteModel.getTotal_mark());
        contentValues.put(SCORED_MARK, scoresSqliteModel.getScored_mark());
        contentValues.put(FULL_SCORED, scoresSqliteModel.getFull_scored());
        try {
            sqLiteDatabase.insert(SCORES, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }
        return true;
    }
    public ScoresSqliteModel getScoresMain() {
        open();
        ScoresSqliteModel scoresSqliteModel = new ScoresSqliteModel();

        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(SCORES, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    scoresSqliteModel = cursorToEntity(selected);
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
        return scoresSqliteModel;
    }

    protected ScoresSqliteModel cursorToEntity(Cursor cursor) {

        ScoresSqliteModel scoresSqliteModel = new ScoresSqliteModel();
        int category_name;
        int category_id;
        int steps;
        int level;
        int total_mark;
        int scored_mark;
        int fully_scored;

        if (cursor != null) {
            if (cursor.getColumnIndex(CATEGORY_NAME) != -1) {
                category_name = cursor.getColumnIndexOrThrow(CATEGORY_NAME);
                scoresSqliteModel.setCategory_name(cursor.getString(category_name));
            }
            if (cursor.getColumnIndex(EN_CATEGORY_ID) != -1) {
                category_id = cursor.getColumnIndexOrThrow(EN_CATEGORY_ID);
                scoresSqliteModel.setCategory_id(cursor.getInt(category_id));
            }
            if (cursor.getColumnIndex(STEPS) != -1) {
                steps = cursor.getColumnIndexOrThrow(STEPS);
                scoresSqliteModel.setStep_id(cursor.getInt(steps));
            }
            if (cursor.getColumnIndex(LEVEL) != -1) {
                level = cursor.getColumnIndexOrThrow(LEVEL);
                scoresSqliteModel.setLevel_id(cursor.getInt(level));
            }
            if (cursor.getColumnIndex(TOTAL_MARK) != -1) {
                total_mark = cursor.getColumnIndexOrThrow(TOTAL_MARK);
                scoresSqliteModel.setTotal_mark(cursor.getInt(total_mark));
            }
            if (cursor.getColumnIndex(SCORED_MARK) != -1) {
                scored_mark = cursor.getColumnIndexOrThrow(SCORED_MARK);
                scoresSqliteModel.setScored_mark(cursor.getInt(scored_mark));
            }
            if (cursor.getColumnIndex(FULL_SCORED) != -1) {
                fully_scored = cursor.getColumnIndexOrThrow(FULL_SCORED);
                scoresSqliteModel.setFull_scored(cursor.getInt(fully_scored));
            }

        }
        return scoresSqliteModel;
    }

}
