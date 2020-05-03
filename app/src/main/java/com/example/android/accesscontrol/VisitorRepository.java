package com.example.android.accesscontrol;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VisitorRepository {
    private VisitorDao mVisitorDao;
    private LiveData<List<Visitor>> allVisitors;

    public VisitorRepository(Application application) {
        AccessDatabase database = AccessDatabase.getInstance(application);

        mVisitorDao = database.visitorDao();
        this.allVisitors = mVisitorDao.getAllVisitors();
    }

    public void insert(Visitor visitor) {
        new InsertAsyncTask(mVisitorDao).execute(visitor);
    }

    public void update(Visitor visitor) {
        new UpdateAsyncTask(mVisitorDao).execute(visitor);

    }

    public void delete(Visitor visitor) {
        new DeleteAsyncTask(mVisitorDao).execute(visitor);

    }

    public void deleteAll() {
        new DeleteAllAsyncTask(mVisitorDao).execute();

    }

    public LiveData<List<Visitor>> getAllVisitors() {
        return allVisitors;
    }

    private static class InsertAsyncTask extends AsyncTask<Visitor, Void, Void> {
        private VisitorDao visitorDao;

        public InsertAsyncTask(VisitorDao visitorDao) {
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.insert(visitors[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Visitor, Void, Void> {
        private VisitorDao visitorDao;

        public UpdateAsyncTask(VisitorDao visitorDao) {
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.update(visitors[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Visitor, Void, Void> {
        private VisitorDao visitorDao;

        public DeleteAsyncTask(VisitorDao visitorDao) {
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.delete(visitors[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private VisitorDao visitorDao;

        public DeleteAllAsyncTask(VisitorDao visitorDao) {
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            visitorDao.deleteAllVisitors();
            return null;
        }
    }

}
