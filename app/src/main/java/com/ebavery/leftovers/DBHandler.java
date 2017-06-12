package com.ebavery.leftovers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elizabeth on 4/18/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "leftovers";
    private static final String TABLE_NAME = "leftoverDetails";
    private static final String KEY_NUMBER = "itemNumber";
    private static final String KEY_DESC = "description";
    private static final String KEY_EXP_DATE = "expDate";

    public DBHandler(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DESC + " TEXT, "
                + KEY_EXP_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addLeftover(String desc, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues leftoverContent = new ContentValues();
        leftoverContent.put(KEY_DESC, desc);
        leftoverContent.put(KEY_EXP_DATE, date);

        long result = db.insert(TABLE_NAME, null, leftoverContent);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public String getKeyDesc(){
        return KEY_DESC;
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteLeftover(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_NUMBER + " = " + id;
        db.execSQL(query);
    }

    public Cursor getItemId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_NUMBER + " FROM " + TABLE_NAME + " WHERE " + KEY_DESC + " = " + "'" + name + "'";
        Cursor data = db.rawQuery(query, null);
        //db.close();
        return data;
    }
}
