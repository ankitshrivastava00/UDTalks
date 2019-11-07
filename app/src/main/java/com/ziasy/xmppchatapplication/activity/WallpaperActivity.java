package com.ziasy.xmppchatapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.WallpaperAdapter;
import com.ziasy.xmppchatapplication.common.SessionManagement;

public class WallpaperActivity extends AppCompatActivity {
    private GridView grid_view;
    private int[] image = {R.drawable.wall_default, R.drawable.wall2, R.drawable.wall3, R.drawable.wall4, R.drawable.wall5, R.drawable.wall6};
    private WallpaperAdapter wallpaperAdapter;
    private Toolbar tb_base;
    private TextView tv_toolbar_title;
    private ImageView iv_back;

    private SessionManagement sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper);
        sd = new SessionManagement(WallpaperActivity.this);
        tb_base = (Toolbar) findViewById(R.id.tb_base);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_toolbar_title.setText("Wallpaper");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        grid_view = (GridView) findViewById(R.id.grid_view);
        wallpaperAdapter = new WallpaperAdapter(WallpaperActivity.this, R.layout.wallpaper_item, image);
        grid_view.setAdapter(wallpaperAdapter);
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sd.setWallpaper(image[position]);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}