package com.fhxh.shpptingsystem.ui.face;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.fhxh.shpptingsystem.MyApp;
import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.room.AppDatabase;
import com.fhxh.shpptingsystem.room.dao.UserDao;
import com.fhxh.shpptingsystem.room.entity.User;
import com.fhxh.shpptingsystem.ui.MainActivity;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
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
import java.util.ArrayList;
import java.util.List;

public class Open2FaceActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindowSettings();
        setContentView(R.layout.activity_open_face);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        openCvCameraView = findViewById(R.id.activity_main_camera_view);
        demo4_cv_iv_qiehuan = findViewById(R.id.demo4_cv_iv_qiehuan);
        openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        openCvCameraView.setCameraPermissionGranted();
        openCvCameraView.setCvCameraViewListener(this);

        demo4_cv_iv_qiehuan.setOnClickListener(v -> {

        });

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
    private int num = 0;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        matRgba = inputFrame.rgba();
        if (!matRgba.empty() && num < 5) {
            Imgproc.cvtColor(matRgba, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
            MatOfRect faces = new MatOfRect();
            if (cascadeClassifier != null) {
                cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.05, 3, 0,
                        new Size(absoluteFaceSize, absoluteFaceSize), new Size());
            }
            Rect[] facesArray = faces.toArray();
            for (int i = 0; i < facesArray.length; i++) {
                Imgproc.rectangle(matRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
                Mat savemat = new Mat(matRgba, facesArray[i].clone());
                detectFace(savemat);

            }
        }
        return matRgba;
    }

    public void detectFace(final Mat source) {
        new Thread(() -> {
            try {
                Mat mat = new Mat();
                Size size = new Size(100, 100);
                Imgproc.resize(source, mat, size);

                AppDatabase users_dp = Room.databaseBuilder(
                        MyApp.getApplication(),
                        AppDatabase.class, "users_dp"
                ).build();
                UserDao userDao = users_dp.userDao();
                List<User> all = userDao.getAll();
                try {
                    for (int i = 0; i < all.size(); i++) {
                        if (all.get(i).getPath() != null) {
                            String[] split = all.get(i).getPath().split(",");
                            int finalI = i;
                            new Thread(() -> {
                                for (int i1 = 0; i1 < split.length; i1++) {
                                    File file = new File(split[i1]);
                                    if (file.exists()){
                                        Log.e("TAG", "detectFace: "+file.getPath() );
                                        Bitmap cacheBitmap = BitmapFactory.decodeFile(file.getPath());
                                        Mat mat1 = new Mat();
                                        Utils.bitmapToMat(cacheBitmap, mat1);
                                        double similar = check(mat, mat1);
                                        if (similar > 0.7) {
                                            MMKV mmkv = MMKV.defaultMMKV();
                                            mmkv.encode("Name", all.get(finalI).getUser_name());
                                            runOnUiThread(() -> {
                                                ToastUtils.show("登录成功");
                                                startActivity(new Intent(Open2FaceActivity.this, MainActivity.class).putExtra("id",all.get(finalI).getUid()));
                                                finish();
                                            });
                                            return;
                                        }
                                    }
                                }
                            }).start();
                        }
                    }

                } catch (Exception e) {

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error2", "error set:" + e.getMessage());
            }
        }).start();


    }

    /**
     * 检测特征值
     */
    public double check(Mat mat1, Mat mat2) {
        //开始检测
        Mat src1 = new Mat();
        Imgproc.cvtColor(mat1, src1, Imgproc.COLOR_BGR2GRAY);
        Mat target1 = new Mat();
        Imgproc.cvtColor(mat2, target1, Imgproc.COLOR_BGR2GRAY);

        src1.convertTo(src1, CvType.CV_32F);
        target1.convertTo(target1, CvType.CV_32F);
        double similar = Imgproc.compareHist(src1, target1, Imgproc.CV_COMP_CORREL);
        Log.e("TAG", "相似度 ： ==" + similar);
        src1.release();
        target1.release();

        return similar;
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

        init();
    }

    /**
     * 初始化
     */
    private List<Mat> matList = new ArrayList<>();

    private void init() {
        matList.clear();
        try {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            path.mkdirs();
            File[] allFile = path.listFiles();
            for (File cache : allFile) {
                Bitmap cacheBitmap = BitmapFactory.decodeFile(cache.getPath());
                Mat mat1 = new Mat();
                Utils.bitmapToMat(cacheBitmap, mat1);
                Mat result = new Mat();
                Imgproc.cvtColor(mat1, result, Imgproc.COLOR_BGR2GRAY);
                matList.add(result);
            }
        } catch (Exception e) {

        }
    }


    private void initWindowSettings() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

}
