package com.uniquedu.myhandler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button mButtonStart;
    private TextView mTextViewTime;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private Button mButtonPost;
    private Button mButtonToOther;
    public static final int TIME_CHANGE = 0x11;
    public static final int UI_TO_OTHER = 0x12;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_CHANGE:
                    String time = (String) msg.obj;
                    mTextViewTime.setText(time);
                    break;
                default:
                    break;
            }
        }
    };
    private MyThread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThread=new MyThread();
        mThread.start();
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonPost = (Button) findViewById(R.id.button_post);
        mTextViewTime = (TextView) findViewById(R.id.textview_time);
        mButtonToOther= (Button) findViewById(R.id.buttonToOtherThread);
        mButtonToOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件中是UI主线程操作的，在此处发送消息到其他线程
                Message msg=mThread.getHandler().obtainMessage();
                msg.what=UI_TO_OTHER;
                mThread.getHandler().sendMessage(msg);
            }
        });
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //此处运行时位于UI主线程。
                                    mTextViewTime.setText(format.format(new Date()));
                                }
                            },1000);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Message msg = mHandler.obtainMessage();//不直接创建新的消息,复用之前的消息
                            msg.obj = format.format(new Date());//在消息上添加信息
                            msg.what = TIME_CHANGE;
                            mHandler.sendMessage(msg);//发送消息
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    class MyThread extends Thread{
        public Handler getHandler() {
            return handler;
        }
        public MyThread(){
            super("不是主线程");
        }
        //需要建立一个MyThread的消息队列
        //该消息队列要循环调用
        //调用到消息后需要一个handler处理这个消息
        Handler handler=null;
        @Override
        public void run() {
            Looper.prepare();//此时回去创建一个属于该线程的消息队列
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case UI_TO_OTHER:
                            System.out.println("接收到来自UI线程的消息"+Thread.currentThread().getName());
                            break;
                    }
                }
            };
            Looper.loop();//实现消息的循环调用
        }
    }

}
