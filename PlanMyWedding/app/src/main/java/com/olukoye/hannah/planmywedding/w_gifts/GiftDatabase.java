package com.olukoye.hannah.planmywedding.w_gifts;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Gifts.class}, version = 4, exportSchema = false)
public abstract class GiftDatabase extends RoomDatabase {

    public static final String DB_NAME = "wedding_gift_db";
    public static final String TABLE_NAME_GIFTS = "gifts";

    public abstract GiftDaoAccess daoAccess();

}
