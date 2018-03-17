package com.hive.hive.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hive.hive.association.request.RequestAdapter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by vplentz on 17/03/18.
 */

public class SupportMutex implements Lock{

    RequestAdapter.RequestViewHolder  holder;

    public SupportMutex(RequestAdapter.RequestViewHolder holder){
        this.holder = holder;
    }

    @Override
    public void lock() {
        holder.getmNumberOfSupportsTV().setEnabled(false);
        holder.getmSupportsIV().setEnabled(false);
    }

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
    public void unlock() {
        holder.getmNumberOfSupportsTV().setEnabled(true);
        holder.getmSupportsIV().setEnabled(true);
    }

    @NonNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
