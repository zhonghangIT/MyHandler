package com.uniquedu.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.aidl.test.IMyAidlInterface;

/**
 * Created by ZhongHang on 2016/3/9.
 */
public class MyService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
    private IMyAidlInterface.Stub stub=new IMyAidlInterface.Stub() {
        @Override
        public int add(int i, int j) throws RemoteException {
            return i+j;
        }
    };
}
