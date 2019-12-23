package com.example.li_itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import static com.example.li_itime.MainActivity.mytimeList;
import static com.example.li_itime.MainActivity.mytime_adpater;


public class Content extends AppCompatActivity {

    private TextView textView_conten_title, textView_conten_time, textView_conten_count;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private String title, deadline;
    private CountDownTimer countDownTimer;
    private long timesMills_now, timesMills_set, mills;
    private int imageViewid;
    private int position;
    private byte[] bytes;
    private int judge;
    public static final int RESULT_MODIFER = 904;
    private TextView textView_beizhu;
    private String beizhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        textView_conten_count = (TextView) findViewById(R.id.content_count);
        textView_conten_time = (TextView) findViewById(R.id.content_time);
        textView_conten_title = (TextView) findViewById(R.id.content_title);
        textView_beizhu = (TextView)findViewById(R.id.beizhu);
        appBarLayout = findViewById(R.id.Appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        deadline = bundle.getString("date");
        position = bundle.getInt("ediPosition");
        judge = bundle.getInt("judge");
        beizhu = bundle.getString("remarks");

        if (judge == 0){
            imageViewid = bundle.getInt("photoId");
            collapsingToolbarLayout.setBackgroundResource(imageViewid);
        }else {
            bytes = bundle.getByteArray("bitmap");
            assert bytes != null;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Drawable  drawable = new BitmapDrawable(bitmap);
            collapsingToolbarLayout.setBackground(drawable);
        }


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
                Intent intent = new Intent(Content.this,Add_Modifier.class);
                intent.putExtra("title_co",title);
                intent.putExtra("dead_co", deadline);
                intent.putExtra("position_co", position);
                intent.putExtra("getback",2);
                intent.putExtra("remarks", beizhu);
                startActivityForResult(intent, RESULT_MODIFER);
                break;
            }
            case R.id.delete:{
                new android.app.AlertDialog.Builder(Content.this)
                        .setTitle("询问")
                        .setIcon(R.drawable.dead)
                        .setMessage("真的要删除吗？")
                        .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Content.this, MainActivity.class);
                                intent.putExtra("ediptionint", position);
                                setResult(3,intent);
                                Content.this.finish();
                            }
                        })
                        .setNegativeButton("不了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Content.this,"逃过一劫", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

                break;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_MODIFER:
                if (resultCode == 5){
                    title = data.getStringExtra("biaoti");
                    deadline = data.getStringExtra("date");
                    beizhu = data.getStringExtra("remarks_add");
                    transfer();
                    initData();
                    mytimeList.get(position).setTitle(title);
                    mytimeList.get(position).setData(deadline);
                    mytimeList.get(position).setBeizhu(beizhu);
                    mytime_adpater.notifyDataSetChanged();
                }

        }
    }


    public void initData(){
        textView_conten_title.setText(title);
        textView_conten_time.setText(deadline);
        textView_beizhu.setText(beizhu);

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
            calendar.setTime(new SimpleDateFormat("yyyy年MM月dd日").parse(deadline));
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
