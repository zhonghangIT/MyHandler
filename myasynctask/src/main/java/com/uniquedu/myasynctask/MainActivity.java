package com.uniquedu.myasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewProgress;
    private Button mButtonStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonStart= (Button) findViewById(R.id.button_start);
        mTextViewProgress= (TextView) findViewById(R.id.textview_progress);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask task=new MyAsyncTask();
                task.execute();
            }
        });
    }

    /**
     * 三个参数类型分别对应的方法，第一个对应doInBackground方法，第二个对应onProgressUpdate，第三个对应onPostExecute
     */
    class MyAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开启另外线程前的准备工作，放置这个地方,属于UI主线程
            System.out.println("下载前的准备工作,属于的线程为"+Thread.currentThread().getName());
            mTextViewProgress.setText("开始下载");
        }
        @Override
        protected String doInBackground(String[] params) {
            //模拟的后台线程下载，耗时操作，属于AsyncTask中封装好的线程，不能操作UI控件

            int i=0;
            while(i<100){
                i++;
                System.out.println("下载中....进度"+i+"%,属于的线程为"+Thread.currentThread().getName());
                publishProgress(i);//通知了onProgressUpdate方法
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "下载完成";
        }
        @Override
        protected void onProgressUpdate(Integer[] values) {
            super.onProgressUpdate(values);
            //动态更改耗时操作的进度，属于主线程，可以动态更改控件
            System.out.println("更改进度显示,属于的线程为"+Thread.currentThread().getName());
            mTextViewProgress.setText("当前下载进度"+values[0]+"%");
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            //返回耗时操作的结果，属于UI主线程
            System.out.println("下载完成,属于的线程为"+Thread.currentThread().getName());
            mTextViewProgress.setText(o);
        }


    }

}
