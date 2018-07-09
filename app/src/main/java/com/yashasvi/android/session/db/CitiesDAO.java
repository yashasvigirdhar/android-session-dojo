package com.yashasvi.android.session.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.yashasvi.android.session.models.City;

import java.util.ArrayList;
import java.util.List;

public class CitiesDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_DESCRIPTION};

    public CitiesDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public City addCity(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_DESCRIPTION, description);
        long insertId = database.insert(MySQLiteHelper.TABLE_CITIES, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CITIES, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        City contact1 = cursorToContact(cursor);
        cursor.close();
        return contact1;
    }

    public void deleteContact(City contact) {
        long id = contact.getId();
        database.delete(MySQLiteHelper.TABLE_CITIES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        System.out.println("Comment deleted with id: " + id);

    }

    public List<City> getAllCities() {
        List<City> contacts = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CITIES, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City contact = cursorToContact(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    private City cursorToContact(Cursor cursor) {
        return new City(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }
}