package com.t3h.newsproject.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.t3h.newsproject.model.News;

@Database(entities = News.class, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class,
                    "news-Database")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return appDatabase;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.

            // Add column
            database.execSQL("ALTER TABLE 'news' ADD COLUMN 'isFavorite' INTEGER DEFAULT 0 NOT NULL");
        }
    };

//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Since we didn't alter the table, there's nothing else to do here.
//
//            //Add favorite
//            database.execSQL("UPDATE news SET isFavorite =1 WHERE id= :id");
//
//            //Del favorite
//            database.execSQL("UPDATE news SET isFavorite =0 WHERE id= :id");
//        }
//    };

    public abstract NewsDao getNewsDao();
}
