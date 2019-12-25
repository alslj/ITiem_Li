package com.example.li_itime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorChoose extends AppCompatActivity {
    private LinearLayout ll;
    private TextView tv;
    private ColorPickerView colorPickerView;
    private Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_choose);
        ll = (LinearLayout) findViewById(R.id.ll_color);
        tv = (TextView) findViewById(R.id.tv_info);
        button =(Button)findViewById(R.id.Color_que);
        button1=(Button)findViewById(R.id.Color_quxiao);
        colorPickerView = new ColorPickerView(this);
        ll.addView(colorPickerView);
        colorPickerView.setOnColorBackListener(new ColorPickerView.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorPickerView.getStrColor());
                tv.setTextColor(Color.argb(a, r, g, b));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorChoose.this, MainActivity.class);
                intent.putExtra("color", colorPickerView.getStrColor());
                setResult(7,intent);
                ColorChoose.this.finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorChoose.this, MainActivity.class);
                setResult(8,intent);
                ColorChoose.this.finish();
            }
        });
    }
}



