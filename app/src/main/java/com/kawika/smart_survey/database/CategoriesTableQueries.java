package com.kawika.smart_survey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.ScoresSqliteModel;

import java.util.ArrayList;
import java.util.List;


/*
 * Created by senthil on 22-02-2018.
 */
public class CategoriesTableQueries implements TableSchema {
    public static CategoriesTableQueries categoriesTableQueries;
    public static DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String allColumns[];

    public static CategoriesTableQueries sharedInstance(Context context) {

        if (categoriesTableQueries == null) {
            dbHelper = new DatabaseHelper(context);
            categoriesTableQueries = new CategoriesTableQueries();
            categoriesTableQueries.allColumns = new String[9];
            categoriesTableQueries.allColumns[0] = PRIMARY_ID;
            categoriesTableQueries.allColumns[1] = EN_CATEGORY_ID;
            categoriesTableQueries.allColumns[2] = CATEGORY_COLOR;
            categoriesTableQueries.allColumns[3] = CATEGORY_NAME;
            categoriesTableQueries.allColumns[4] = CROWN;
            categoriesTableQueries.allColumns[5] = CROWN_IMAGE;
            categoriesTableQueries.allColumns[6] = LEVEL_COUNT;
            categoriesTableQueries.allColumns[7] = IMAGE_PATH;
            categoriesTableQueries.allColumns[8] = IS_FOLLOWED;
            categoriesTableQueries.open();
        }
        return categoriesTableQueries;
    }

    public void deleteAllCategories() {
        open();
        sqLiteDatabase.delete(CATEGORIES, null, null);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public boolean insertAllTopics(CategoriesSqliteModel categoriesSqliteModel) {

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EN_CATEGORY_ID, categoriesSqliteModel.getEn_category_id());
        contentValues.put(CATEGORY_COLOR, categoriesSqliteModel.getCategory_color());
        contentValues.put(CATEGORY_NAME, categoriesSqliteModel.getCategory_name());
        contentValues.put(CROWN, categoriesSqliteModel.getCrown());
        contentValues.put(CROWN_IMAGE, categoriesSqliteModel.getCrown_image());
        contentValues.put(LEVEL_COUNT, categoriesSqliteModel.getLevel_count());
        contentValues.put(IS_FOLLOWED, categoriesSqliteModel.getIs_followed());
        contentValues.put(IMAGE_PATH, categoriesSqliteModel.getImage_path());

        try {
            sqLiteDatabase.insert(CATEGORIES, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                close();
            }
        }

        return true;
    }

