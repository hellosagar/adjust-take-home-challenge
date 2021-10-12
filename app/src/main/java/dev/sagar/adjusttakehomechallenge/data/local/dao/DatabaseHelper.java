package dev.sagar.adjusttakehomechallenge.data.local.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import dev.sagar.adjusttakehomechallenge.data.local.TimeEntity;
import dev.sagar.adjusttakehomechallenge.util.TimeUtil;

/**
 * This is a DAO(Data Access Object) class which contains all the CRUD operations, creation and upgradation of database with version
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "time_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create time table
        sqLiteDatabase.execSQL(TimeEntity.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Upgrading database
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeEntity.TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public boolean insertTime(int second) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // check if second is exist
        if (isTimeExist(String.valueOf(second))) {
            return false;
        }

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(TimeEntity.COLUMN_SECOND, second);
        values.put(TimeEntity.COLUMN_IS_SYNCED, -1);
        values.put(TimeEntity.COLUMN_CREATED_TIME, TimeUtil.getCurrentTimeInMillisecond());

        // insert row
        long insert = db.insert(TimeEntity.TABLE_NAME, null, values);

        // close db connection
        db.close();

        return insert != -1;
    }

    public TimeEntity getTime(String second){
        // get all unSynced time in descending order query
        String query = "SELECT  * FROM " + TimeEntity.TABLE_NAME + " WHERE " + TimeEntity.COLUMN_SECOND + "=" + second;

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();

        try {
            // prepare time object
            TimeEntity timeEntity = new TimeEntity(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_SECOND)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_IS_SYNCED)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_CREATED_TIME))
            );
            cursor.close();

            return timeEntity;
        } catch (Exception e) {
            cursor.close();

            return null;
        }

    }

    public List<TimeEntity> getAllUnSyncedTime(){
        List<TimeEntity> seconds = new ArrayList<>();


        // get all unSynced time in descending order query
        String query = "SELECT  * FROM " + TimeEntity.TABLE_NAME + " WHERE " + TimeEntity.COLUMN_IS_SYNCED + "=-1"  + " ORDER BY " +
                TimeEntity.COLUMN_CREATED_TIME + " ASC";

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TimeEntity time = new TimeEntity();
                time.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_ID)));
                time.setSecond(cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_SECOND)));
                time.setIsSynced(cursor.getInt(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_IS_SYNCED)));
                time.setCreatedTime(cursor.getLong(cursor.getColumnIndexOrThrow(TimeEntity.COLUMN_CREATED_TIME)));

                seconds.add(time);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return second list
        return seconds;
    }

    public boolean isTimeExist(String second){
        TimeEntity entity = getTime(second);

        return entity != null;
    }

    public boolean isTimeUnSynced(String second){
        TimeEntity entity = getTime(second);

        return entity != null && entity.getIsSynced() != 0 && entity.getIsSynced() != 1;
    }

    public void markSync(String second) {
        TimeEntity entity = getTime(second);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TimeEntity.COLUMN_IS_SYNCED, 1);

        // updating row
        db.update(TimeEntity.TABLE_NAME, values, TimeEntity.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entity.getId())});
    }

    public void markUnSync(String second) {
        TimeEntity entity = getTime(second);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TimeEntity.COLUMN_IS_SYNCED, -1);

        // updating row
        db.update(TimeEntity.TABLE_NAME, values, TimeEntity.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entity.getId())});
    }

    public void markInProgress(String second) {
        TimeEntity entity = getTime(second);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TimeEntity.COLUMN_IS_SYNCED, 0);

        // updating row
        db.update(TimeEntity.TABLE_NAME, values, TimeEntity.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entity.getId())});
    }


}
