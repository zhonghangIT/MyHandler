package com.uniquedu.myprogress;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private ProgressBar mProgressBar;
    private Button mButtonDialog;
    private ProgressDialog mProgressDialog;
    private static final int MESSAGE_TO_PROGRESSBAR=0x21;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_TO_PROGRESSBAR:
                   mProgressBar.setProgress( msg.arg1);
                    if(msg.arg1==100){
                        mButton.setText("下载完成");
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton= (Button) findViewById(R.id.buttonBar);
        mButtonDialog= (Button) findViewById(R.id.buttonDialog);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog=new ProgressDialog(MainActivity.this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();
                //使用AsyncTask更改进度
                MyAsyncTask task=new MyAsyncTask();
                task.execute();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟一个下载，使用handler实现线程间通讯
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //模拟下载任务
                        int i=0;
                        while(i<100){
                            i++;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg=mHandler.obtainMessage();
                            msg.what=MESSAGE_TO_PROGRESSBAR;
                            msg.arg1=i;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mButtonDialog.setText("下载中....");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mButtonDialog.setText(s);
            mProgressDialog.cancel();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            int i=0;
            while(i<100) {
                i++;
                publishProgress(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "下载完成";
        }
    }
}
