package com.view2bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCommon;
    private Button btnScroll;
    private Button btnInflate;
    private RelativeLayout container;
    private LinearLayout llScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCommon = findViewById(R.id.btn_common);
        btnScroll = findViewById(R.id.btn_scroll);
        btnInflate = findViewById(R.id.btn_inflate);
        container = findViewById(R.id.container);
        llScroll = findViewById(R.id.ll_scroll);

        btnCommon.setOnClickListener(this);
        btnScroll.setOnClickListener(this);
        btnInflate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Bitmap bitmap=null;
        switch (v.getId()) {
            case R.id.btn_common:
                bitmap = createBitmap(container);
                break;
            case R.id.btn_scroll:
                bitmap = createBitmap2(llScroll);
                break;
            case R.id.btn_inflate:
                View view = LayoutInflater.from(this).inflate(R.layout.view_inflate, null, false);
                bitmap = createBitmap3(view, getScreenWidth(), getScreenHeight());
                break;
            default:
                break;
        }
        saveBitmap(bitmap);

    }


    private Bitmap createBitmap(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public Bitmap createBitmap2(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bitmap;
    }

    public Bitmap createBitmap3(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bitmap;
    }

    private void saveBitmap(Bitmap bitmap) {
        FileOutputStream fos;
        try {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, "test.png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }


}
