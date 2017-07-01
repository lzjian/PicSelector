package com.lzjian.picselector.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzjian.picselector.R;
import com.lzjian.picselector.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static final int SELECT_PIC = 1;
    public static final int MAX_PIC_NUM = 9;

    private TextView tv_title;
    private LinearLayout ll_left;
    private Button btn_select_pic;

    private List<String> originalPicPaths = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);

        tv_title = (TextView) findViewById(R.id.titlebar_tv_title);
        ll_left = (LinearLayout) findViewById(R.id.titlebar_ll_left);
        btn_select_pic = (Button) findViewById(R.id.btn_select_pic);
        tv_title.setText("仿微信图片选择");
        ll_left.setVisibility(View.GONE);
        btn_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int canSelectNum = getCanSelectNum();
                if (canSelectNum == 0){
                    ToastUtils.show("您选择的图片已达上限");
                    return;
                }
                Intent intent = new Intent(MainActivity.this, SelectMultiPicActivity.class);
                intent.putExtra("canSelectNum", getCanSelectNum());
                startActivityForResult(intent, SELECT_PIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case SELECT_PIC:
                    if (data == null) return;
                    ArrayList<String> selectedPics = data.getStringArrayListExtra(SelectMultiPicActivity.KEY_PICS);
                    if (selectedPics == null) return;
                    Log.i(TAG, selectedPics.toString());
                    originalPicPaths.addAll(selectedPics);
                    break;
            }
        }
    }

    private int getCanSelectNum() {
        return MAX_PIC_NUM - originalPicPaths.size();
    }
}
