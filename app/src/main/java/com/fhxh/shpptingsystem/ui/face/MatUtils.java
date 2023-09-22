package com.fhxh.shpptingsystem.ui.face;



import static org.opencv.core.CvType.CV_8UC4;

import static java.lang.System.currentTimeMillis;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MatUtils {

    private static final String TAG = "MatUtils";

    /**
     * 图片灰度
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap RGB2Gray(Bitmap photo) {
        Mat mat = new Mat(); //创建mat对象
        Utils.bitmapToMat(photo, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY); //灰度转换
        Imgproc.resize(mat,mat,new Size(50,50));
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        mat.release(); //释放
        return bitmap;
    }

    /**
     * 图片灰度
     *
     * @param mat mat
     * @return bitmap
     */
    public static Bitmap RGB2Gray(Mat mat) {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY); //灰度转换
//        Imgproc.resize(mat,mat,new Size(200,200));
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        mat.release(); //释放
        return bitmap;
    }

    /**
     * 黑白滤镜
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap binaryzation(Bitmap photo) {
        Mat mat = new Mat();
        Utils.bitmapToMat(photo, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
        Bitmap bitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        mat.release();
        return bitmap;
    }

    /**
     * 怀旧
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap remin(Bitmap photo) {
        Mat mat = new Mat();
        Utils.bitmapToMat(photo, mat);
        int channel = mat.channels(); //获取通道
        int width = mat.width();//获取宽
        int height = mat.height();//获取高
        Log.e(TAG, "channel: " + channel + "   width: " + width + "   height: " + height);

        byte[] p = new byte[channel];
        Mat matDst = new Mat(height, width, CV_8UC4);

        int b = 0, g = 0, r = 0;
        for (int row = 0; row < height; row++) {//行
            for (int col = 0; col < width; col++) {//列
                mat.get(row, col, p);
                b = p[0] & 0xff;
                g = p[1] & 0xff;
                r = p[2] & 0xff;

                int AB = (int) (0.272 * r + 0.534 * g + 0.131 * b);
                int AG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int AR = (int) (0.393 * r + 0.769 * g + 0.189 * b);

                AR = (AR > 255 ? 255 : (Math.max(AR, 0)));
                AG = (AG > 255 ? 255 : (Math.max(AG, 0)));
                AB = (AB > 255 ? 255 : (Math.max(AB, 0)));

                p[0] = (byte) AB;
                p[1] = (byte) AG;
                p[2] = (byte) AR;

                matDst.put(row, col, p);
            }
        }
        Log.e(TAG, "remin: " + matDst.width() + "    " + matDst.height());
        Bitmap bitmap = Bitmap.createBitmap(matDst.width(), matDst.height(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(matDst, matDst, Imgproc.COLOR_BGRA2BGR);
        Utils.matToBitmap(matDst, bitmap);
        mat.release();
        matDst.release();
        return bitmap;
    }

    /**
     * 连环画
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap cartoon(Bitmap photo) {
        Mat mat = new Mat();
        Utils.bitmapToMat(photo, mat);
        int channel = mat.channels(); //获取通道
        int width = mat.cols();//获取宽
        int height = mat.rows();//获取高
        Log.e(TAG, "channel: " + channel + "   width: " + width + "   height: " + height);
        byte[] p = new byte[width * channel];
        Mat matDst = new Mat(height, width, CV_8UC4);

        int b = 0, g = 0, r = 0;
        for (int row = 0; row < height; row++) {//行
            mat.get(row, 0, p);
            for (int col = 0; col < width; col++) {//列
                int index = channel * col;
                b = p[index] & 0xff;
                g = p[index + 1] & 0xff;
                r = p[index + 2] & 0xff;

                int abs = Math.abs(b - g + b + r);
                int AB = abs * g / 256;
                int AG = abs * r / 256;
                int AR = Math.abs(g - b + g + r) * r / 256;

                AR = (AR > 255 ? 255 : (Math.max(AR, 0)));
                AG = (AG > 255 ? 255 : (Math.max(AG, 0)));
                AB = (AB > 255 ? 255 : (Math.max(AB, 0)));

                p[index] = (byte) AB;
                p[index + 1] = (byte) AG;
                p[index + 2] = (byte) AR;


            }
            matDst.put(row, 0, p);
        }
        Bitmap bitmap = Bitmap.createBitmap(matDst.cols(), matDst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matDst, bitmap);
        mat.release();
        matDst.release();
        return bitmap;
    }

    /**
     * 浮雕
     *  TODO 不可用  内存消耗太大
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap emboss2(Bitmap photo) {
        Mat mat = new Mat();
        Utils.bitmapToMat(photo, mat);
        int channel = mat.channels(); //获取通道
        int width = mat.cols();//获取宽
        int height = mat.rows();//获取高
        Log.e(TAG, "channel: " + channel + "   width: " + width + "   height: " + height);
        byte[] p = new byte[width * channel];

        Mat matDst = new Mat(height, width, CV_8UC4);

        int b = 0, g = 0, r = 0;
        for (int row = 0; row < height; row++) {//行
            mat.get(row, 0, p);
            for (int col = 0; col < width; col++) {//列
                int index = channel * col;
                b = p[index] & 0xff;
                g = p[index + 1] & 0xff;
                r = p[index + 2] & 0xff;

                int AB = b + 128;
                int AG = g + 128;
                int AR = r + 128;

                AR = (AR > 255 ? 255 : (Math.max(AR, 0)));
                AG = (AG > 255 ? 255 : (Math.max(AG, 0)));
                AB = (AB > 255 ? 255 : (Math.max(AB, 0)));


                p[index] = (byte) AB;
                p[index + 1] = (byte) AG;
                p[index + 2] = (byte) AR;
            }
            matDst.put(row, 0, p);
        }


        Bitmap bitmap = Bitmap.createBitmap(matDst.cols(), matDst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matDst, bitmap);
        mat.release();
        matDst.release();
        return bitmap;
    }

    /**
     * 浮雕
     *  TODO 不可用  内存消耗太大
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap emboss(Bitmap photo) {
        Mat mat = new Mat();
        Utils.bitmapToMat(photo, mat);
        int channel = mat.channels(); //获取通道
        int width = mat.cols();//获取宽
        int height = mat.rows();//获取高
        Log.e(TAG, "channel: " + channel + "   width: " + width + "   height: " + height);
        byte[] p = new byte[width * height * channel];
        byte[] pDst = new byte[width * height * channel];

        Mat matDst = new Mat(height, width, CV_8UC4);

        int b1 = 0, g1 = 0, r1 = 0, b2 = 0, g2 = 0, r2 = 0;
        mat.get(0, 0, p);
        for (int row = 0; row < height; row++) {//行
            for (int col = 0; col < width; col++) {//列
                int index1 = channel * ((row + 1) * width + col + 1);
                b1 = p[index1] & 0xff;
                g1 = p[index1 + 1] & 0xff;
                r1 = p[index1 + 2] & 0xff;

                int index2 = Math.abs(channel * ((row - 1) * width + col - 1));
                b2 = p[index2] & 0xff;
                g2 = p[index2 + 1] & 0xff;
                r2 = p[index2 + 2] & 0xff;

                int AB = b1 - b2 + 128;
                int AG = g1 - g2 + 128;
                int AR = r1 - r2 + 128;

                AR = (AR > 255 ? 255 : (Math.max(AR, 0)));
                AG = (AG > 255 ? 255 : (Math.max(AG, 0)));
                AB = (AB > 255 ? 255 : (Math.max(AB, 0)));

                int index = channel * (row * width + col);

                pDst[index] = (byte) AB;
                pDst[index + 1] = (byte) AG;
                pDst[index + 2] = (byte) AR;
            }
        }
        matDst.put(0, 0, pDst);

        Bitmap bitmap = Bitmap.createBitmap(matDst.cols(), matDst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matDst, bitmap);
        mat.release();
        matDst.release();
        return bitmap;
    }


    /**
     * 人脸检测
     *
     * @param photo bitmap
     * @return bitmap
     */
    public static Bitmap faceDetect(Bitmap photo) {

        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Mat matGray = new Mat();
        Utils.bitmapToMat(photo, matSrc);
        Imgproc.cvtColor(matSrc, matGray, Imgproc.COLOR_BGRA2GRAY);
        MatOfRect matOfRect = new MatOfRect();
        cascadeClassifier.detectMultiScale(matGray, matOfRect, 1.05, 3, 0, new Size(3, 3), new Size());
        List<Rect> faceList = matOfRect.toList();
        matSrc.copyTo(matDst);
        if (faceList.size() > 0) {
            for (org.opencv.core.Rect rect : faceList) {
//                Imgproc.rectangle(matDst, rect.tl(), rect.br(), new Scalar(255, 0, 0, 255), 4, 8, 0);

//                Rect rect2 = new Rect((int)rect.tl().x, (int)rect.tl().y, (int)(rect.br().x-rect.tl().x), (int)(rect.br().y-rect.tl().y));
//                Mat  dstMat = new Mat(matSrc, rect2);
//                Bitmap resultBitmap = Bitmap.createBitmap(dstMat.width(), dstMat.height(), Bitmap.Config.ARGB_8888);
//                Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_BGR2RGB);
//                Utils.matToBitmap(dstMat, resultBitmap);
//                matSrc.release();
//                dstMat.release();
//                Bitmap resizedBitmap = resizeBitmap(resultBitmap, 300, 300);
//                return resizedBitmap;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matDst, bitmap);
        matSrc.release();
        matDst.release();
        matGray.release();
        return bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    private static CascadeClassifier cascadeClassifier;


    /**
     * 初始化人脸分类器
     */
    public static CascadeClassifier initClassifier(Context context,int rawId,String rawName) {
        try {
            InputStream is = context.getResources().openRawResource(rawId);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, rawName);
            FileOutputStream os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            cascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
            cascadeDir.delete();
            cascadeFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cascadeClassifier;
    }

    public static String saveMat(Mat srcImg) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String l = String.valueOf(currentTimeMillis());
        // 创建一个文件
        String fileName = l+".png";
        File file = new File(directory, fileName);
        //先把Mat转成Bitmap
        Bitmap mBitmap = null;
        mBitmap = Bitmap.createBitmap(srcImg.cols(), srcImg.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(srcImg, mBitmap);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();

    }


}
