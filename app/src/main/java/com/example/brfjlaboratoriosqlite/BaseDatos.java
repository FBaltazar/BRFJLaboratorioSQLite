package com.example.brfjlaboratoriosqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper
{

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE REGISTROALUMNOS(ID INTEGER PRIMARY KEY, NOMBRE VARCHAR(200), CELULAR VARCHAR(50), CORREO VARCHAR(100), CARRERA VARCHAR(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
