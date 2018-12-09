package com.blacksugar.mytodolist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EDMTDev";
    private static final int DB_VER = 1;
    public static final String TABLE = "Task";
    public static final String KEY_ID = "ID";
    public static final String KEY_TASKNAME = "TaskName";
    public static final String KEY_COMPLETED = "IsCompleted";

    public DbHelper (Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s TEXT NOT NULL," +
                        " %s INTEGER NOT NULL);",
                TABLE, KEY_ID, KEY_TASKNAME, KEY_COMPLETED);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task);
        values.put(KEY_COMPLETED, 0);
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteTask (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, KEY_TASKNAME + " = ?", new String[] {task});
        db.close();
    }

    public void updateTask (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> idList = new ArrayList<>();

        Cursor cursor = db.query(TABLE, new String[]{KEY_COMPLETED}, KEY_TASKNAME + " = ?", new String[]{task}, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(KEY_COMPLETED);
            idList.add(cursor.getInt(index));
        }

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task);

        if(idList.get(0) == 0) {
            values.put(KEY_COMPLETED, 1);
        } else {
            values.put(KEY_COMPLETED, 0);
        }

        db.update(TABLE, values,KEY_TASKNAME + " = ?", new String[] {task});
        db.close();
    }

    public void markAsComplete (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task);
        values.put(KEY_COMPLETED, 1);

        db.update(TABLE, values,KEY_TASKNAME + " = ?", new String[] {task});
        db.close();
    }

    public void markAsIncomplete (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task);
        values.put(KEY_COMPLETED, 0);

        db.update(TABLE, values,KEY_TASKNAME + " = ?", new String[] {task});
        db.close();
    }

    public ArrayList<String> getIncompleteTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = KEY_COMPLETED + "=0";

        Cursor cursor = db.query(TABLE, new String[]{KEY_TASKNAME}, whereClause, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(KEY_TASKNAME);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public ArrayList<String> getCompleteTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = KEY_COMPLETED + "=1";

        Cursor cursor = db.query(TABLE, new String[]{KEY_TASKNAME}, whereClause, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(KEY_TASKNAME);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

}
