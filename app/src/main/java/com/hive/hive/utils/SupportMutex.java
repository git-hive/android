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

    TextView nroSupportsTV;
    ImageView supportsIV;

    public SupportMutex(TextView nroSupportsTV, ImageView supportsIV){
        this.nroSupportsTV = nroSupportsTV;
        this.supportsIV = supportsIV;
    }
    @Override
    public void lock() {
        nroSupportsTV.setEnabled(false);
        supportsIV.setEnabled(false);
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
        nroSupportsTV.setEnabled(true);
        supportsIV.setEnabled(true);
    }

    @NonNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
