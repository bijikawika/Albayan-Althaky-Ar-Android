package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.ProfileUpdateResponse;
import com.kawika.smart_survey.models.ScoresSqliteModel;
import com.kawika.smart_survey.models.SubmitResultsModel;
import com.kawika.smart_survey.models.TopPlayersModel;
import com.kawika.smart_survey.models.TopPlayersSqliteModel;

import java.util.ArrayList;


/*
 * Created by senthil on 10-03-2018.
 */
public class TopPlayersTableQueries implements TableSchema {
    public static TopPlayersTableQueries topPlayersTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static TopPlayersTableQueries sharedInstance(Context context) {

        if (topPlayersTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            topPlayersTableQueries = new TopPlayersTableQueries();
            topPlayersTableQueries.allColumns = new String[9];
            topPlayersTableQueries.allColumns[0] = PRIMARY_ID;
            topPlayersTableQueries.allColumns[1] = EN_CATEGORY_ID;
            topPlayersTableQueries.allColumns[2] = FIRST_NAME;
            topPlayersTableQueries.allColumns[3] = LAST_NAME;
            topPlayersTableQueries.allColumns[4] = USER_ID;
            topPlayersTableQueries.allColumns[5] = TOTAL_MARK;
            topPlayersTableQueries.allColumns[6] = RANK;
            topPlayersTableQueries.allColumns[7] = IMAGE_PATH;
            topPlayersTableQueries.allColumns[8] = CATEGORY_NAME;
            topPlayersTableQueries.open();
        }
        return topPlayersTableQueries;
    }

    public void deleteAllTopPlayers() {
        open();
        sqLiteDatabase.delete(TOP_PLAYERS, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertTopPlayers(int categorId, String firstName, String lastName, int userId, int totalMark, int rank, String imagePath, String categoryName) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EN_CATEGORY_ID, categorId);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(USER_ID, userId);
        contentValues.put(TOTAL_MARK, totalMark);
        contentValues.put(RANK, rank);
        contentValues.put(IMAGE_PATH, imagePath);
        contentValues.put(CATEGORY_NAME, categoryName);

        try {
            sqLiteDatabase.insert(TOP_PLAYERS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }
    public ArrayList<TopPlayersSqliteModel> getTopPlayerList(int selectedCategoryId) {
        open();
        ArrayList<TopPlayersSqliteModel> topPlayersSqliteModelArrayList = new ArrayList<>();


        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(TOP_PLAYERS, allColumns, getWhereForCategoryId(selectedCategoryId), null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    TopPlayersSqliteModel topPlayersSqliteModel = cursorToEntity(selected);
                    topPlayersSqliteModelArrayList.add(topPlayersSqliteModel);
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
        return topPlayersSqliteModelArrayList;
    }

    protected TopPlayersSqliteModel cursorToEntity(Cursor cursor) {

        TopPlayersSqliteModel topPlayersSqliteModel = new TopPlayersSqliteModel();
        int category_id;
        int first_name;
        int last_name;
        int user_id;
        int total_mark;
        int rank;
        int image_path;
        int category_name;

        if (cursor != null) {
            if (cursor.getColumnIndex(EN_CATEGORY_ID) != -1) {
                category_id = cursor.getColumnIndexOrThrow(EN_CATEGORY_ID);
                topPlayersSqliteModel.setEn_category_id(cursor.getInt(category_id));
            }
            if (cursor.getColumnIndex(FIRST_NAME) != -1) {
                first_name = cursor.getColumnIndexOrThrow(FIRST_NAME);
                topPlayersSqliteModel.setFirstname(cursor.getString(first_name));
            }
            if (cursor.getColumnIndex(LAST_NAME) != -1) {
                last_name = cursor.getColumnIndexOrThrow(LAST_NAME);
                topPlayersSqliteModel.setLastname(cursor.getString(last_name));
            }
            if (cursor.getColumnIndex(USER_ID) != -1) {
                user_id = cursor.getColumnIndexOrThrow(USER_ID);
                topPlayersSqliteModel.setUser_id(cursor.getInt(user_id));
            }
            if (cursor.getColumnIndex(TOTAL_MARK) != -1) {
                total_mark = cursor.getColumnIndexOrThrow(TOTAL_MARK);
                topPlayersSqliteModel.setTotal_mark(cursor.getInt(total_mark));
            }
            if (cursor.getColumnIndex(RANK) != -1) {
                rank = cursor.getColumnIndexOrThrow(RANK);
                topPlayersSqliteModel.setRank(cursor.getInt(rank));
            }
            if (cursor.getColumnIndex(IMAGE_PATH) != -1) {
                image_path = cursor.getColumnIndexOrThrow(IMAGE_PATH);
                topPlayersSqliteModel.setImage(cursor.getString(image_path));
            }
            if (cursor.getColumnIndex(CATEGORY_NAME) != -1) {
                category_name = cursor.getColumnIndexOrThrow(CATEGORY_NAME);
                topPlayersSqliteModel.setCategory_name(cursor.getString(category_name));
            }

        }
        return topPlayersSqliteModel;
    }

    private String getWhereForCategoryId(int category_id) {
        return EN_CATEGORY_ID + " = '" + category_id + "'";
    }

}
