package com.king.faceunlockdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btUserId;
    private EditText etUserId;
    private Button btCamera;
    private Button btCompare;
    private Button btDetect;
    private Button btGetImage;
    private RelativeLayout rlPicture;
    private File mPictureFile;

    private int REQUEST_CAMERA_IMAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlPicture = findViewById(R.id.rl_picture);
        btUserId = findViewById(R.id.bt_userId);
        etUserId = findViewById(R.id.et_userID);
        btCamera = findViewById(R.id.bt_camera);
        btCompare = findViewById(R.id.bt_compare);
        btDetect = findViewById(R.id.bt_detect);
        btGetImage = findViewById(R.id.bt_getImage);


    }

    private void takePhoto(){
        //相机拍照后照片保存的路径
        mPictureFile = new File(Environment.getExternalStorageDirectory(),
                "picture" + System.currentTimeMillis() / 1000 + ".jpg");

        //启动拍照，并保存到临时文件
        Intent mIntent = new Intent();
        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
        mIntent.putExtra(MediaStore.Images.Media.ORIENTATION,0);
        startActivityForResult(mIntent,REQUEST_CAMERA_IMAGE);
    }


}
