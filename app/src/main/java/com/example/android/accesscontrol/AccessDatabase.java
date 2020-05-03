package com.example.android.accesscontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

@Database(entities = {Visitor.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class AccessDatabase extends RoomDatabase {

    private static AccessDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized AccessDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AccessDatabase.class, "visitor_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract VisitorDao visitorDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        long date = System.currentTimeMillis();
        private VisitorDao visitorDao;

        public PopulateDbAsyncTask(AccessDatabase db) {
            this.visitorDao = db.visitorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            visitorDao.insert(new Visitor("Word", "Emmanuel", 4316, date, date, date));
//            visitorDao.insert(new Visitor("Grace", "Emmanuel", 0116, date, date, date));
//            visitorDao.insert(new Visitor("Love", "Emmanuel", 4231, date, date, date));
            return null;
        }
    }
}
