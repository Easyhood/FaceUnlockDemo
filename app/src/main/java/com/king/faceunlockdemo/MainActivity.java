package com.king.faceunlockdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btUserId;
    private EditText etUserId;
    private Button btPhoto1;
    private Button btPhoto2;
    private Button btCompare;
    private Button btGetImage;
    private RelativeLayout rlPicture;
    private TextView tvConResult;

    private String face_token1;
    private String face_token2;

    private Context context = MainActivity.this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlPicture = findViewById(R.id.rl_picture);
        btUserId = findViewById(R.id.bt_userId);
        etUserId = findViewById(R.id.et_userID);
        btPhoto1 = findViewById(R.id.bt_camera);
        btPhoto2 = findViewById(R.id.bt_compare);
        btGetImage= findViewById(R.id.bt_detect);
        btCompare = findViewById(R.id.bt_getImage);
        tvConResult = findViewById(R.id.tv_conResult);

        FaceUtil.checkAndRequestPermission(context,200);

        takePhoto1();
        takePhoto2();
        takeCompare();
    }




    private void takePhoto1() {

        btPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File photoPath = takePhoto();
                if (photoPath != null){
                    Toast.makeText(context,photoPath.toString(),Toast.LENGTH_SHORT).show();
                    String json = FaceappDetect.detectRequest(photoPath);

                    if (json != null){
                        face_token1 = DetectJsonParse.getFaceToken(json);
                    }else {
                        Toast.makeText(context, "json为null",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"photoPath为null",Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    File mPhotoPath;
    private void takePhoto2() {
        btPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mPhotoPath = takePhoto();
//                if (photoPath != null){
//                    Toast.makeText(context,photoPath.toString(),Toast.LENGTH_SHORT).show();
//
//                    String json = FaceappDetect.detectRequest(photoPath);
//
//                    if (json != null){
//                        face_token2 = DetectJsonParse.getFaceToken(json);
//                    }else {
//                        Toast.makeText(context, "json为null",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(context,"photoPath为null",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void takeCompare() {

        btCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = FaceCompare.compareRequest(face_token1, face_token2);
                if (json != null){
                    Toast.makeText(context,json,Toast.LENGTH_SHORT).show();
                    float confindence = CompareJsonParse.getConfindence(json);
                    if (confindence>80){
                        tvConResult.setText("比对结果：是同一个人");
                    }else {
                        tvConResult.setText("比对结果：不是同一个人");
                    }
                }else {
                    Toast.makeText(context,"json为null",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }



    private File takePhoto(){
        //相机拍照后照片保存的路径
        File mPictureFile = new File(Environment.getExternalStorageDirectory(),
                "picture" + System.currentTimeMillis() / 1000 + ".jpg");

        //启动拍照，并保存到临时文件
        Intent mIntent = new Intent();
        Uri photoUri = FileProvider.getUriForFile(this,getPackageName()
                +".provider",
                mPictureFile);
        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        mIntent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
        mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(mIntent,1);

        return mPictureFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        Log.i("ktian", "requestCode" + requestCode + ", resultCode =" + (resultCode==RESULT_OK ? "true":"false"));
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            if (mPhotoPath != null){
                Toast.makeText(context,mPhotoPath.toString(),Toast.LENGTH_SHORT).show();

                String json = FaceappDetect.detectRequest(mPhotoPath);

                if (json != null){
                    Toast.makeText(context, "json 正常",Toast.LENGTH_SHORT).show();
                    face_token2 = DetectJsonParse.getFaceToken(json);
                }else {
                    Toast.makeText(context, "json为null",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context,"photoPath为null",Toast.LENGTH_SHORT).show();
            }


//            cameraview.iview.setImageURI(Uri.fromFile(new File(cameraview.imagepath)));
//
//            Bitmap bmp = (Bitmap)data.getExtras().get("data");
//            Log.d("Test", "bmp width" + bmp.getWidth() + ",height:" + bmp.getHeight());
        }
    }
}
