package com.example.masknotifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.widget.Toast;

import com.example.masknotifier.model.HistoryData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "MaskNotifierDB";
    public static final String TABLE_NAME = "history";
    public static final String HISTORY_ID_COLUMN = "id";
    public static final String HISTORY_REPLY_COLUMN = "reply";
    public static final String HISTORY_TIMESTAMP_COLUMN = "timestamp";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table history" +
                "(id integer primary key autoincrement, reply text, timestamp text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS history");
        onCreate(sqLiteDatabase);
    }

    public boolean insertHistory(String reply, String timestamp) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_REPLY_COLUMN, reply);
        contentValues.put(HISTORY_TIMESTAMP_COLUMN, timestamp);
        long x = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        Toast.makeText(context, "Data Added! " + x + " " + reply, Toast.LENGTH_SHORT).show();
        return true;
    }

    public Integer deleteLast() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT MAX(_id) FROM history", null);
        return deleteHistory(res.getInt(res.getColumnIndex(HISTORY_ID_COLUMN)));
    }


    public Integer deleteHistory(String timestamp) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("history", "timestamp = ?", new String[] { timestamp});
    }

    public Integer deleteHistory(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("history", "id = ?", new String[] { Integer.toString(id)});
    }

    public Cursor getData(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from history where id=" + id + "", null);
        return(res);
    }

    public List<HistoryData> getHistory() {

        List<HistoryData> arrayList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from history", null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            HistoryData historyData = new HistoryData();
            historyData.setReply(res.getString(res.getColumnIndex(HISTORY_REPLY_COLUMN)));
            historyData.setTimeStamp(res.getString(res.getColumnIndex(HISTORY_TIMESTAMP_COLUMN)));
            arrayList.add(historyData);
            res.moveToNext();
        }

        Collections.reverse(arrayList);

        Toast.makeText(context, "size in helper " + arrayList.size(), Toast.LENGTH_SHORT).show();
        return arrayList;
    }

}
