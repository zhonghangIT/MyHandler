package com.uniquedu.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aidl.test.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private IMyAidlInterface myaidl;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myaidl=IMyAidlInterface.Stub.asInterface(service);
            try {
                int i=myaidl.add(10,15);
                Log.d("myaidl","调用服务进程中的方法得到的结果"+i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myaidl=null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton= (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去绑定服务进程中的Service
                //使用隐式的方式绑定Service
                //在Android5.0以后隐式启动Service的必须声明启动是哪个应用下的Service
                Intent intent=new Intent();
                intent.setAction("com.uniquedu.myapplication.MyService");
                intent.setPackage("com.uniquedu.myapplication");
                bindService(intent,connection,BIND_AUTO_CREATE);
            }
        });
    }
}
