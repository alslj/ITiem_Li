package com.example.li_itime;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 901;
    public static final int REQUEST_SET = 902;
    protected static ListView listView;
    protected static List<Mytime>  mytimeList =new ArrayList<>();
    protected static Mytime_Adpater mytime_adpater;
    private Toolbar toolbar;
    private Mytime_FileResoure mytime_fileResoure;
    private  int ediPosition;
    private long timeMilffs_set, timeMiffls_now;
    private long day;
    private AppBarConfiguration mAppBarConfiguration;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_Modifier.class);
                intent.putExtra("getback",1);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        init();



        Log.d(TAG, mytimeList.size()+"个");
        listView = (ListView)findViewById(R.id.list_view_content_main);

        mytime_adpater = new Mytime_Adpater(MainActivity.this, R.layout.item_listview, mytimeList);
        listView.setAdapter(mytime_adpater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ediPosition =  position;
                Intent intent = new Intent(MainActivity.this, Content.class);//显示启动另一个程序
                Mytime mytime = (Mytime) mytime_adpater.getItem(position);
                if (mytime.getJudg() == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", mytime.getTitle());
                    bundle.putString("date", mytime.getData());
                    bundle.putInt("judge", mytime.getJudg());
                    bundle.putInt("photoId", mytime.getCoverResoureID());
                    bundle.putInt("ediPosition", ediPosition);
                    intent.putExtras(bundle);
                    //onDestroy();
                    startActivityForResult(intent, REQUEST_SET);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", mytime.getTitle());
                    bundle.putString("date", mytime.getData());
                    bundle.putInt("ediPosition", ediPosition);
                    bundle.putByteArray("bitmap",mytime.getBitmap());
                    bundle.putInt("judge", mytime.getJudg());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_SET);

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void init(){
        mytime_fileResoure = new Mytime_FileResoure(this);
        mytimeList = mytime_fileResoure.Mytime_load();
        if ( 0 ==mytimeList.size()){
            mytimeList.add(new Mytime("西瓜","2020年12月18日", R.drawable.pg10));
        }//为什么修改这里的图片id会影响在文件中的图片id??。
        //原因：在Mytime_FileResoure中存储文件格式问题。
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    class Mytime_Adpater extends ArrayAdapter<Mytime> {
        private int resoureID;

        public Mytime_Adpater(@NonNull Context context, int resource, List<Mytime> mytimeList) {
            super(context, resource, mytimeList);
            resoureID = resource;
        }

        @SuppressLint("SimpleDateFormat")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Mytime mytime1 = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resoureID, parent, false);
            assert mytime1 != null;
            if (mytime1.getJudg() == 0) {
                TextView title = view.findViewById(R.id.list_text_title);
                title.setText(mytime1.getTitle());
                TextView data = view.findViewById(R.id.list_text_date);
                data.setText(mytime1.getData());
                ImageView imageView = view.findViewById(R.id.image_list_cover);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(mytime1.getCoverResoureID());
                final StackBlurManager stackBlurManager = new StackBlurManager(bitmapDrawable.getBitmap());
                imageView.setImageBitmap(stackBlurManager.process(50));
            }else {
                TextView title = view.findViewById(R.id.list_text_title);
                title.setText(mytime1.getTitle());
                TextView data = view.findViewById(R.id.list_text_date);
                data.setText(mytime1.getData());
                ImageView imageView = view.findViewById(R.id.image_list_cover);
                Bitmap bitmap = BitmapFactory.decodeByteArray(mytime1.getBitmap(), 0, mytime1.getBitmap().length);
                final StackBlurManager stackBlurManager = new StackBlurManager(bitmap);
                imageView.setImageBitmap(stackBlurManager.process(50));
            }

            try {
                Calendar calendar = Calendar.getInstance();
                timeMiffls_now = calendar.getTimeInMillis();

                calendar.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy年MM月dd日").parse(mytime1.getData())));
                timeMilffs_set = calendar.getTimeInMillis();
                day=(timeMilffs_set- timeMiffls_now)/(1000*24*60*60);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TextView textView = view.findViewById(R.id.list_text_countday);
            textView.setText(day+"天");
            return view;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode==1){
                    String biaoti=data.getStringExtra("biaoti");
                    String date=data.getStringExtra("date");
                    int num = (int) (Math.random() * 7 + 1);
                    switch(num){
                        case 1:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg1));
                            break;
                        case 2:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg10));
                            break;
                        case 3:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg3));
                            break;
                        case 4:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg6));
                            break;
                        case 5:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg7));
                            break;
                        case 6:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg8));
                            break;
                        case 7:
                            mytimeList.add(new Mytime(biaoti,date,R.drawable.pg9));
                            break;
                    }
                    String str = "保存成功";
                    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                    mytime_adpater.notifyDataSetChanged();
                    //Log.d(TAG, mytimeList.size()+"个");
                    mytime_fileResoure.Mytime_save();
                    break;
                }else if (resultCode == 2){
                    String biaoti=data.getStringExtra("biaoti");
                    String date=data.getStringExtra("date");
                    byte[] bytes = data.getByteArrayExtra("bitmap");
                    mytimeList.add(new Mytime(biaoti, date, bytes));
                    mytime_adpater.notifyDataSetChanged();
                    mytime_fileResoure.Mytime_save();
                    break;
                }
            case REQUEST_SET:
                if (resultCode == 3){
                    int position = data.getIntExtra("ediptionint", 1000000);
                    if (position != 1000000){
                        mytimeList.remove(position);
                        mytime_adpater.notifyDataSetChanged();
                        mytime_fileResoure.Mytime_save();
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mytime_fileResoure.Mytime_save();
    }
}

