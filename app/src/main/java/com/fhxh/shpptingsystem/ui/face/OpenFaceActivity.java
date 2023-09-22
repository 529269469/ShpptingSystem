package com.fhxh.shpptingsystem.ui.face;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.fhxh.shpptingsystem.MyApp;
import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.room.AppDatabase;
import com.fhxh.shpptingsystem.room.dao.UserDao;
import com.fhxh.shpptingsystem.room.entity.User;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class OpenFaceActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCamera2View openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    private ImageView demo4_cv_iv_qiehuan;

    private void initializeOpenCVDependencies() {
        try {
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        openCvCameraView.enableView();
    }

    private Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindowSettings();
        setContentView(R.layout.activity_open_face);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        id=getIntent().getLongExtra("id",0);
        openCvCameraView = findViewById(R.id.activity_main_camera_view);
        demo4_cv_iv_qiehuan = findViewById(R.id.demo4_cv_iv_qiehuan);
        openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        openCvCameraView.setCameraPermissionGranted();
        openCvCameraView.setCvCameraViewListener(this);

        demo4_cv_iv_qiehuan.setOnClickListener(v -> {

        });

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                isSuccess=true;
            }
        };

        timer.schedule(timerTask,500,1000);

    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        matRgba = new Mat();
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int) (height * 0.2);
    }

    @Override
    public void onCameraViewStopped() {
        matRgba.release();
    }

    private Mat matRgba;

    private boolean isSuccess;
    private int num=0;
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        matRgba = inputFrame.rgba();
        if (isSuccess&&!matRgba.empty()&&num<3) {
            isSuccess=false;
            Imgproc.cvtColor(matRgba, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
            MatOfRect faces = new MatOfRect();
            if (cascadeClassifier != null) {
                cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.05, 3, 0,
                        new Size(absoluteFaceSize, absoluteFaceSize), new Size());
            }
            Rect[] facesArray = faces.toArray();
            StringBuffer stringBuffer=new StringBuffer();
            for (int i = 0; i < facesArray.length; i++) {
                Imgproc.rectangle(matRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
                Mat savemat = new Mat(matRgba, facesArray[i].clone());
                Mat mat = new Mat();
                Size size = new Size(100, 100);
                Imgproc.resize(savemat, mat, size);

                String s = MatUtils.saveMat(mat);
                Log.e("TAG", "onCreate: " + s);
                stringBuffer.append(s).append(",");
                num++;
                if (num==3){
                    String s1 = stringBuffer.toString();
                    String substring = s1.substring(0, s1.length() - 1);
                    AppDatabase users_dp = Room.databaseBuilder(
                            MyApp.getApplication(),
                            AppDatabase.class, "users_dp"
                    ).build();

                    new Thread(() -> {
                        UserDao userDao = users_dp.userDao();
                        User user = userDao.loadAllById(id);
                        user.setPath(substring);
                        userDao.update(user);
                        runOnUiThread(() -> {
                            ToastUtils.show("录入完成");
                            finish();
                        });
                    }).start();

                }
            }
        }
        return matRgba;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
        }
        initializeOpenCVDependencies();
        final String[] permissions = {
                Manifest.permission.CAMERA
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 0);
        }
    }



    private void initWindowSettings() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

}
