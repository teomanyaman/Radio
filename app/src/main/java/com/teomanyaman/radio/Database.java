package com.teomanyaman.radio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String DBNAME = "MyRadios.sqlite";
    public static final String DBLOCATION = "/data/data/com.teomanyaman.radio/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Database(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<RadioDao> getRadios() {
        RadioDao product = null;
        List<RadioDao> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM MyRadios", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new RadioDao(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public List<RadioDao> getFavourites() {
        RadioDao favourite;
        List<RadioDao> favorList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM MyRadios WHERE Favourite = 2", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            favourite = new RadioDao(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
            favorList.add(favourite);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return favorList;
    }

    public long updateFavourite(RadioDao radioDao) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", radioDao.getName());
        contentValues.put("URL", radioDao.getURL());
        contentValues.put("Favourite", 2);
        contentValues.put("Icon", radioDao.getIcon());
        String[] whereArgs = {Integer.toString(radioDao.getId())};
        openDatabase();
        long returnValue = mDatabase.update("MyRadios",contentValues, "id=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long deleteFavourite(RadioDao radioDao) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", radioDao.getName());
        contentValues.put("URL", radioDao.getURL());
        contentValues.put("Favourite", 1);
        contentValues.put("Icon", radioDao.getIcon());
        String[] whereArgs = {Integer.toString(radioDao.getId())};
        openDatabase();
        long returnValue = mDatabase.update("MyRadios",contentValues, "id=?", whereArgs);
        closeDatabase();
        return returnValue;
    }
}

