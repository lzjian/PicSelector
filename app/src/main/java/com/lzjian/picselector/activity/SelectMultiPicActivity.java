package com.lzjian.picselector.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzjian.picselector.model.ImageFolder;
import com.lzjian.picselector.widget.PicFolderPopupWindow;
import com.lzjian.picselector.R;
import com.lzjian.picselector.adapter.SelectMultiPicAdapter;
import com.lzjian.picselector.util.ToastUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class SelectMultiPicActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = getClass().getSimpleName();
    public final static String KEY_PICS = "pics";

    private GridView gv;
    private TextView tv_title;
    private LinearLayout ll_left;
    private TextView tv_right;
    private TextView tv_folder_name;
    private RelativeLayout rl_pic_folder;
    private RelativeLayout rl_bottom;

    private SelectMultiPicAdapter mAdapter;
    private List<String> totalPath=new ArrayList<>();
    private int mCanSelectNum;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<ImageFolder>();

    //展示图片的线程
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x110:
                    initGriView();
                    break;
                default:
                    break;
            }

        }
    };

    private PicFolderPopupWindow bottomPopupOption;

    private Handler dismissPopupWindowHandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (bottomPopupOption != null){
                bottomPopupOption.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_select_pic);

        mCanSelectNum = getIntent().getIntExtra("canSelectNum", 0);

        gv = (GridView) findViewById(R.id.gv);
        tv_title = (TextView) findViewById(R.id.titlebar_tv_title);
        ll_left = (LinearLayout) findViewById(R.id.titlebar_ll_left);
        tv_right = (TextView) findViewById(R.id.titlebar_text_right);
        tv_folder_name = (TextView) findViewById(R.id.tv_folder_name);
        rl_pic_folder = (RelativeLayout) findViewById(R.id.rl_pic_folder);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);

        tv_title.setText("选择图片");
        tv_right.setText("完成");
        tv_right.setVisibility(View.VISIBLE);

        ll_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        rl_pic_folder.setOnClickListener(this);

        File sd = Environment.getExternalStorageDirectory();
        if (sd.canRead()){
            getImages();
        }
    }

    //扫描手机中的图片
    private void getImages(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtils.show("暂无外部存储");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                //利用contentResolver获取第三方app永远暴露的图片
                ContentResolver mContentResolver= getContentResolver();
                //查询需要的sql
                String sql= MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?";

                //与sql语句对应的参数
                String[] args=new String[]{"image/jpeg","image/png"};
                //安装添加的时间排序
//                Cursor mCursor=mContentResolver.query(mImageUri,null,sql,args,
//                        MediaStore.Images.Media.DATE_ADDED);

                String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " desc";
                Cursor mCursor=mContentResolver.query(mImageUri,null,null,null,orderBy);
                if (mCursor == null) return;
                while (mCursor.moveToNext()){
                    String path=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (new File(path).length() > 0){
                        totalPath.add(path);
                    }else{
                        // 结束单次循环
                        continue;
                    }

                    //获取该图片的父路径名
                    File parentFile=new File(path).getParentFile();
                    if (parentFile==null){
                        // 结束单次循环
                        continue;
                    }
                    String dirPath=parentFile.getAbsolutePath();
                    ImageFolder imageFolder;

                    //利用一个hashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        //初始化imageFolder
                        imageFolder=new ImageFolder();
                        // 记录父目录
                        imageFolder.setDir(dirPath);
                        // 获取第一张图片
                        imageFolder.setFirstImagePath(path);
                        // 记录父目录的文件名
                        imageFolder.setName(parentFile.getName());
                    }

                    int picSize=parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            filename = filename.toLowerCase();
                            if (filename.endsWith(".jpg")
                                    ||filename.endsWith(".png")
                                    ||filename.endsWith(".jpeg")
                                    // 后面这几个不常用的要加上去吧
                                    ||filename.endsWith(".gif")
                                    ||filename.endsWith(".bmp")
                                    ||filename.endsWith(".webp")
                                    ){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    imageFolder.setCount(picSize);
                    mImageFolders.add(imageFolder);
                }
                //释放辅助工具
                mCursor.close();
                mDirPaths = null;

                // 第一个文件夹(即所有图片)
                ImageFolder firstImageFolder = new ImageFolder();

                int totalNum = 0;
                for (ImageFolder imageFolder: mImageFolders){
                    totalNum = totalNum + imageFolder.getCount();
                }
                firstImageFolder.setName("所有图片");
                firstImageFolder.setCount(totalNum);
                if (mImageFolders.size() > 0){
                    firstImageFolder.setFirstImagePath(mImageFolders.get(0).getFirstImagePath());
                }
                mImageFolders.add(0, firstImageFolder);
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    private void initGriView(){
        if (totalPath.size() != 0){
            mAdapter=new SelectMultiPicAdapter(this, totalPath);
            mAdapter.setCanSelectNum(mCanSelectNum);
            mAdapter.setOnChangeSelectedPicNum(new SelectMultiPicAdapter.onChangeSelectedPicNumListener() {
                @Override
                public void changeSelectedPicNum(int num) {
                    if (num == 0){
                        tv_right.setText("完成");
                    }else {
                        tv_right.setText("完成"+"("+num+"/"+mCanSelectNum+")");
                    }
                }
            });
            gv.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_ll_left:
                onBackPressed();
                break;
            case R.id.titlebar_text_right:
                if (mAdapter.getSelectedPaths().size() > 0){
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(KEY_PICS, mAdapter.getSelectedPaths());
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    ToastUtils.show("请选择图片");
                }
                break;
            case R.id.rl_pic_folder:
                showOrDismissPopupWindow();
                break;
            default:
                break;
        }
    }

    private void showOrDismissPopupWindow(){
        if(bottomPopupOption == null){
            bottomPopupOption = new PicFolderPopupWindow(this, mImageFolders);
            bottomPopupOption.setOnListViewItemClickListener(new PicFolderPopupWindow.onListViewItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismissPopupWindowHandler.postDelayed(runnable, 200);
                    // 当为0时,是要显示所有图片
                    if (position == 0){
                        refreshGridView(0, totalPath);
                        return;
                    }
                    List<String> picPaths = new ArrayList<String>();
                    List<File> files = new ArrayList<File>();
                    File dir = new File(mImageFolders.get(position).getDir());
                    if (dir != null && dir.isDirectory()){
                        for(File file: dir.listFiles()){
                            String filename = file.getName().toLowerCase();
                            if (filename.endsWith(".jpg")
                                    ||filename.endsWith(".png")
                                    ||filename.endsWith(".jpeg")
                                    // 后面这几个不常用的也加上去吧
                                    ||filename.endsWith(".gif")
                                    ||filename.endsWith(".bmp")
                                    ||filename.endsWith(".webp")
                                    ){
                                files.add(file);
                            }
                        }
                        Collections.sort(files, new FileComparator());
                        for (File file: files){
                            picPaths.add(file.getAbsolutePath());
                        }
                        refreshGridView(position, picPaths);
                    }
                }
            });
        }
        if (bottomPopupOption.isShowing()){
            bottomPopupOption.dismiss();
        }else{
            bottomPopupOption.show(rl_bottom);
        }
    }

    private void refreshGridView(int position, List<String> picPaths){
        // scrollTo和scrollBy不管用,只能用这个了
        gv.smoothScrollToPositionFromTop(0, 0);
        tv_folder_name.setText(mImageFolders.get(position).getName());
        mAdapter.setData(picPaths);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 按 文件修改时间 排序（从新到旧）
     */
    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() < rhs.lastModified()) {
                return 1;//最后修改的照片在前
            } else if (lhs.lastModified() > rhs.lastModified()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
