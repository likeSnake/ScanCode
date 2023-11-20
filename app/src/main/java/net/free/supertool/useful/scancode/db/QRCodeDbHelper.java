package net.free.supertool.useful.scancode.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class QRCodeDbHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper sql;
    private final static String name = "QRCodeConfig.db";

    public static SQLiteOpenHelper getInstance(Context context){
        if(sql == null){
            sql = new QRCodeDbHelper(context, name, null, 1);
        }
        return sql;
    }
    public QRCodeDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public QRCodeDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public QRCodeDbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table QRCodeConfig(_id integer primary key autoincrement,code_image BLOB,code_icon BLOB,code_content text,code_format text,code_time text,code_type text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
