package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.ProfileUpdateResponse;
import com.kawika.smart_survey.models.QuickChallengeCheckModel;
import com.kawika.smart_survey.models.QuickChallengeSqliteModel;
import com.kawika.smart_survey.models.SubmitResultsModel;


/*
 * Created by senthil on 22-02-2018.
 */
public class QuickChallengeTableQueries implements TableSchema {
    public static QuickChallengeTableQueries quickChallengeTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static QuickChallengeTableQueries sharedInstance(Context context) {

        if (quickChallengeTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            quickChallengeTableQueries = new QuickChallengeTableQueries();
            quickChallengeTableQueries.allColumns = new String[3];
            quickChallengeTableQueries.allColumns[0] = PRIMARY_ID;
            quickChallengeTableQueries.allColumns[1] = IS_AVAILABLE;
            quickChallengeTableQueries.allColumns[2] = MESSAGE;
            quickChallengeTableQueries.open();
        }
        return quickChallengeTableQueries;
    }

    public void deleteAllQuickChallenge() {
        open();
        sqLiteDatabase.delete(QUICK_CHALLENGE, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertQuickChallenge(int isAvailable, String message) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_AVAILABLE, isAvailable);
        contentValues.put(MESSAGE, message);
        try {
            sqLiteDatabase.insert(QUICK_CHALLENGE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public QuickChallengeSqliteModel getQuickChallenge() {
        open();
        QuickChallengeSqliteModel quickChallengeSqliteModel = new QuickChallengeSqliteModel();

        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(QUICK_CHALLENGE, allColumns, null, null, null, null, null);

            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    quickChallengeSqliteModel = cursorToEntity(selected);
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
        return quickChallengeSqliteModel;
    }
    protected QuickChallengeSqliteModel cursorToEntity(Cursor cursor) {

        QuickChallengeSqliteModel quickChallengeSqliteModel = new QuickChallengeSqliteModel();
        int is_available;
        int message;

        if (cursor != null) {
            if (cursor.getColumnIndex(IS_AVAILABLE) != -1) {
                is_available = cursor.getColumnIndexOrThrow(IS_AVAILABLE);
                quickChallengeSqliteModel.setIs_available(cursor.getInt(is_available));
            }
            if (cursor.getColumnIndex(MESSAGE) != -1) {
                message = cursor.getColumnIndexOrThrow(MESSAGE);
                quickChallengeSqliteModel.setMessage(cursor.getString(message));
            }

        }
        return quickChallengeSqliteModel;
    }


}
