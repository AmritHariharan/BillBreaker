package com.billbreaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ReceiptDatabase extends SQLiteOpenHelper {
    private static final String RECEIPT_TABLE_NAME = "RECEIPT";

    ReceiptDatabase(Context context) {
        super(context, "BillBreaker.db", null, 1);
    }

    /** Called implicitly the first time the database is created. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "
                        + RECEIPT_TABLE_NAME
                        + "("
                        + "RECEIPT BLOB"
                        + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException();
    }

    public void putReceipt(List<PersonalReceiptItem> receipt) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(receipt);
            byte[] bytes = bos.toByteArray();
            values.put("RECEIPT", bytes);
            db.insertOrThrow(RECEIPT_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("TEST", e.getMessage());
            // TODO error
        } finally {
            db.endTransaction();
        }
    }
}
