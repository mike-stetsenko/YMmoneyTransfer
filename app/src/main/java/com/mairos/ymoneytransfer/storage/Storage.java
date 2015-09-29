package com.mairos.ymoneytransfer.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mairos.ymoneytransfer.YmoneyApplication;

import java.util.Collection;
import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Storage {

    YmoneySqliteOpenHelper openHelper;
    SQLiteDatabase database;
    DatabaseCompartment dataCompartment;

    private static Storage instance;
    private static final Object INIT_LOCK = new Object();

    private Storage(Context context) {
        openHelper = new YmoneySqliteOpenHelper(context);
        database = openHelper.getWritableDatabase();
        dataCompartment = cupboard().withDatabase(database);
    }

    public static Storage get() {
        if (instance == null) {
            synchronized (INIT_LOCK) {
                if (instance == null) {
                    instance = new Storage(YmoneyApplication.getInstance());
                }
            }
        }
        return instance;
    }

    public long put(Object entity) {
        return dataCompartment.put(entity);
    }
    public void put(Collection<?> entities) {
        dataCompartment.put(entities);
    }
    public void clearTable(Class classId) {
        dataCompartment.delete(classId, "_id IS NOT ?", "NULL");
    }
    public <T> List<T> get(Class<T> className) {
        return dataCompartment.query(className).list();
    }
    public <T> List<T> getWithSelection(Class<T> className, String selection, String args){
        return dataCompartment.query(className).withSelection(selection, args).list();
    }
}
