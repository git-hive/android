package com.hive.hive.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.association.request.comments.CommentsAdapter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by vplentz on 17/03/18.
 */

public class SupportMutex implements Lock{

    public SupportMutex(){ }
    @Override
    public void lock() { }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, @NonNull TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {}

    @NonNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
