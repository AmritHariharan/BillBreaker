package com.billbreaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Database to hold personal receipt items per transaction. Each transaction will have a list of
 * personal receipt items. Currently, data will be deleted every 90 days.
 */
class ReceiptDatabase extends SQLiteOpenHelper {
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
                        + ", TIMESTAMP BIGINT PRIMARY KEY"
                        + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException();
    }

    /**
     * Adds a receipt to the database by storing the list of personal receipt items. The timestamp
     * is added here in order to keep track of auto deletion after it expires as well as display the
     * date to the user
     */
    void putReceipt(List<PersonalReceiptItem> receipt) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            byte[] receiptBytes = serializeReceipt(receipt);
            long timestamp = getTimestamp();

            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("RECEIPT", receiptBytes);
            values.put("TIMESTAMP", timestamp);
            db.insertOrThrow(RECEIPT_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieves one receipt with the timestamp as the primary key
     */
    Receipt getReceipt(long timestamp) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = "TIMESTAMP = ?";
        String[] selectionArgs = {
                String.valueOf(timestamp)
        };

        Cursor cursor =
                db.query(RECEIPT_TABLE_NAME, // The table to query
                        null, // // The values for the WHERE clause
                        selection, // The columns for the WHERE clause
                        selectionArgs, // The values for the WHERE clause
                        null, // don't group the rows
                        null, // don't filter by row groups
                        null // The sort order (default ASC)
                );
        try {
            if (cursor.moveToNext()) {
                byte[] personalReceiptItem = cursor.getBlob(0);
                List<PersonalReceiptItem> personalReceiptItems = deserializeReceipt(personalReceiptItem);
                long receiptTimestamp = cursor.getLong(1);
                return new Receipt(personalReceiptItems, receiptTimestamp);
            } else {
                throw new RuntimeException("Database should not have two results for a primary key");
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Retrieves all receipts for the user
     */
    List<Receipt> getAllReceipts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =
                db.query(RECEIPT_TABLE_NAME, // The table to query
                        null, // The array of columns to return (pass null to get all)
                        null, // The columns for the WHERE clause
                        null, // The values for the WHERE clause
                        null, // don't group the rows
                        null, // don't filter by row groups
                        null // The sort order (default ASC)
                );
        try {
            List<Receipt> receipts = new ArrayList<>();
            while (cursor.moveToNext()) {
                byte[] personalReceiptItem = cursor.getBlob(0);
                List<PersonalReceiptItem> personalReceiptItems = deserializeReceipt(personalReceiptItem);
                long timestamp = cursor.getLong(1);
                receipts.add(new Receipt(personalReceiptItems, timestamp));
            }
            return receipts;
        } finally {
            cursor.close();
        }
    }

    /** Deletes all entries from the database that is > 90 days old */
    void deleteOldEntries() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();

            String whereClause = "TIMESTAMP < ?";
            String[] whereArgs = {String.valueOf(getTimestamp90DaysAgo())};

            db.delete(RECEIPT_TABLE_NAME, whereClause, whereArgs);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieve timestamp 90 days ago to be used for deletion
     */
    private long getTimestamp90DaysAgo() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.DATE, -90);
        return calendar.getTimeInMillis();
    }

    /**
     * Retrieve timestamp for the current time
     */
    private long getTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis();
    }

    /**
     * Serialize the receipt to be a byte array as SQLite blobs only takes byte arrays
     */
    private byte[] serializeReceipt(List<PersonalReceiptItem> receipt) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(receipt);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deserialize the byte array back into a list of personal receipt items
     */
    private List<PersonalReceiptItem> deserializeReceipt(byte[] receiptBytes) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiptBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (List<PersonalReceiptItem>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
