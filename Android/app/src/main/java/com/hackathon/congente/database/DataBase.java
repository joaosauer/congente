package com.hackathon.congente.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.facebook.login.LoginManager;
import com.hackathon.congente.datatype.User;

public class DataBase extends SQLiteOpenHelper {
    //1 - Initial version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "congente.db";

    //Tables
    private static final String TABLE_SETTINGS = "settings";

    //Player data
    public static final String SETTINGS_ID = "id";
    public static final String SETTINGS_NOME = "usuario";
    public static final String SETTINGS_SENHA = "senha";
    public static final String SETTINGS_TOKEN = "token";
    private static final String SETTINGS_CREATE = "CREATE TABLE "
            + TABLE_SETTINGS + " ( "
            + SETTINGS_ID + " integer primary key autoincrement, "
            + SETTINGS_NOME + " text UNIQUE not null, "
            + SETTINGS_SENHA + " text,"
            + SETTINGS_TOKEN + " text);";

    private static DataBase instance = null;

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SETTINGS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    public static  DataBase getDataBase(Context context) {
        if(instance == null) {
            instance = new DataBase(context);
        }
        return instance;
    }

    public void deleteUserData() {
        SQLiteDatabase sqlLite = getReadableDatabase();
        sqlLite.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(sqlLite);
        LoginManager.getInstance().logOut();
    }

    public User getUserData() {
        User user = new User();

        SQLiteDatabase sqlLite = getReadableDatabase();

        Cursor cursor = sqlLite.query(TABLE_SETTINGS, new String[]{SETTINGS_NOME, SETTINGS_SENHA, SETTINGS_TOKEN}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user.usuario = cursor.getString(cursor.getColumnIndex(SETTINGS_NOME));
            user.senha = cursor.getString(cursor.getColumnIndex(SETTINGS_SENHA));
            user.fb_token = cursor.getString(cursor.getColumnIndex(SETTINGS_TOKEN));
        }

        if (cursor != null)
            cursor.close();

        return user;
    }

    public boolean saveUserData(User usuario) {
        boolean result = false;

        SQLiteDatabase sqlLite = getReadableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(SETTINGS_NOME, usuario.usuario);
        initialValues.put(SETTINGS_SENHA, usuario.senha);
        initialValues.put(SETTINGS_TOKEN, usuario.fb_token);
        if(sqlLite.insert(TABLE_SETTINGS, null, initialValues) > 0) {
            result = true;
        }

        return result;
    }
}
