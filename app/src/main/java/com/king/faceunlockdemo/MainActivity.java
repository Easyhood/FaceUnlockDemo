package com.king.faceunlockdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.FaceDetector;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback ,
Camera.PictureCallback{

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
    private SurfaceView mSurfaceView;

    private SurfaceHolder mHolder;

    private Handler mHandler = new Handler();

    private int mCurrenTimer = 3;

    private Camera mCamera;

    private boolean mlsSurfaceCreated = false;
    private boolean mlsTimerRunning = false;

    private boolean firstTakephoto = true;
    private File photoPath1;
    private File photoPath2;

    private static final int CAMERA_ID = 1; //前置摄像头


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
        mSurfaceView = findViewById(R.id.surface_view);

        FaceUtil.checkAndRequestPermission(context,200);

        initEvent();

        takePhoto1();
        takePhoto2();
        takeCompare();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopPreView();
    }

    private void takePhoto1() {

        btPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mlsTimerRunning){
                    mlsTimerRunning = true;
                    mHandler.post(timeRunnable);
                }

//                File photoPath = takePhoto();
//                if (photoPath != null){
//                    Toast.makeText(context,photoPath.toString(),Toast.LENGTH_SHORT).show();
//                    String json = FaceappDetect.detectRequest(photoPath);
//
//                    if (json != null){
//                        face_token1 = DetectJsonParse.getFaceToken(json);
//                    }else {
//                        Toast.makeText(context, "json为null",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(context,"photoPath为null",Toast.LENGTH_SHORT).show();
//                }



            }
        });

    }

    private void takePhoto2() {
        btPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mlsTimerRunning){
                    mlsTimerRunning = true;
                    mHandler.post(timeRunnable);
                }


//                File photoPath = takePhoto();
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



//    private File takePhoto(){
//        //相机拍照后照片保存的路径
//        File mPictureFile = new File(Environment.getExternalStorageDirectory(),
//                "picture" + System.currentTimeMillis() / 1000 + ".jpg");
//
//        //启动拍照，并保存到临时文件
//        Intent mIntent = new Intent();
//        Uri photoUri = FileProvider.getUriForFile(this,getPackageName()
//                +".provider",
//                mPictureFile);
//        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        mIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
//        mIntent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
//        mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//        startActivityForResult(mIntent,1);
//
//        return mPictureFile;
//    }

    private void initEvent() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mlsSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mlsSurfaceCreated = false;
    }

    private void startPreview() {
        if(mCamera != null || !mlsSurfaceCreated){
            Log.e("face","startPreview will return");
            return;
        }
        try {
            mCamera = Camera.open(CAMERA_ID);
            Camera.Parameters parameters = mCamera.getParameters();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            Camera.Size size = getBestPreviewSize(width, height, parameters);
            if (size != null){
                //设置预览分辨率
                parameters.setPreviewSize(size.width,size.height);
                //设置保存图片的大小
                parameters.setPictureSize(size.width,size.height);
            }

            //自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setPreviewFrameRate(3);

            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.set("jpeg-quality",85);



            //设置相机预览方向
            mCamera.setDisplayOrientation(90);

            mCamera.setParameters(parameters);
        }catch (Exception e){
            e.printStackTrace();
        }






        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("face", e.getMessage());
        }


    }

    private void stopPreView(){
        //释放Camera对象
        if (mCamera != null){
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }catch (Exception e){
                e.printStackTrace();
                Log.e("face",e.getMessage());
            }

        }
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {

        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }



    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCurrenTimer > 0){
                tvConResult.setText(mCurrenTimer+"");
                mCurrenTimer--;
                mHandler.postDelayed(timeRunnable,1000);
            }else {
                tvConResult.setText("");
                mCamera.takePicture(null,null,null,MainActivity.this);
                playSound();

                mlsTimerRunning =false;
                mCurrenTimer = 10;
            }
        }
    };

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            File mPictureFile = new File(Environment.getExternalStorageDirectory() + File.separator +
                            System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(mPictureFile);

            //旋转角度，保证保存的图片方向是对的
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            if (firstTakephoto){
                photoPath1 = mPictureFile;
                getFacetoken1(photoPath1);
                firstTakephoto =false;
            }else {
                photoPath2 = mPictureFile;
                getFacetoken2(photoPath2);
                firstTakephoto = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();

    }


    /**
     *   播放系统拍照声音
     */
    public void playSound() {
        MediaPlayer mediaPlayer = null;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0) {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(this,
                        Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    public void getFacetoken1(File photoPath){
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

    public void getFacetoken2(File photoPath){
        if (photoPath != null){
            Toast.makeText(context,photoPath.toString(),Toast.LENGTH_SHORT).show();
            String json = FaceappDetect.detectRequest(photoPath);

            if (json != null){
                face_token2 = DetectJsonParse.getFaceToken(json);
            }else {
                Toast.makeText(context, "json为null",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context,"photoPath为null",Toast.LENGTH_SHORT).show();
        }
    }
}
