package com.ziasy.xmppchatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ziasy.xmppchatapplication.R;

public class WallpaperAdapter extends BaseAdapter {
    private int[] image;
    private Context context;

    public WallpaperAdapter(Context context, int Resource, int[] image) {
        this.context = context;
        this.image = image;

    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, null, false);
            viewHolder.wallIcon = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }try {
            viewHolder.wallIcon.setImageResource(image[position]);
        }catch (Exception e){
            e.toString();
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView wallIcon;
    }
}
