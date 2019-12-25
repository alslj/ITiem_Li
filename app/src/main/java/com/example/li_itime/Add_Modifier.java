package com.example.li_itime;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.le.AdvertiseData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.li_itime.MainActivity.color;

public class Add_Modifier extends AppCompatActivity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private EditText biaoti, beizhu;
    private int year,month,day,hour, minute;
    private int editpition;
    private ListView listView;
    private List<SetItem> setItemList = new ArrayList<SetItem>();
    private  StringBuffer date, time;
    private Toolbar toolbar;
    private static final int CHOOSE_PHOTO = 2;
    private String title, deadline;
    public static final String ADD = "Add_Modifier";
    public static final int TAKE_PHOTO =1 ;
    public static final int CHOOSE_PHTOT =2;
    private Uri uri;
    private byte[] bitmapByte = null;
    private int getback;
    private String remarks;
    private ConstraintLayout constraintLayout;
    private Window window ;
    private ListView listView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__modifier);
        toolbar = findViewById(R.id.toolbar_Add);
        window = Add_Modifier.this.getWindow();
        widnowColorchose(color,window);
        biaoti = (EditText) findViewById(R.id.Add_Modifier_title);
        beizhu = (EditText) findViewById(R.id.beizhu_Add_Modifier);
        listView = (ListView) findViewById(R.id.listview_add_Modifier);
        date = new StringBuffer();
        time = new StringBuffer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        constraintLayout = (ConstraintLayout)findViewById(R.id.coslayout_Add_Modifier);
        constraintLayout.setBackgroundColor(Color.parseColor(color));
        getback = getIntent().getIntExtra("getback", -1);
        if (getback == 2){
            title=getIntent().getStringExtra("title_co");
            deadline = getIntent().getStringExtra("dead_co");
            remarks = getIntent().getStringExtra("remarks");
            biaoti.setText(title);
            beizhu.setText(remarks);
        }

        editpition = getIntent().getIntExtra("ediPosition", 1000000);




        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        setItemList.add(new SetItem("日期"," ", R.drawable.ic_calendar));
        setItemList.add(new SetItem("图片", " ", R.drawable.ic_photo));
        setItemList.add(new SetItem("标签", " ", R.drawable.biaoqian));
        setItemList.add(new SetItem("重复设置"," ", R.drawable.chongfu));



        SetItem_Adapter setItem_adapter = new SetItem_Adapter(Add_Modifier.this, R.layout.item_add_list, setItemList);
        listView.setAdapter(setItem_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :{
                        final AlertDialog.Builder builder_date = new AlertDialog.Builder(Add_Modifier.this);
                        builder_date.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (date.length() > 0) {
                                    date.delete(0, date.length());
                                }
                                AlertDialog.Builder builder_time = new AlertDialog.Builder(Add_Modifier.this);
                                builder_time.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (time.length() > 0) {
                                            time.delete(0, time.length());
                                        }
                                        time = time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分");
                                        dialog.dismiss();
                                    }
                                });
                                builder_time.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog_time = builder_time.create();
                                View time_dialogView = View.inflate(Add_Modifier.this, R.layout.time_dialog, null);
                                TimePicker timePicker = (TimePicker) time_dialogView.findViewById(R.id.time_Add);
                                timePicker.setCurrentHour(hour);
                                timePicker.setCurrentMinute(minute);
                                timePicker.setIs24HourView(true);

                                timePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) Add_Modifier.this);
                                dialog_time.setTitle("设置时间");
                                dialog_time.setView(time_dialogView);
                                dialog_time.show();

                                date = date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月")
                                        .append(String.valueOf(day)).append("日");
                                dialog.dismiss();
                            }
                        });
                        builder_date.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog_date = builder_date.create();
                        View date_dialogView = View.inflate(Add_Modifier.this,R.layout.date_dialog, null);
                        DatePicker datePicker = (DatePicker) date_dialogView.findViewById(R.id.datepicker_Add);

                        dialog_date.setTitle("设置日期");
                        dialog_date.setView(date_dialogView);
                        dialog_date.show();
                        datePicker.init(year,month,day, (DatePicker.OnDateChangedListener) Add_Modifier.this);
                        break;
                    }
                    case 1:{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Add_Modifier.this);
                        dialog.setTitle("选择图片来源")
                                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showPictureDialogIncustom();
                                    }
                                })
                                .setPositiveButton("相机", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File file = new File(getExternalCacheDir(), "out_putimage.jpg");
                                        try {
                                            if (file.exists()){
                                                file.delete();
                                            }
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (Build.VERSION.SDK_INT >= 24){
                                            uri = FileProvider.getUriForFile(Add_Modifier.this, "com.example.li_itime", file);
                                        }else {
                                            uri = Uri.fromFile(file);
                                        }
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, TAKE_PHOTO);
                                    }
                                }).show();
                        break;
                    }
                    case 2:{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Add_Modifier.this);
                        dialog.setTitle("抱歉")
                              .setMessage("此功能尚未完成，请取消点击")
                               .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               }).show();
                        break;
                    }
                    case 3:{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Add_Modifier.this);
                        final AlertDialog dialog_zhouqi = dialog.create();
                        View view1=View.inflate(Add_Modifier.this,R.layout.item_aler, null);
                        listView1 = (ListView)view1.findViewById(R.id.item_alte);
                        String[] M = {"每年","每月","每周","每天","自定义"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Modifier.this,android.R.layout.simple_list_item_1,M);
                        listView1.setAdapter(adapter);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialog_zhouqi.dismiss();
                            }
                        });
                        dialog_zhouqi.setView(view1);
                        dialog_zhouqi.show();
                        break;
                    }

                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_modifer, menu);
        return true;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String examin_biaoti = biaoti.getText().toString().trim();
        String examin_beizhu = beizhu.getText().toString().trim();
        String examin_deadline = date.toString().trim();
        if (examin_deadline.length() != 0){
            deadline = date.toString().trim();
        }

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(Add_Modifier.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.quereng:
               if (getback == 1){
                   if (0 == examin_biaoti.length()){
                       Toast.makeText(Add_Modifier.this, "标题不能为空", Toast.LENGTH_LONG).show();
                   }else {
                       if (deadline == null){
                           Toast.makeText(Add_Modifier.this, "日期不能为空", Toast.LENGTH_LONG).show();
                       }
                       else {
                           if (bitmapByte == null) {
                               Intent intent1 = new Intent(this, MainActivity.class);
                               intent1.putExtra("biaoti", biaoti.getText().toString());
                               intent1.putExtra("date", deadline);
                               if (examin_beizhu != null){
                                   intent1.putExtra("remarks_add",examin_beizhu);
                               }else {
                                   examin_beizhu = " ";
                                   intent1.putExtra("remarks_add",examin_beizhu);
                               }
                               setResult(1, intent1);
                               Add_Modifier.this.finish();
                           }else {
                               Intent intent1 = new Intent(this, MainActivity.class);
                               intent1.putExtra("biaoti", biaoti.getText().toString());
                               intent1.putExtra("date", deadline);
                               intent1.putExtra("bitmap", bitmapByte);
                               if (examin_beizhu != null){
                                   intent1.putExtra("remarks_add",examin_beizhu);
                               }else {
                                   examin_beizhu = " ";
                                   intent1.putExtra("remarks_add",examin_beizhu);
                               }
                               setResult(2, intent1);
                               Add_Modifier.this.finish();
                           }
                       }
                   }
               }else if (getback == 2){
                   Intent intent2 = new Intent(this, Content.class);
                   intent2.putExtra("biaoti", examin_biaoti);
                   intent2.putExtra("date",deadline);
                   if (examin_beizhu != null){
                       intent2.putExtra("remarks_add_content",examin_beizhu);
                   }else {
                       examin_beizhu = " ";
                       intent2.putExtra("remarks_add_content",examin_beizhu);
                   }
                   setResult(5,intent2);
                   Add_Modifier.this.finish();
               }
                break;
        }
        return true;
    }

    class SetItem_Adapter extends ArrayAdapter<SetItem>{
        private  int resorceId;
        public SetItem_Adapter(@NonNull Context context, int resource, List<SetItem> objects) {
            super(context, resource, objects);
            resorceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SetItem setItem = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resorceId, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.setImage_Add_list);
            imageView.setImageResource(setItem.getImageid());
            TextView textView1 = (TextView) view.findViewById(R.id.buchong_Add_list);
            textView1.setText(setItem.getBuchong());
            TextView textView2 = (TextView) view.findViewById(R.id.text_Add_list);
            textView2.setText(setItem.getBiaoti());
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO :
                if (resultCode == RESULT_OK){
                    Bitmap bitmap =  null;
                    try {
                         bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    bitmapByte = baos.toByteArray();
                }
                break;
            case CHOOSE_PHTOT:
                if (resultCode == RESULT_OK){
                    Uri uri1 = data.getData();
                    Bitmap bitmap= null;
                    ContentResolver cr = this.getContentResolver();
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                   bitmapByte = baos.toByteArray();
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPictureDialogIncustom(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOOSE_PHTOT);
    }
    protected void widnowColorchose(String color, Window window){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
    }








}
