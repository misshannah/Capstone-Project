package com.olukoye.hannah.planmywedding.w_guests;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GuestDaoAccess {

    @Insert
    long insertGuests(Guests guests);

    @Insert
    void insertGuestsList(List<Guests> guestsList);

    @Query("SELECT * FROM " + GuestDatabase.TABLE_NAME_GUESTS)
    List<Guests> fetchAllGuests();

    @Query("SELECT * FROM " + GuestDatabase.TABLE_NAME_GUESTS + " WHERE category = :category")
    List<Guests> fetchGuestsListByCategory(String category);

    @Query("SELECT * FROM " + GuestDatabase.TABLE_NAME_GUESTS + " WHERE guests_id = :guestsId")
    Guests fetchGuestsListById(int guestsId);

    @Update
    int updateGuests(Guests guests);

    @Delete
    int deleteGuests(Guests guests);
}
