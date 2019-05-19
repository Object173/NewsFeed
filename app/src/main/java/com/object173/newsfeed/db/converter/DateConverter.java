package com.object173.newsfeed.db.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public Long fromDate(Date date) {
        return date != null ? date.getTime() : null;
    }

    @TypeConverter
    public Date toDate(Long data) {
        return data != null ? new Date(data) : null;
    }
}
