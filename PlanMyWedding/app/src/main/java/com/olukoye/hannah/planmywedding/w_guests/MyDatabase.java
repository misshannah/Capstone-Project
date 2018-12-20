package com.olukoye.hannah.planmywedding.w_guests;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Guests.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "app_db";
    public static final String TABLE_NAME_GUESTS = "guests";

    public abstract DaoAccess daoAccess();

}
