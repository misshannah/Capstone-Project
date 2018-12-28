package com.olukoye.hannah.planmywedding.w_gifts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GiftDaoAccess {

    @Insert
    long insertGifts(Gifts gifts);

    @Insert
    void insertGiftsList(List<Gifts> giftsList);

    @Query("SELECT * FROM " + GiftDatabase.TABLE_NAME_GIFTS)
    List<Gifts> fetchAllGifts();

    @Query("SELECT * FROM " + GiftDatabase.TABLE_NAME_GIFTS + " WHERE category = :category")
    List<Gifts> fetchGiftsListByCategory(String category);

    @Query("SELECT * FROM " + GiftDatabase.TABLE_NAME_GIFTS + " WHERE gifts_id = :giftsId")
    Gifts fetchGiftsListById(int giftsId);

    @Update
    int updateGifts(Gifts gifts);

    @Delete
    int deleteGifts(Gifts gifts);
}
