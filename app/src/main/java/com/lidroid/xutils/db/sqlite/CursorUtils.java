/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.db.sqlite;

import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.Column;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.db.table.Finder;
import com.lidroid.xutils.db.table.Foreign;
import com.lidroid.xutils.db.table.Id;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.core.DoubleKeyValueMap;

public class CursorUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getEntity(final DbUtils db, final Cursor cursor, Class<T> entityType, long findCacheSequence) {
        if (db == null || cursor == null) return null;

        EntityTempCache.setSeq(findCacheSequence);
        try {
            Table table = Table.get(db, entityType);
            Id id = table.getId();
            String idColumnName = id.getColumnName();
            int idIndex = cursor.getColumnIndex(idColumnName);
            String idStr = cursor.getString(idIndex);
            T entity = EntityTempCache.get(entityType, idStr);
            if (entity == null) {
                entity = entityType.newInstance();
                id.setValue2Entity(entity, cursor, idIndex);
                EntityTempCache.put(entity, idStr);
            } else {
                return entity;
            }
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                Column column = table.columnMap.get(columnName);
                if (column != null) {
                    if (column instanceof Foreign) {
                        Foreign foreign = (Foreign) column;
                        foreign.db = db;
                        foreign.setValue2Entity(entity, cursor, i);
                    } else {
                        column.setValue2Entity(entity, cursor, i);
                    }
                }
            }

            for (Column column : table.columnMap.values()) {
                if (column instanceof Finder) {
                    Finder finder = (Finder) column;
                    finder.db = db;
                    finder.setValue2Entity(entity, null, 0);
                }
            }
            return entity;
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }

        return null;
    }

    public static DbModel getDbModel(final Cursor cursor) {
        DbModel result = null;
        if (cursor != null) {
            result = new DbModel();
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                result.add(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return result;
    }

    public static class FindCacheSequence {
        private static long seq = 0;
        private static final String FOREIGN_LAZY_LOADER_CLASS_NAME = ForeignLazyLoader.class.getName();
        private static final String FINDER_LAZY_LOADER_CLASS_NAME = FinderLazyLoader.class.getName();

        public static long getSeq() {
            String findMethodCaller = Thread.currentThread().getStackTrace()[4].getClassName();
            if (!findMethodCaller.equals(FOREIGN_LAZY_LOADER_CLASS_NAME) && !findMethodCaller.equals(FINDER_LAZY_LOADER_CLASS_NAME)) {
                ++seq;
            }
            return seq;
        }
    }

    private static class EntityTempCache {
        private EntityTempCache() {
        }

        /**
         * k1: entityType;
         * k2: idValue
         * value: entity
         */
        private static final DoubleKeyValueMap<Class<?>, String, Object> cache = new DoubleKeyValueMap<Class<?>, String, Object>();

        private static long seq = 0;

        public static void put(Object entity, String idStr) {
            if (entity != null && idStr != null) {
                cache.put(entity.getClass(), idStr, entity);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T get(Class<T> entityType, String idStr) {
            return (T) cache.get(entityType, idStr);
        }

        public static void setSeq(long seq) {
            if (EntityTempCache.seq != seq) {
                cache.clear();
                EntityTempCache.seq = seq;
            }
        }
    }
}
