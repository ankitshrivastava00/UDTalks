package com.ziasy.xmppchatapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.EmojiAdapter;
import com.ziasy.xmppchatapplication.common.GridItemView;
import com.ziasy.xmppchatapplication.database.LocalFileManager;
import com.ziasy.xmppchatapplication.model.EmojiMadel;

import java.io.File;
import java.util.ArrayList;

public class EmojiActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<EmojiMadel> list;
    private ArrayList<Integer> selectedStrings;
    private ArrayList<String> selectValue;

    private EmojiAdapter adapter;
    private ImageView backBtn;
    private TextView sendTxt;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji_listview);

        selectedStrings = new ArrayList<>();
        selectValue = new ArrayList<>();
        list = new ArrayList<>();
        File root= Environment.getExternalStorageDirectory();
        String path=getFilesDir()+ LocalFileManager.image_folder_name+
                "/Emojis"+"/"+"emoji";
        list.add(new EmojiMadel("emoji1",path+"1.png"));
        list.add(new EmojiMadel("emoji2",path+"2.png"));
        list.add(new EmojiMadel("emoji3",path+"3.png"));
        list.add(new EmojiMadel("emoji4",path+"4.png"));
        list.add(new EmojiMadel("emoji5",path+"5.png"));
        list.add(new EmojiMadel("emoji6",path+"6.png"));
        list.add(new EmojiMadel("emoji7",path+"7.png"));
        list.add(new EmojiMadel("emoji8",path+"8.png"));
        list.add(new EmojiMadel("emoji9",path+"9.png"));
        list.add(new EmojiMadel("emoji10",path+"10.png"));
        list.add(new EmojiMadel("emoji11",path+"11.png"));
        list.add(new EmojiMadel("emoji12",path+"12.png"));
        list.add(new EmojiMadel("emoji13",path+"13.png"));
        list.add(new EmojiMadel("emoji14",path+"14.png"));
        list.add(new EmojiMadel("emoji15",path+"15.png"));
        list.add(new EmojiMadel("emoji16",path+"16.png"));
        list.add(new EmojiMadel("emoji17",path+"17.png"));
        list.add(new EmojiMadel("emoji18",path+"18.png"));
        list.add(new EmojiMadel("emoji19",path+"19.png"));
        list.add(new EmojiMadel("emoji20",path+"20.png"));
        list.add(new EmojiMadel("emoji21",path+"21.png"));
        list.add(new EmojiMadel("emoji22",path+"22.png"));
        list.add(new EmojiMadel("emoji23",path+"23.png"));
        list.add(new EmojiMadel("emoji24",path+"24.png"));
        list.add(new EmojiMadel("emoji25",path+"25.png"));
        list.add(new EmojiMadel("emoji26",path+"26.png"));
        list.add(new EmojiMadel("emoji27",path+"27.png"));
        list.add(new EmojiMadel("emoji28",path+"28.png"));
        list.add(new EmojiMadel("emoji29",path+"29.png"));
        list.add(new EmojiMadel("emoji30",path+"30.png"));
        list.add(new EmojiMadel("emoji31",path+"31.png"));
        list.add(new EmojiMadel("emoji32",path+"32.png"));
        list.add(new EmojiMadel("emoji33",path+"33.png"));
        list.add(new EmojiMadel("emoji34",path+"34.png"));
        list.add(new EmojiMadel("emoji35",path+"35.png"));
        list.add(new EmojiMadel("emoji36",path+"36.png"));
        list.add(new EmojiMadel("emoji37",path+"37.png"));
        list.add(new EmojiMadel("emoji38",path+"38.png"));
        list.add(new EmojiMadel("emoji39",path+"39.png"));
        list.add(new EmojiMadel("emoji40",path+"40.png"));
        list.add(new EmojiMadel("emoji41",path+"41.png"));
        list.add(new EmojiMadel("emoji42",path+"42.png"));
        list.add(new EmojiMadel("emoji43",path+"43.png"));
        list.add(new EmojiMadel("emoji44",path+"44.png"));
        list.add(new EmojiMadel("emoji45",path+"45.png"));
        list.add(new EmojiMadel("emoji46",path+"46.png"));
        list.add(new EmojiMadel("emoji47",path+"47.png"));
        list.add(new EmojiMadel("emoji48",path+"48.png"));
        list.add(new EmojiMadel("emoji49",path+"49.png"));
        list.add(new EmojiMadel("emoji50",path+"50.png"));
        list.add(new EmojiMadel("emoji51",path+"51.png"));
        list.add(new EmojiMadel("emoji52",path+"52.png"));
        list.add(new EmojiMadel("emoji53",path+"53.png"));
        list.add(new EmojiMadel("emoji54",path+"54.png"));
        list.add(new EmojiMadel("emoji55",path+"55.png"));
        list.add(new EmojiMadel("emoji56",path+"56.png"));
        list.add(new EmojiMadel("emoji57",path+"57.png"));
        list.add(new EmojiMadel("emoji58",path+"58.png"));
        list.add(new EmojiMadel("emoji59",path+"59.png"));
        list.add(new EmojiMadel("emoji60",path+"60.png"));
        list.add(new EmojiMadel("emoji61",path+"61.png"));
        list.add(new EmojiMadel("emoji62",path+"62.png"));
        list.add(new EmojiMadel("emoji63",path+"63.png"));
        list.add(new EmojiMadel("emoji64",path+"64.png"));
        list.add(new EmojiMadel("emoji65",path+"65.png"));
        list.add(new EmojiMadel("emoji66",path+"66.png"));
        list.add(new EmojiMadel("emoji67",path+"67.png"));
        list.add(new EmojiMadel("emoji68",path+"68.png"));
        list.add(new EmojiMadel("emoji69",path+"69.png"));
        list.add(new EmojiMadel("emoji70",path+"70.png"));
        list.add(new EmojiMadel("emoji71",path+"71.png"));
        list.add(new EmojiMadel("emoji72",path+"72.png"));
        list.add(new EmojiMadel("emoji73",path+"73.png"));
        list.add(new EmojiMadel("emoji74",path+"74.png"));
        list.add(new EmojiMadel("emoji75",path+"75.png"));
        list.add(new EmojiMadel("emoji76",path+"76.png"));
        list.add(new EmojiMadel("emoji77",path+"77.png"));
        list.add(new EmojiMadel("emoji78",path+"78.png"));
        list.add(new EmojiMadel("emoji79",path+"79.png"));


        gridView = (GridView) findViewById(R.id.grid_view_id);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        backBtn = (ImageView) findViewById(R.id.backBtn);
        sendTxt = (TextView) findViewById(R.id.sendTxt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("selectedStrings",selectedStrings.size()+"");
                if (selectValue.size()<=0){
                    Toast.makeText(EmojiActivity.this,"Please Select Emoji First", Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("image", selectValue);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        adapter = new EmojiAdapter(EmojiActivity.this, R.layout.emoji_listview, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(EmojiActivity.this, "Position " + i  +"EMOJI RESOURCE ID "+list.get(i), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent();
                intent.putExtra("image", list.get(i));
                setResult(RESULT_OK, intent);
                finish();*/
                int selectedIndex = adapter.selectedPositions.indexOf(i);

                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) view).display(false);
                    selectValue.remove( list.get(i).getName());
                } else {
                    adapter.selectedPositions.add(i);
                    ((GridItemView) view).display(true);
                    selectValue.add(list.get(i).getName());

                }
            }
        });

    }
}
