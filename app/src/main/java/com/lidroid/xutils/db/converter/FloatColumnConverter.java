package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class FloatColumnConverter implements ColumnConverter<Float> {
    @Override
    public Float getFiledValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getFloat(index);
    }

    @Override
    public Float getFiledValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return Float.valueOf(fieldStringValue);
    }

    @Override
    public Object fieldValue2ColumnValue(Float fieldValue) {
        return fieldValue;
    }

    @Override
    public String getColumnDbType() {
        return "REAL";
    }
}
