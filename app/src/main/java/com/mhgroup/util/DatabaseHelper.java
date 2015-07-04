package com.mhgroup.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableLayout;

import com.mhgroup.bean.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DK Wang on 2015/3/31.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MobileHandset.db";

    private static final String TABLE_NAME = "Records";

    private static final String[] COLUMNS = new String[]{
            "id",
            "first_word",
            "whole_phrase",
            "changed_phrase",
            "length",               // length of the original sentence.
            "frequency",
    };


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_NAME + "(" + COLUMNS[0]
                + " integer primary key autoincrement, " + COLUMNS[1]
                + " text not null, " + COLUMNS[2]
                + " text not null, " + COLUMNS[3]
                + " text not null, " + COLUMNS[4]
                + " integer not null, " + COLUMNS[5]
                + " integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addRecord(Record record) {
        int count = countRecords(record.getWholePhrase(), record.getChangedPhrase());
        if(count != 0)
        {
            String updateString = "UPDATE " + TABLE_NAME + " SET " + COLUMNS[5] + " = " + COLUMNS[5] + "+1 WHERE " +
                    COLUMNS[2] + " = ? AND " + COLUMNS[3] + " = ?";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(updateString, new String[]{record.getWholePhrase(), record.getChangedPhrase()});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMNS[1], record.getFirstWord());
            values.put(COLUMNS[2], record.getWholePhrase());
            values.put(COLUMNS[3], record.getChangedPhrase());
            values.put(COLUMNS[4], record.getLength());
            values.put(COLUMNS[5], 1);
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
    }

    public List<Record> queryRecords(String firstWord)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMNS[1] + " =? AND " +
                COLUMNS[5] + " > 1 ORDER BY " +
                COLUMNS[4] + " DESC",
                new String[]{ firstWord });
        List<Record> records = new ArrayList<Record>();
        while(cursor.moveToNext())
        {
            Record record = new Record(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getInt(4), cursor.getInt(5));
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    public int countRecords(String wholePhrase, String changedPhrase) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + TABLE_NAME + " WHERE " +
                        COLUMNS[2] + "=? AND " +
                        COLUMNS[3] + "=?",
                new String[]{wholePhrase, changedPhrase});
        if (null != cursor)
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();
        db.close();
        return count;
    }

}
