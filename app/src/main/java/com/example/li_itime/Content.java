package com.example.li_itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Content extends AppCompatActivity {

    private TextView textView_conten_title, textView_conten_time, textView_conten_count;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private String title, deadline;
    private CountDownTimer countDownTimer;
    private long timesMills_now, timesMills_set, mills;
    int imageViewid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        textView_conten_count = (TextView) findViewById(R.id.content_count);
        textView_conten_time = (TextView) findViewById(R.id.content_time);
        textView_conten_title = (TextView) findViewById(R.id.content_title);
        appBarLayout = findViewById(R.id.Appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        deadline = bundle.getString("date");
        imageViewid = bundle.getInt("photoId");


        collapsingToolbarLayout.setBackgroundResource(imageViewid);
        //设置图片


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回健

        transfer();
        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.modifer:{
                Toast.makeText(this,"选择了修改", Toast.LENGTH_SHORT).show();
                //修改
                break;
            }
            case R.id.delete:{
                Toast.makeText(this,"删除成功", Toast.LENGTH_SHORT).show();
                //删除
                break;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:{
                if (1==requestCode){
                   //do something
                }
                Toast.makeText(this,"修改成功", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    public void initData(){
        textView_conten_title.setText(title);
        textView_conten_time.setText(deadline);

        countDownTimer = new CountDownTimer(mills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!Content.this.isFinishing()){
                    long day = millisUntilFinished/ (1000 * 24 * 60 * 60); //单位天
                    long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
                    long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
                    long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒

                    textView_conten_count.setText(day+"天"+hour + "小时" + minute + "分钟" + second + "秒");

                }

            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    public void transfer(){
        Calendar calendar = Calendar.getInstance();
        timesMills_now = calendar.getTimeInMillis();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy年mm月dd日").parse(deadline));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timesMills_set = calendar.getTimeInMillis();
        mills = timesMills_set - timesMills_now;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }



}
