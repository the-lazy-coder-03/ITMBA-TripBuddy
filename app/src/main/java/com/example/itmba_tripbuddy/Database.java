package com.example.itmba_tripbuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class  Database extends SQLiteOpenHelper {

    private static final String DBName="TripInfo";
    private static final int DBVersion=1;
    private static final String TableName="TRIPINFO";
    private static final String KeyID="id";
    private static final String Email="Email";
    private static final String Password="Password";
    private static final String Destination="Destination";
    private static final String Notes="Notes";
    private static final String Type="SpinnerInput";//spinner input
    private static final String CustomCost="TravelCost";//add travel cost
    private static final String TripCounter="TripCounter";



    public Database(@Nullable Context context ) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TableName);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
