package com.lidroid.xutils.db.converter;

import android.database.Cursor;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午8:57
 */
public interface ColumnConverter<T> {

    T getFiledValue(final Cursor cursor, int index);

    T getFiledValue(String fieldStringValue);

    Object fieldValue2ColumnValue(T fieldValue);

    String getColumnDbType();
}
