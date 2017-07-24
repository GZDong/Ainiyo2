package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class IntegerColumnConverter implements ColumnConverter<Integer> {
    @Override
    public Integer getFiledValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getInt(index);
    }

    @Override
    public Integer getFiledValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return Integer.valueOf(fieldStringValue);
    }

    @Override
    public Object fieldValue2ColumnValue(Integer fieldValue) {
        return fieldValue;
    }

    @Override
    public String getColumnDbType() {
        return "INTEGER";
    }
}
