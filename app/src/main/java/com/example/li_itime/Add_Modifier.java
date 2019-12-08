package com.example.li_itime;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Add_Modifier extends AppCompatActivity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private EditText biaoti, beizhu;
    private int year,month,day,hour, minute;
    private int editpition;
    private ListView listView;
    private List<SetItem> setItemList = new ArrayList<SetItem>();
    private  StringBuffer date, time;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__modifier);
        toolbar = findViewById(R.id.toolbar_Add);
        biaoti = (EditText) findViewById(R.id.Add_Modifier_title);
        beizhu = (EditText) findViewById(R.id.beizhu_Add_Modifier);
        listView = (ListView) findViewById(R.id.listview_add_Modifier);
        date = new StringBuffer();
        time = new StringBuffer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editpition = getIntent().getIntExtra("ediPosition", 100000);


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
                                AlertDialog.Builder builder_time = new AlertDialog.Builder(Add_Modifier.this);
                                builder_time.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
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
        String examin_date = date.toString().trim();
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(Add_Modifier.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.quereng:
                if (0 == examin_biaoti.length()){
                    Toast.makeText(Add_Modifier.this, "标题不能为空", Toast.LENGTH_LONG).show();
                }else {
                    if (0 == date.length()){
                        Toast.makeText(Add_Modifier.this, "日期不能为空", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent1 = new Intent(this, MainActivity.class);
                        intent1.putExtra("biaoti", biaoti.getText().toString());
                        intent1.putExtra("date", examin_date);
                        setResult(1, intent1);
                        Add_Modifier.this.finish();
                    }
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
}
