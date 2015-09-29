package com.mairos.ymoneytransfer.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mairos.ymoneytransfer.storage.models.PaymentInfo;

import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class YmoneySqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_FILE = "data.db";
    private static final int DB_VERSION = 1;

    static {

        // Register the factory and set the instance as the global Cupboard instance
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());

        // register our models
        cupboard().register(PaymentInfo.class);
    }

    public YmoneySqliteOpenHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
