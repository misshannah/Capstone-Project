package com.olukoye.hannah.planmywedding.w_gifts;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = GiftDatabase.TABLE_NAME_GIFTS)
public class Gifts implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int gifts_id;

    public String name;


    public String category;

    @Ignore
    public String priority;

}
