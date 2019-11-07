package com.ziasy.xmppchatapplication.adapter;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.activity.CareerContactListActivity;
import com.ziasy.xmppchatapplication.model.ChatUserList;

import java.util.List;

public class AddContatctListAdapter extends BaseAdapter {
    private  ViewHolder viewHolder;
    private static LayoutInflater inflater = null;
    private List<ChatUserList> list;
    private Context activity;
    private CareerContactListActivity careerContactListActivity;

    public AddContatctListAdapter(Context activity, List<ChatUserList> list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.activity = activity;
        this.careerContactListActivity = (CareerContactListActivity) activity;
    }

    @Override
    public int getCount() {
        return list.size();
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
                View vi = convertView;
        if (convertView == null) {
         vi = inflater.inflate(R.layout.ud_user_list_row, null);
        viewHolder = new ViewHolder();
        viewHolder.userName = (TextView) vi.findViewById(R.id.tv_name);
        viewHolder.mainLayout = (LinearLayout) vi.findViewById(R.id.mainLayout);
        viewHolder.view_career = (RelativeLayout) vi.findViewById(R.id.view_career);
        vi.setTag(viewHolder);

        } else{
            viewHolder = (ViewHolder) vi.getTag();
        }
        if (position % 2 == 0) {
            viewHolder.view_career.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));
        } else {
            viewHolder.view_career.setBackgroundColor(ContextCompat.getColor(activity, R.color.ud_blue));
        }
     /*   viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        viewHolder.userName.setText(list.get(position).getName());
        return vi;
    }

    class ViewHolder {
        TextView userName;
        private LinearLayout mainLayout;
        private RelativeLayout view_career;
    }
}