    public boolean updateToFollowedTopic(CategoriesSqliteModel categoriesSqliteModel) {
        open();
        long updateId = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(EN_CATEGORY_ID, categoriesSqliteModel.getEn_category_id());
        contentValues.put(CATEGORY_COLOR, categoriesSqliteModel.getCategory_color());
        contentValues.put(CATEGORY_NAME, categoriesSqliteModel.getCategory_name());
        contentValues.put(CROWN, categoriesSqliteModel.getCrown());
        contentValues.put(CROWN_IMAGE, categoriesSqliteModel.getCrown_image());
        contentValues.put(LEVEL_COUNT, categoriesSqliteModel.getLevel_count());
        contentValues.put(IS_FOLLOWED, categoriesSqliteModel.getIs_followed());
        contentValues.put(IMAGE_PATH, categoriesSqliteModel.getImage_path());
        try {
            updateId = sqLiteDatabase.update(CATEGORIES, contentValues,
                    getWhereForCategoryId(categoriesSqliteModel.getEn_category_id()), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateId > 0;
    }

    public ArrayList<CategoriesSqliteModel> getFollowedTopics() {
        open();
        ArrayList<CategoriesSqliteModel> categoriesSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CATEGORIES, allColumns, getWhereForFollowedTopics(), null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CategoriesSqliteModel categoriesSqliteModel = cursorToEntity(selected);
                    categoriesSqliteModelArrayList.add(categoriesSqliteModel);
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

    public ArrayList<CategoriesSqliteModel> getLatestAcheivments() {
        open();
        ArrayList<CategoriesSqliteModel> categoriesSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CATEGORIES, allColumns, getWhereForLatestAcheivments(), null, null, null, getOderBY());
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CategoriesSqliteModel categoriesSqliteModel = cursorToEntity(selected);
                    categoriesSqliteModelArrayList.add(categoriesSqliteModel);
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

    public int updateSelectedStatus(int isFollowValue) {
        int noOfColumnUpdated = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(IS_FOLLOWED, isFollowValue);

            noOfColumnUpdated = sqLiteDatabase.update(CATEGORIES, initialValues, null,
                    null);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
        }
        return noOfColumnUpdated;
    }

    public ArrayList<CategoriesSqliteModel> getAllTopics() {
        open();
        ArrayList<CategoriesSqliteModel> categoriesSqliteModelArrayList = new ArrayList<>();
        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CATEGORIES, allColumns, null, null, null, null, null);
            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    CategoriesSqliteModel categoriesSqliteModel = cursorToEntity(selected);
                    categoriesSqliteModelArrayList.add(categoriesSqliteModel);
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

    protected CategoriesSqliteModel cursorToEntity(Cursor cursor) {

        CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
        int en_category_id;
        int category_color;
        int category_name;
        int crown;
        int crown_image;
        int level_count;
        int is_followed;
        int image_path;

        if (cursor != null) {
            if (cursor.getColumnIndex(EN_CATEGORY_ID) != -1) {
                en_category_id = cursor.getColumnIndexOrThrow(EN_CATEGORY_ID);
                categoriesSqliteModel.setEn_category_id(cursor.getInt(en_category_id));
            }
            if (cursor.getColumnIndex(CATEGORY_COLOR) != -1) {
                category_color = cursor.getColumnIndexOrThrow(CATEGORY_COLOR);
                categoriesSqliteModel.setCategory_color(cursor.getString(category_color));
            }
            if (cursor.getColumnIndex(CATEGORY_NAME) != -1) {
                category_name = cursor.getColumnIndexOrThrow(CATEGORY_NAME);
                categoriesSqliteModel.setCategory_name(cursor.getString(category_name));
            }
            if (cursor.getColumnIndex(IS_FOLLOWED) != -1) {
                is_followed = cursor.getColumnIndexOrThrow(IS_FOLLOWED);
                categoriesSqliteModel.setIs_followed(cursor.getInt(is_followed));
            }
            if (cursor.getColumnIndex(CROWN) != -1) {
                crown = cursor.getColumnIndexOrThrow(CROWN);
                categoriesSqliteModel.setCrown(cursor.getString(crown));
            }
            if (cursor.getColumnIndex(CROWN_IMAGE) != -1) {
                crown_image = cursor.getColumnIndexOrThrow(CROWN_IMAGE);
                categoriesSqliteModel.setCrown_image(cursor.getString(crown_image));
            }
            if (cursor.getColumnIndex(LEVEL_COUNT) != -1) {
                level_count = cursor.getColumnIndexOrThrow(LEVEL_COUNT);
                categoriesSqliteModel.setLevel_count(cursor.getInt(level_count));
            }
            if (cursor.getColumnIndex(IMAGE_PATH) != -1) {
                image_path = cursor.getColumnIndexOrThrow(IMAGE_PATH);
                categoriesSqliteModel.setImage_path(cursor.getString(image_path));
            }

        }
        return categoriesSqliteModel;
    }

    public CategoriesSqliteModel getSelectedCategoryBasedById(int selectedCategoryId) {
        open();
        CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();

        Cursor selected = null;
        try {
            selected = sqLiteDatabase.query(CATEGORIES, allColumns, getWhereForCategoryId(selectedCategoryId), null, null, null, null);

            if (selected != null && selected.getCount() > 0) {
                selected.moveToFirst();
                while (!selected.isAfterLast()) {
                    categoriesSqliteModel = cursorToEntity(selected);
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
        return categoriesSqliteModel;
    }



    private String getWhereForCategoryId(int category_id) {
        return EN_CATEGORY_ID + " = '" + category_id + "'";
    }

    private String getWhereForLatestAcheivments() {
        return IS_FOLLOWED +  " = '" + 1 + "'"+" AND " +LEVEL_COUNT + "!='" + 0 + "'";
    }

    private String getWhereForFollowedTopics() {
        return IS_FOLLOWED + " = '" + 1 + "'";
    }

    private String getOderBY() {
        return LEVEL_COUNT + " DESC ";
    }

}
