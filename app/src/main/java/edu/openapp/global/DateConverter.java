package edu.openapp.global;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Ankit on 19/01/18.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}