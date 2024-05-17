package com.example.diaryapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.diaryapp.Models.DiaryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DiaryDBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "DiaryApp";
    private static final int DB_VERSION = 7;
    private static final String DIARY_TABLE = "diary";
    private static final String ENTRY_ID = "entry_id";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String IMAGE_URIS = "image_uris";

    public DiaryDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DIARY_TABLE_CREATE_QUERY = "CREATE TABLE " + DIARY_TABLE + " (" +
                ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                CONTENT + " TEXT, " +
                DATE + " TEXT, " +
                IMAGE_URIS + " TEXT" +
                ");";
        db.execSQL(DIARY_TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DIARY_TABLE);
        onCreate(db);
    }

    public void addDiaryEntry(DiaryEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, entry.getTitle());
        values.put(CONTENT, entry.getContent());
        values.put(DATE, entry.getDate());
        values.put(IMAGE_URIS, uriListToString(entry.getImageUris()));
        db.insert(DIARY_TABLE, null, values);
        db.close();
    }

    public List<DiaryEntry> getAllDiaryEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DIARY_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                DiaryEntry entry = new DiaryEntry();
                entry.setEntry_id(cursor.getInt(cursor.getColumnIndex(ENTRY_ID)));
                entry.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                entry.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                entry.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                entry.setImageUris(stringToUriList(cursor.getString(cursor.getColumnIndex(IMAGE_URIS))));
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Collections.reverse(entries);
        return entries;
    }

    public DiaryEntry getDiaryEntry(int entry_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DIARY_TABLE, null, ENTRY_ID + "=?", new String[]{String.valueOf(entry_id)}, null, null, null);
        DiaryEntry entry = null;
        if (cursor != null && cursor.moveToFirst()) {
            entry = new DiaryEntry();
            entry.setEntry_id(cursor.getInt(cursor.getColumnIndex(ENTRY_ID)));
            entry.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            entry.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            entry.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            entry.setImageUris(stringToUriList(cursor.getString(cursor.getColumnIndex(IMAGE_URIS))));
            cursor.close();
        }
        db.close();
        return entry;
    }

    public int updateDiaryEntry(DiaryEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, entry.getTitle());
        values.put(CONTENT, entry.getContent());
        values.put(DATE, entry.getDate());
        values.put(IMAGE_URIS, uriListToString(entry.getImageUris()));
        return db.update(DIARY_TABLE, values, ENTRY_ID + "=?", new String[]{String.valueOf(entry.getEntry_id())});
    }

    public void deleteDiaryEntry(int entry_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DIARY_TABLE, ENTRY_ID + "=?", new String[]{String.valueOf(entry_id)});
        db.close();
    }

    private String uriListToString(List<Uri> uris) {
        StringBuilder sb = new StringBuilder();
        for (Uri uri : uris) {
            sb.append(uri.toString()).append(",");
        }
        return sb.toString();
    }

    private List<Uri> stringToUriList(String uriString) {
        List<Uri> uris = new ArrayList<>();
        if (uriString != null && !uriString.isEmpty()) {
            String[] uriArray = uriString.split(",");
            for (String uri : uriArray) {
                uris.add(Uri.parse(uri));
            }
        }
        return uris;
    }
}
