package com.object173.newsfeed.db.entities;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public Long fromHobbies(Date date) {
        return date != null ? date.getTime() : null;
    }

    @TypeConverter
    public Date toHobbies(Long data) {
        return data != null ? new Date(data) : null;
    }
}
