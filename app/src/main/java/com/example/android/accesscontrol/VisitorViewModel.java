package com.example.android.accesscontrol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VisitorViewModel extends AndroidViewModel {
    private VisitorRepository mVisitorRepository;
    private LiveData<List<Visitor>> allVisitors;

    public VisitorViewModel(Application application) {
        super(application);
        mVisitorRepository = new VisitorRepository(application);
        allVisitors = mVisitorRepository.getAllVisitors();
    }

    public void insert(Visitor visitor) {
        mVisitorRepository.insert(visitor);
    }

    public void update(Visitor visitor) {
        mVisitorRepository.update(visitor);
    }

    public void delete(Visitor visitor) {
        mVisitorRepository.delete(visitor);
    }

    public void deleteAll() {
        mVisitorRepository.deleteAll();
    }

    public LiveData<List<Visitor>> getAllVisitors() {
        return allVisitors;
    }
}
