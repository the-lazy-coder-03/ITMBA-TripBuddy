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
    private static final String Email="id";
    private static final String Password="id";
    private static final String Destination="id";
    private static final String Notes="id";
    private static final String Type="id";//spinner input
    private static final String CustomCost="id";//add travel cost
    private static final String TripCounter="id";



    public Database(@Nullable Context context ) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TableName);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
