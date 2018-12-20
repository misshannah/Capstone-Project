package com.olukoye.hannah.planmywedding.w_guests;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_GUESTS)
public class Guests implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int guests_id;

    public String name;


    public String category;

    @Ignore
    public String priority;

}
