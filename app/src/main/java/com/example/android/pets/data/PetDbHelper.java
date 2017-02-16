package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adul on 10/19/16.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    // Setting Nama Database file
    private static final String DATABASE_NAME = "shelter.db";
    //Setting Database Version, bila merubah schema maka kamu harus update Database Version
    private static final int DATABASE_VERSION = 1;

    // Constractor Database
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method ini akan dipanggil ketika database dibuat pertama kali
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Buat String constant SQL statement untuk membuat table pets
        String CREATE_PETS_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Menjalankan SQL statement
        sqLiteDatabase.execSQL(CREATE_PETS_TABLE);
    }

    // Method ini dipanggil ketika database perlu dirubah/diedit
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Database sekarang masih Versi 1, jadi tidak ada code yang perlu dijalankan disini
    }
}
