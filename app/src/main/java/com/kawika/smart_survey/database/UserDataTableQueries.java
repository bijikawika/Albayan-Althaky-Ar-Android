package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.ProfileUpdateResponse;
import com.kawika.smart_survey.models.SubmitResultsModel;


/*
 * Created by senthil on 22-02-2018.
 */
public class UserDataTableQueries implements TableSchema {
    public static UserDataTableQueries user_logintable_queries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static UserDataTableQueries sharedInstance(Context context) {

        if (user_logintable_queries == null) {
            dbHelper = new DatabaseHelper(context);
            user_logintable_queries = new UserDataTableQueries();
            user_logintable_queries.allColumns = new String[12];
            user_logintable_queries.allColumns[0] = PRIMARY_ID;
            user_logintable_queries.allColumns[1] = DEPARTMENT_ID;
            user_logintable_queries.allColumns[2] = DEPARTMENT_NAME;
            user_logintable_queries.allColumns[3] = DEVICE_ID;
            user_logintable_queries.allColumns[4] = EMAIL;
            user_logintable_queries.allColumns[5] = FIRST_NAME;
            user_logintable_queries.allColumns[6] = LANGUAGE_ID;
            user_logintable_queries.allColumns[7] = LAST_NAME;
            user_logintable_queries.allColumns[8] = PHONE;
            user_logintable_queries.allColumns[9] = TOTAL_MARK;
            user_logintable_queries.allColumns[10] = ID;
            user_logintable_queries.allColumns[11] = IMAGE;
            user_logintable_queries.open();
        }
        return user_logintable_queries;
    }

    public void deleteAllUserDetails() {
        open();
        sqLiteDatabase.delete(USER_DETAILS, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertUserDetails(AuthenticationModel authenticationModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEPARTMENT_ID, authenticationModel.getData().getDepartment_id());
        contentValues.put(DEPARTMENT_NAME, authenticationModel.getData().getDepartment());
        contentValues.put(DEVICE_ID, authenticationModel.getData().getDevice_id());
        contentValues.put(EMAIL, authenticationModel.getData().getEmail());
        contentValues.put(FIRST_NAME, authenticationModel.getData().getFirstname());
        contentValues.put(LANGUAGE_ID, authenticationModel.getData().getLanguage_id());
        contentValues.put(LAST_NAME, authenticationModel.getData().getLastname());
        contentValues.put(PHONE, authenticationModel.getData().getPhone());
        contentValues.put(TOTAL_MARK, authenticationModel.getData().getUser_mark());
        contentValues.put(ID, authenticationModel.getData().getId());
        contentValues.put(IMAGE, authenticationModel.getData().getImage_path());

        try {
            sqLiteDatabase.insert(USER_DETAILS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public boolean insertDetails(int department_id, String department, String device_id, String email,
                                 String firstname, int language_id, String lastname, String phone, int user_mark,
                                 int id, String image_path) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEPARTMENT_ID, department_id);
        contentValues.put(DEPARTMENT_NAME, department);
        contentValues.put(DEVICE_ID, device_id);
        contentValues.put(EMAIL, email);
        contentValues.put(FIRST_NAME, firstname);
        contentValues.put(LANGUAGE_ID, language_id);
        contentValues.put(LAST_NAME, lastname);
        contentValues.put(PHONE, phone);
        contentValues.put(TOTAL_MARK, user_mark);
        contentValues.put(ID, id);
        contentValues.put(IMAGE, image_path);

        try {
            sqLiteDatabase.insert(USER_DETAILS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public boolean insertUserDetailsOnQuizComplete(SubmitResultsModel authenticationModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEPARTMENT_ID, authenticationModel.getData().getDepartment_id());
        contentValues.put(DEPARTMENT_NAME, authenticationModel.getData().getDepartment());
        contentValues.put(DEVICE_ID, authenticationModel.getData().getDevice_id());
        contentValues.put(EMAIL, authenticationModel.getData().getEmail());
        contentValues.put(FIRST_NAME, authenticationModel.getData().getFirstname());
        contentValues.put(LANGUAGE_ID, authenticationModel.getData().getLanguage_id());
        contentValues.put(LAST_NAME, authenticationModel.getData().getLastname());
        contentValues.put(PHONE, authenticationModel.getData().getPhone());
        contentValues.put(TOTAL_MARK, authenticationModel.getData().getUser_mark());
        contentValues.put(ID, authenticationModel.getData().getId());
        contentValues.put(IMAGE, authenticationModel.getData().getImage_path());

        try {
            sqLiteDatabase.insert(USER_DETAILS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    /**
     * update
     *
     * @param profileUpdateResponse dto
     * @param lastName last name
     * @param firstName last name
     * @return result in boolean
     */
    public boolean updateFirstnameAndLastName(String userId, ProfileUpdateResponse profileUpdateResponse,
                                              String lastName, String firstName) {
        open();
        long updateId = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(IMAGE, profileUpdateResponse.getData().getImage_path());
        try {
            updateId = sqLiteDatabase.update(USER_DETAILS, contentValues,
                    getIdCondition(userId), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateId > 0;
    }

    public boolean updateMark(SubmitResultsModel.DataBean profileUpdateResponse) {
        open();
        long updateId = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_MARK, profileUpdateResponse.getUser_mark());
        try {
            updateId = sqLiteDatabase.update(USER_DETAILS, contentValues,
                    getIdCondition(String.valueOf(profileUpdateResponse.getId())), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateId > 0;
    }

    public String getName() {
        open();
        String name = "";
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(USER_DETAILS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                for (int i = 0; i < selected.getCount(); i++) {
                    name = selected.getString(5);
                    selected.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }
        return name;
    }

    public String getLastName() {
        open();
        String name = "";
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(USER_DETAILS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                for (int i = 0; i < selected.getCount(); i++) {
                    name = selected.getString(7);
                    selected.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }
        return name;
    }

    public String getDepartment() {
        open();
        String department = "";
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(USER_DETAILS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                for (int i = 0; i < selected.getCount(); i++) {
                    department = selected.getString(2);
                    selected.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }
        return department;
    }

    public String getProfileImagePath() {
        open();
        String profileImage = "";
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(USER_DETAILS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                for (int i = 0; i < selected.getCount(); i++) {
                    profileImage = selected.getString(11);
                    selected.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }

        System.out.println("profileImage = " + profileImage);
        return profileImage;
    }

    public String getMyScore() {
        open();
        String my_score = "";
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(USER_DETAILS, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                for (int i = 0; i < selected.getCount(); i++) {
                    my_score = selected.getString(9);
                    selected.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selected != null) {
                selected.close();
            }
        }
        return my_score;
    }

    //Id condition
    private String getIdCondition(String id) {
        return ID + " = '" + id + "'";
    }


}
