package com.olukoye.hannah.planmywedding.w_guests;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Guests.class}, version = 3, exportSchema = false)
public abstract class GuestDatabase extends RoomDatabase {

    public static final String DB_NAME = "wedding_guest_db";
    public static final String TABLE_NAME_GUESTS = "guests";

    public abstract GuestDaoAccess daoAccess();

}
