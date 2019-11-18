package com.ziasy.xmppchatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.ziasy.xmppchatapplication.common.GridItemView;
import com.ziasy.xmppchatapplication.model.EmojiMadel;

import java.util.ArrayList;
import java.util.List;

public class EmojiAdapter extends ArrayAdapter<EmojiMadel> {
    private Context context;
    private ViewHolder viewHolder;
    private ArrayList<EmojiMadel> list;
    private static LayoutInflater inflater = null;
    public List<Integer> selectedPositions;

    public EmojiAdapter(Context context, int Resource, ArrayList<EmojiMadel> list) {
        super(context, Resource, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.list = list;
        selectedPositions=  new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public EmojiMadel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(context) : (GridItemView) convertView;
        customView.display(list.get(position).getImage(), selectedPositions.contains(position));

        return customView;
    }
    private class ViewHolder {
        private ImageView emoji_image;
    }
}