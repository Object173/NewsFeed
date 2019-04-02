package com.object173.newsfeed.db.entities;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public String fromHobbies(Date date) {
        return date != null ? date.toString() : null;
    }

    @TypeConverter
    public Date toHobbies(String data) {
        return data != null ? new Date(data) : null;
    }
}
