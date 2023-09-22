package com.fhxh.shpptingsystem.utils;

import android.content.res.Resources;
import android.util.Log;

import com.fhxh.shpptingsystem.R;

/**
 * Created by  on 2021/6/29 11:30.
 */
public class ImgXYCalc {
    private static final String TAG ="ImgXYCalc" ;
    private Double[] imgXlist;
    private Double[] imgYlist;

    private int IMGWith;
    private int IMGHeight;

    private static final double centreXarg = 0.5;
    private static final double centreYarg = 0.594375;
    private double EveryPxEqMillimeter = 0.33333;

    private int CentreX;
    private int CentreY;

    public ImgXYCalc(int imgwith, int imgheight, Resources resources) {
        IMGWith = imgwith;
        IMGHeight = imgheight;
        EveryPxEqMillimeter = 0.33333333 * ( 1600.0 /((double) imgwith));
        if(imgwith < imgheight){
            CentreX = (int) ((double) imgwith * centreXarg);
            CentreY = (int) ((double) imgwith * centreYarg);
            CentreY = CentreY + (imgheight - imgwith)/2;
            EveryPxEqMillimeter = 0.33333333 * ( 1600.0 / ((double) imgwith));
        } else if (imgheight < imgwith) {
            CentreX = (int) ((double) imgheight * centreXarg);
            CentreY = (int) ((double) imgheight * centreYarg);
            CentreX = CentreX + (imgwith - imgheight) / 2;
            EveryPxEqMillimeter = 0.33333333 * (1600.0 / ((double) imgheight));
        } else {
            CentreX = (int) ((double) imgwith * centreXarg);
            CentreY = (int) ((double) imgheight * centreYarg);
        }
//        RxLogTool.e("IMGWith", IMGWith+"  "+CentreX+"  "+CentreY);
//        RxLogTool.e("IMGHeight", IMGHeight);
        String[] imgx = resources.getStringArray(R.array.img_x);
        String[] imgy = resources.getStringArray(R.array.img_y);
        imgXlist = new Double[438];
        imgYlist = new Double[438];
        for (int i = 0; i < imgx.length; i++) {
            imgXlist[i] = Double.parseDouble(imgx[i]);
            imgYlist[i] = Double.parseDouble(imgy[i]);
        }
    }

    public int getIMGWith() {
        return IMGWith;
    }

    public int getIMGHeight() {
        return IMGHeight;
    }



    public int getCalcX(double XX) {
        int XXX = -1;
        if (IMGHeight > IMGWith) {
            XXX = (int) (XX * ((double) IMGWith));
        } else if (IMGHeight < IMGWith) {
            XXX = (int) (XX * ((double) IMGHeight)) + ((IMGWith - IMGHeight) / 2);
        } else {
            XXX = (int) (XX * ((double) IMGWith));
        }
        return XXX;
    }

    public int getCalcY(double YY) {
        int YYY = -1;
        if (IMGHeight > IMGWith) {
            YYY = (int) (YY * ((double) IMGWith)) + ((IMGHeight - IMGWith) / 2);
        } else if (IMGHeight < IMGWith) {
            YYY = (int) (YY * ((double) IMGHeight));
        } else {
            YYY = (int) (YY * ((double) IMGHeight));
        }
        return YYY;
    }

    public double[] getRingNumber(float floatx, float floaty) {

        int x= (int) (floatx*IMGWith);
        int y= (int) (floaty*IMGHeight);
//        Log.e(TAG, "xy: "+x+"     "+y );
        double[] result = new double[4];
        double RingNumber = 0.0;
        double XRingNumber = 0.0;
        double YRingNumber = 0.0;
        double lineint = Math.sqrt(Math.pow(Math.abs(CentreX - x), 2) + Math.pow(Math.abs(CentreY - y), 2));
        double cendis = (lineint * EveryPxEqMillimeter) / 10.0;
        double Xlineint = Math.abs(CentreX - x);
        double Ylineint = Math.abs(CentreY - y);
        double LL10, LL9, LL8, LL7, LL6, LL5, LL4;
        double L1, L2, L3, L4, L5, L6, L7, L8, L9;
        LL10 = getComparisonValue(50);
        LL9 = getComparisonValue(50 + 53);
        LL8 = getComparisonValue(50 + 53 * 2);
        LL7 = getComparisonValue(50 + 53 * 3);
        LL6 = getComparisonValue(50 + 53 * 4);
        LL5 = getComparisonValue(50 + 53 * 5);
        LL4 = getComparisonValue(50 + 53 * 6);
        L1 = (LL10 / 10.0) * 9.0;
        L2 = (LL10 / 10.0) * 8.0;
        L3 = (LL10 / 10.0) * 7.0;
        L4 = (LL10 / 10.0) * 6.0;
        L5 = (LL10 / 10.0) * 5.0;
        L6 = (LL10 / 10.0) * 4.0;
        L7 = (LL10 / 10.0) * 3.0;
        L8 = (LL10 / 10.0) * 2.0;
        L9 = (LL10 / 10.0);
        if (lineint <= LL10) {     //解析10环
            if (lineint <= L9) {
                RingNumber = 10.9;
            } else if (lineint <= L8) {
                RingNumber = 10.8;
            } else if (lineint <= L7) {
                RingNumber = 10.7;
            } else if (lineint <= L6) {
                RingNumber = 10.6;
            } else if (lineint <= L5) {
                RingNumber = 10.5;
            } else if (lineint <= L4) {
                RingNumber = 10.4;
            } else if (lineint <= L3) {
                RingNumber = 10.3;
            } else if (lineint <= L2) {
                RingNumber = 10.2;
            } else if (lineint <= L1) {
                RingNumber = 10.1;
            } else {
                RingNumber = 10.0;
            }
        } else if (lineint <= LL9) {     //解析9环
            L1 = ((LL9 - LL10) / 10.0) * 9.0 + LL10;
            L2 = ((LL9 - LL10) / 10.0) * 8.0 + LL10;
            L3 = ((LL9 - LL10) / 10.0) * 7.0 + LL10;
            L4 = ((LL9 - LL10) / 10.0) * 6.0 + LL10;
            L5 = ((LL9 - LL10) / 10.0) * 5.0 + LL10;
            L6 = ((LL9 - LL10) / 10.0) * 4.0 + LL10;
            L7 = ((LL9 - LL10) / 10.0) * 3.0 + LL10;
            L8 = ((LL9 - LL10) / 10.0) * 2.0 + LL10;
            L9 = ((LL9 - LL10) / 10.0) + LL10;
            if (lineint <= L9) {
                RingNumber = 9.9;
            } else if (lineint <= L8) {
                RingNumber = 9.8;
            } else if (lineint <= L7) {
                RingNumber = 9.7;
            } else if (lineint <= L6) {
                RingNumber = 9.6;
            } else if (lineint <= L5) {
                RingNumber = 9.5;
            } else if (lineint <= L4) {
                RingNumber = 9.4;
            } else if (lineint <= L3) {
                RingNumber = 9.3;
            } else if (lineint <= L2) {
                RingNumber = 9.2;
            } else if (lineint <= L1) {
                RingNumber = 9.1;
            } else {
                RingNumber = 9.0;
            }
        } else if (lineint <= LL8) {     //解析8环
            L1 = ((LL8 - LL9) / 10.0) * 9.0 + LL9;
            L2 = ((LL8 - LL9) / 10.0) * 8.0 + LL9;
            L3 = ((LL8 - LL9) / 10.0) * 7.0 + LL9;
            L4 = ((LL8 - LL9) / 10.0) * 6.0 + LL9;
            L5 = ((LL8 - LL9) / 10.0) * 5.0 + LL9;
            L6 = ((LL8 - LL9) / 10.0) * 4.0 + LL9;
            L7 = ((LL8 - LL9) / 10.0) * 3.0 + LL9;
            L8 = ((LL8 - LL9) / 10.0) * 2.0 + LL9;
            L9 = ((LL8 - LL9) / 10.0) + LL9;
            if (lineint <= L9) {
                RingNumber = 8.9;
            } else if (lineint <= L8) {
                RingNumber = 8.8;
            } else if (lineint <= L7) {
                RingNumber = 8.7;
            } else if (lineint <= L6) {
                RingNumber = 8.6;
            } else if (lineint <= L5) {
                RingNumber = 8.5;
            } else if (lineint <= L4) {
                RingNumber = 8.4;
            } else if (lineint <= L3) {
                RingNumber = 8.3;
            } else if (lineint <= L2) {
                RingNumber = 8.2;
            } else if (lineint <= L1) {
                RingNumber = 8.1;
            } else {
                RingNumber = 8.0;
            }
        } else if (lineint <= LL7) {     //解析7环
            L1 = ((LL7 - LL8) / 10.0) * 9.0 + LL8;
            L2 = ((LL7 - LL8) / 10.0) * 8.0 + LL8;
            L3 = ((LL7 - LL8) / 10.0) * 7.0 + LL8;
            L4 = ((LL7 - LL8) / 10.0) * 6.0 + LL8;
            L5 = ((LL7 - LL8) / 10.0) * 5.0 + LL8;
            L6 = ((LL7 - LL8) / 10.0) * 4.0 + LL8;
            L7 = ((LL7 - LL8) / 10.0) * 3.0 + LL8;
            L8 = ((LL7 - LL8) / 10.0) * 2.0 + LL8;
            L9 = ((LL7 - LL8) / 10.0) + LL8;
            if (lineint <= L9) {
                RingNumber = 7.9;
            } else if (lineint <= L8) {
                RingNumber = 7.8;
            } else if (lineint <= L7) {
                RingNumber = 7.7;
            } else if (lineint <= L6) {
                RingNumber = 7.6;
            } else if (lineint <= L5) {
                RingNumber = 7.5;
            } else if (lineint <= L4) {
                RingNumber = 7.4;
            } else if (lineint <= L3) {
                RingNumber = 7.3;
            } else if (lineint <= L2) {
                RingNumber = 7.2;
            } else if (lineint <= L1) {
                RingNumber = 7.1;
            } else {
                RingNumber = 7.0;
            }
        } else if (lineint <= LL6) {     //解析6环
            L1 = ((LL6 - LL7) / 10.0) * 9.0 + LL7;
            L2 = ((LL6 - LL7) / 10.0) * 8.0 + LL7;
            L3 = ((LL6 - LL7) / 10.0) * 7.0 + LL7;
            L4 = ((LL6 - LL7) / 10.0) * 6.0 + LL7;
            L5 = ((LL6 - LL7) / 10.0) * 5.0 + LL7;
            L6 = ((LL6 - LL7) / 10.0) * 4.0 + LL7;
            L7 = ((LL6 - LL7) / 10.0) * 3.0 + LL7;
            L8 = ((LL6 - LL7) / 10.0) * 2.0 + LL7;
            L9 = ((LL6 - LL7) / 10.0) + LL7;
            if (lineint <= L9) {
                RingNumber = 6.9;
            } else if (lineint <= L8) {
                RingNumber = 6.8;
            } else if (lineint <= L7) {
                RingNumber = 6.7;
            } else if (lineint <= L6) {
                RingNumber = 6.6;
            } else if (lineint <= L5) {
                RingNumber = 6.5;
            } else if (lineint <= L4) {
                RingNumber = 6.4;
            } else if (lineint <= L3) {
                RingNumber = 6.3;
            } else if (lineint <= L2) {
                RingNumber = 6.2;
            } else if (lineint <= L1) {
                RingNumber = 6.1;
            } else {
                RingNumber = 6.0;
            }
        } else if (lineint <= LL5) {     //解析5环
            L1 = ((LL5 - LL6) / 10.0) * 9.0 + LL6;
            L2 = ((LL5 - LL6) / 10.0) * 8.0 + LL6;
            L3 = ((LL5 - LL6) / 10.0) * 7.0 + LL6;
            L4 = ((LL5 - LL6) / 10.0) * 6.0 + LL6;
            L5 = ((LL5 - LL6) / 10.0) * 5.0 + LL6;
            L6 = ((LL5 - LL6) / 10.0) * 4.0 + LL6;
            L7 = ((LL5 - LL6) / 10.0) * 3.0 + LL6;
            L8 = ((LL5 - LL6) / 10.0) * 2.0 + LL6;
            L9 = ((LL5 - LL6) / 10.0) + LL6;
            if (lineint <= L9) {
                RingNumber = 5.9;
            } else if (lineint <= L8) {
                RingNumber = 5.8;
            } else if (lineint <= L7) {
                RingNumber = 5.7;
            } else if (lineint <= L6) {
                RingNumber = 5.6;
            } else if (lineint <= L5) {
                RingNumber = 5.5;
            } else if (lineint <= L4) {
                RingNumber = 5.4;
            } else if (lineint <= L3) {
                RingNumber = 5.3;
            } else if (lineint <= L2) {
                RingNumber = 5.2;
            } else if (lineint <= L1) {
                RingNumber = 5.1;
            } else {
                RingNumber = 5.0;
            }
        } else if (lineint <= LL4) {     //解析4环
            L1 = ((LL4 - LL5) / 10.0) * 9.0 + LL5;
            L2 = ((LL4 - LL5) / 10.0) * 8.0 + LL5;
            L3 = ((LL4 - LL5) / 10.0) * 7.0 + LL5;
            L4 = ((LL4 - LL5) / 10.0) * 6.0 + LL5;
            L5 = ((LL4 - LL5) / 10.0) * 5.0 + LL5;
            L6 = ((LL4 - LL5) / 10.0) * 4.0 + LL5;
            L7 = ((LL4 - LL5) / 10.0) * 3.0 + LL5;
            L8 = ((LL4 - LL5) / 10.0) * 2.0 + LL5;
            L9 = ((LL4 - LL5) / 10.0) + LL5;
            if (lineint <= L9) {
                RingNumber = 4.9;
            } else if (lineint <= L8) {
                RingNumber = 4.8;
            } else if (lineint <= L7) {
                RingNumber = 4.7;
            } else if (lineint <= L6) {
                RingNumber = 4.6;
            } else if (lineint <= L5) {
                RingNumber = 4.5;
            } else if (lineint <= L4) {
                RingNumber = 4.4;
            } else if (lineint <= L3) {
                RingNumber = 4.3;
            } else if (lineint <= L2) {
                RingNumber = 4.2;
            } else if (lineint <= L1) {
                RingNumber = 4.1;
            } else {
                RingNumber = 4.0;
            }
        }

        L1 = (LL10 / 10.0) * 9.0;
        L2 = (LL10 / 10.0) * 8.0;
        L3 = (LL10 / 10.0) * 7.0;
        L4 = (LL10 / 10.0) * 6.0;
        L5 = (LL10 / 10.0) * 5.0;
        L6 = (LL10 / 10.0) * 4.0;
        L7 = (LL10 / 10.0) * 3.0;
        L8 = (LL10 / 10.0) * 2.0;
        L9 = (LL10 / 10.0);
        if (Xlineint <= LL10) {     //解析10环
            if (Xlineint <= L9) {
                XRingNumber = 10.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 10.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 10.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 10.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 10.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 10.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 10.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 10.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 10.1;
            } else {
                XRingNumber = 10.0;
            }
        } else if (Xlineint <= LL9) {     //解析9环
            L1 = ((LL9 - LL10) / 10.0) * 9.0 + LL10;
            L2 = ((LL9 - LL10) / 10.0) * 8.0 + LL10;
            L3 = ((LL9 - LL10) / 10.0) * 7.0 + LL10;
            L4 = ((LL9 - LL10) / 10.0) * 6.0 + LL10;
            L5 = ((LL9 - LL10) / 10.0) * 5.0 + LL10;
            L6 = ((LL9 - LL10) / 10.0) * 4.0 + LL10;
            L7 = ((LL9 - LL10) / 10.0) * 3.0 + LL10;
            L8 = ((LL9 - LL10) / 10.0) * 2.0 + LL10;
            L9 = ((LL9 - LL10) / 10.0) + LL10;
            if (Xlineint <= L9) {
                XRingNumber = 9.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 9.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 9.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 9.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 9.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 9.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 9.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 9.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 9.1;
            } else {
                XRingNumber = 9.0;
            }
        } else if (Xlineint <= LL8) {     //解析8环
            L1 = ((LL8 - LL9) / 10.0) * 9.0 + LL9;
            L2 = ((LL8 - LL9) / 10.0) * 8.0 + LL9;
            L3 = ((LL8 - LL9) / 10.0) * 7.0 + LL9;
            L4 = ((LL8 - LL9) / 10.0) * 6.0 + LL9;
            L5 = ((LL8 - LL9) / 10.0) * 5.0 + LL9;
            L6 = ((LL8 - LL9) / 10.0) * 4.0 + LL9;
            L7 = ((LL8 - LL9) / 10.0) * 3.0 + LL9;
            L8 = ((LL8 - LL9) / 10.0) * 2.0 + LL9;
            L9 = ((LL8 - LL9) / 10.0) + LL9;
            if (Xlineint <= L9) {
                XRingNumber = 8.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 8.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 8.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 8.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 8.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 8.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 8.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 8.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 8.1;
            } else {
                XRingNumber = 8.0;
            }
        } else if (Xlineint <= LL7) {     //解析7环
            L1 = ((LL7 - LL8) / 10.0) * 9.0 + LL8;
            L2 = ((LL7 - LL8) / 10.0) * 8.0 + LL8;
            L3 = ((LL7 - LL8) / 10.0) * 7.0 + LL8;
            L4 = ((LL7 - LL8) / 10.0) * 6.0 + LL8;
            L5 = ((LL7 - LL8) / 10.0) * 5.0 + LL8;
            L6 = ((LL7 - LL8) / 10.0) * 4.0 + LL8;
            L7 = ((LL7 - LL8) / 10.0) * 3.0 + LL8;
            L8 = ((LL7 - LL8) / 10.0) * 2.0 + LL8;
            L9 = ((LL7 - LL8) / 10.0) + LL8;
            if (Xlineint <= L9) {
                XRingNumber = 7.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 7.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 7.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 7.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 7.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 7.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 7.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 7.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 7.1;
            } else {
                XRingNumber = 7.0;
            }
        } else if (Xlineint <= LL6) {     //解析6环
            L1 = ((LL6 - LL7) / 10.0) * 9.0 + LL7;
            L2 = ((LL6 - LL7) / 10.0) * 8.0 + LL7;
            L3 = ((LL6 - LL7) / 10.0) * 7.0 + LL7;
            L4 = ((LL6 - LL7) / 10.0) * 6.0 + LL7;
            L5 = ((LL6 - LL7) / 10.0) * 5.0 + LL7;
            L6 = ((LL6 - LL7) / 10.0) * 4.0 + LL7;
            L7 = ((LL6 - LL7) / 10.0) * 3.0 + LL7;
            L8 = ((LL6 - LL7) / 10.0) * 2.0 + LL7;
            L9 = ((LL6 - LL7) / 10.0) + LL7;
            if (Xlineint <= L9) {
                XRingNumber = 6.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 6.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 6.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 6.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 6.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 6.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 6.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 6.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 6.1;
            } else {
                XRingNumber = 6.0;
            }
        } else if (Xlineint <= LL5) {     //解析5环
            L1 = ((LL5 - LL6) / 10.0) * 9.0 + LL6;
            L2 = ((LL5 - LL6) / 10.0) * 8.0 + LL6;
            L3 = ((LL5 - LL6) / 10.0) * 7.0 + LL6;
            L4 = ((LL5 - LL6) / 10.0) * 6.0 + LL6;
            L5 = ((LL5 - LL6) / 10.0) * 5.0 + LL6;
            L6 = ((LL5 - LL6) / 10.0) * 4.0 + LL6;
            L7 = ((LL5 - LL6) / 10.0) * 3.0 + LL6;
            L8 = ((LL5 - LL6) / 10.0) * 2.0 + LL6;
            L9 = ((LL5 - LL6) / 10.0) + LL6;
            if (Xlineint <= L9) {
                XRingNumber = 5.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 5.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 5.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 5.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 5.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 5.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 5.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 5.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 5.1;
            } else {
                XRingNumber = 5.0;
            }
        } else if (Xlineint <= LL4) {     //解析4环
            L1 = ((LL4 - LL5) / 10.0) * 9.0 + LL5;
            L2 = ((LL4 - LL5) / 10.0) * 8.0 + LL5;
            L3 = ((LL4 - LL5) / 10.0) * 7.0 + LL5;
            L4 = ((LL4 - LL5) / 10.0) * 6.0 + LL5;
            L5 = ((LL4 - LL5) / 10.0) * 5.0 + LL5;
            L6 = ((LL4 - LL5) / 10.0) * 4.0 + LL5;
            L7 = ((LL4 - LL5) / 10.0) * 3.0 + LL5;
            L8 = ((LL4 - LL5) / 10.0) * 2.0 + LL5;
            L9 = ((LL4 - LL5) / 10.0) + LL5;
            if (Xlineint <= L9) {
                XRingNumber = 4.9;
            } else if (Xlineint <= L8) {
                XRingNumber = 4.8;
            } else if (Xlineint <= L7) {
                XRingNumber = 4.7;
            } else if (Xlineint <= L6) {
                XRingNumber = 4.6;
            } else if (Xlineint <= L5) {
                XRingNumber = 4.5;
            } else if (Xlineint <= L4) {
                XRingNumber = 4.4;
            } else if (Xlineint <= L3) {
                XRingNumber = 4.3;
            } else if (Xlineint <= L2) {
                XRingNumber = 4.2;
            } else if (Xlineint <= L1) {
                XRingNumber = 4.1;
            } else {
                XRingNumber = 4.0;
            }
        }

        L1 = (LL10 / 10.0) * 9.0;
        L2 = (LL10 / 10.0) * 8.0;
        L3 = (LL10 / 10.0) * 7.0;
        L4 = (LL10 / 10.0) * 6.0;
        L5 = (LL10 / 10.0) * 5.0;
        L6 = (LL10 / 10.0) * 4.0;
        L7 = (LL10 / 10.0) * 3.0;
        L8 = (LL10 / 10.0) * 2.0;
        L9 = (LL10 / 10.0);
        if (Ylineint <= LL10) {     //解析10环
            if (Ylineint <= L9) {
                YRingNumber = 10.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 10.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 10.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 10.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 10.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 10.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 10.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 10.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 10.1;
            } else {
                YRingNumber = 10.0;
            }
        } else if (Ylineint <= LL9) {     //解析9环
            L1 = ((LL9 - LL10) / 10.0) * 9.0 + LL10;
            L2 = ((LL9 - LL10) / 10.0) * 8.0 + LL10;
            L3 = ((LL9 - LL10) / 10.0) * 7.0 + LL10;
            L4 = ((LL9 - LL10) / 10.0) * 6.0 + LL10;
            L5 = ((LL9 - LL10) / 10.0) * 5.0 + LL10;
            L6 = ((LL9 - LL10) / 10.0) * 4.0 + LL10;
            L7 = ((LL9 - LL10) / 10.0) * 3.0 + LL10;
            L8 = ((LL9 - LL10) / 10.0) * 2.0 + LL10;
            L9 = ((LL9 - LL10) / 10.0) + LL10;
            if (Ylineint <= L9) {
                YRingNumber = 9.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 9.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 9.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 9.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 9.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 9.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 9.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 9.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 9.1;
            } else {
                YRingNumber = 9.0;
            }
        } else if (Ylineint <= LL8) {     //解析8环
            L1 = ((LL8 - LL9) / 10.0) * 9.0 + LL9;
            L2 = ((LL8 - LL9) / 10.0) * 8.0 + LL9;
            L3 = ((LL8 - LL9) / 10.0) * 7.0 + LL9;
            L4 = ((LL8 - LL9) / 10.0) * 6.0 + LL9;
            L5 = ((LL8 - LL9) / 10.0) * 5.0 + LL9;
            L6 = ((LL8 - LL9) / 10.0) * 4.0 + LL9;
            L7 = ((LL8 - LL9) / 10.0) * 3.0 + LL9;
            L8 = ((LL8 - LL9) / 10.0) * 2.0 + LL9;
            L9 = ((LL8 - LL9) / 10.0) + LL9;
            if (Ylineint <= L9) {
                YRingNumber = 8.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 8.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 8.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 8.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 8.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 8.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 8.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 8.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 8.1;
            } else {
                YRingNumber = 8.0;
            }
        } else if (Ylineint <= LL7) {     //解析7环
            L1 = ((LL7 - LL8) / 10.0) * 9.0 + LL8;
            L2 = ((LL7 - LL8) / 10.0) * 8.0 + LL8;
            L3 = ((LL7 - LL8) / 10.0) * 7.0 + LL8;
            L4 = ((LL7 - LL8) / 10.0) * 6.0 + LL8;
            L5 = ((LL7 - LL8) / 10.0) * 5.0 + LL8;
            L6 = ((LL7 - LL8) / 10.0) * 4.0 + LL8;
            L7 = ((LL7 - LL8) / 10.0) * 3.0 + LL8;
            L8 = ((LL7 - LL8) / 10.0) * 2.0 + LL8;
            L9 = ((LL7 - LL8) / 10.0) + LL8;
            if (Ylineint <= L9) {
                YRingNumber = 7.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 7.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 7.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 7.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 7.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 7.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 7.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 7.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 7.1;
            } else {
                YRingNumber = 7.0;
            }
        } else if (Ylineint <= LL6) {     //解析6环
            L1 = ((LL6 - LL7) / 10.0) * 9.0 + LL7;
            L2 = ((LL6 - LL7) / 10.0) * 8.0 + LL7;
            L3 = ((LL6 - LL7) / 10.0) * 7.0 + LL7;
            L4 = ((LL6 - LL7) / 10.0) * 6.0 + LL7;
            L5 = ((LL6 - LL7) / 10.0) * 5.0 + LL7;
            L6 = ((LL6 - LL7) / 10.0) * 4.0 + LL7;
            L7 = ((LL6 - LL7) / 10.0) * 3.0 + LL7;
            L8 = ((LL6 - LL7) / 10.0) * 2.0 + LL7;
            L9 = ((LL6 - LL7) / 10.0) + LL7;
            if (Ylineint <= L9) {
                YRingNumber = 6.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 6.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 6.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 6.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 6.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 6.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 6.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 6.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 6.1;
            } else {
                YRingNumber = 6.0;
            }
        } else if (Ylineint <= LL5) {     //解析5环
            L1 = ((LL5 - LL6) / 10.0) * 9.0 + LL6;
            L2 = ((LL5 - LL6) / 10.0) * 8.0 + LL6;
            L3 = ((LL5 - LL6) / 10.0) * 7.0 + LL6;
            L4 = ((LL5 - LL6) / 10.0) * 6.0 + LL6;
            L5 = ((LL5 - LL6) / 10.0) * 5.0 + LL6;
            L6 = ((LL5 - LL6) / 10.0) * 4.0 + LL6;
            L7 = ((LL5 - LL6) / 10.0) * 3.0 + LL6;
            L8 = ((LL5 - LL6) / 10.0) * 2.0 + LL6;
            L9 = ((LL5 - LL6) / 10.0) + LL6;
            if (Ylineint <= L9) {
                YRingNumber = 5.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 5.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 5.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 5.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 5.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 5.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 5.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 5.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 5.1;
            } else {
                YRingNumber = 5.0;
            }
        } else if (Ylineint <= LL4) {     //解析4环
            L1 = ((LL4 - LL5) / 10.0) * 9.0 + LL5;
            L2 = ((LL4 - LL5) / 10.0) * 8.0 + LL5;
            L3 = ((LL4 - LL5) / 10.0) * 7.0 + LL5;
            L4 = ((LL4 - LL5) / 10.0) * 6.0 + LL5;
            L5 = ((LL4 - LL5) / 10.0) * 5.0 + LL5;
            L6 = ((LL4 - LL5) / 10.0) * 4.0 + LL5;
            L7 = ((LL4 - LL5) / 10.0) * 3.0 + LL5;
            L8 = ((LL4 - LL5) / 10.0) * 2.0 + LL5;
            L9 = ((LL4 - LL5) / 10.0) + LL5;
            if (Ylineint <= L9) {
                YRingNumber = 4.9;
            } else if (Ylineint <= L8) {
                YRingNumber = 4.8;
            } else if (Ylineint <= L7) {
                YRingNumber = 4.7;
            } else if (Ylineint <= L6) {
                YRingNumber = 4.6;
            } else if (Ylineint <= L5) {
                YRingNumber = 4.5;
            } else if (Ylineint <= L4) {
                YRingNumber = 4.4;
            } else if (Ylineint <= L3) {
                YRingNumber = 4.3;
            } else if (Ylineint <= L2) {
                YRingNumber = 4.2;
            } else if (Ylineint <= L1) {
                YRingNumber = 4.1;
            } else {
                YRingNumber = 4.0;
            }
        }

        result[0] = RingNumber;
        result[1] = XRingNumber;
        result[2] = YRingNumber;
        result[3] = cendis;

        return result;
    }

    public int getDirection(float floatx, float floaty) {
        if (floatx<=0.0f&&floaty<=0.0f){
            return 0;
        }
        int x= (int) (floatx*IMGWith);
        int y= (int) (floaty*IMGHeight);
        int Direction = 0;
        double angle = 0.0;
        if ((x - CentreX) != 0 && (y - CentreY) != 0) {
            if ((x - CentreX) > 0 && (y - CentreY) < 0) {     //1象限
                angle = Math.toDegrees(Math.atan(((double) (x - CentreX)) / ((double) (CentreY - y))));
                if (angle <= 15.0) {
                    Direction = 12;
                } else if (angle > 15.0 && angle <= 45.0) {
                    Direction = 1;
                } else if (angle > 45.0 && angle <= 75.0) {
                    Direction = 2;
                } else {
                    Direction = 3;
                }
            } else if ((x - CentreX) > 0 && (y - CentreY) > 0) {     //2象限
                angle = Math.toDegrees(Math.atan(((double) (y - CentreY)) / ((double) (x - CentreX))));
                if (angle <= 15.0) {
                    Direction = 3;
                } else if (angle > 15.0 && angle <= 45.0) {
                    Direction = 4;
                } else if (angle > 45.0 && angle <= 75.0) {
                    Direction = 5;
                } else {
                    Direction = 6;
                }
            } else if ((x - CentreX) < 0 && (y - CentreY) > 0) {     //3象限
                angle = Math.toDegrees(Math.atan(((double) (CentreX - x)) / ((double) (y - CentreY))));
                if (angle <= 15.0) {
                    Direction = 6;
                } else if (angle > 15.0 && angle <= 45.0) {
                    Direction = 7;
                } else if (angle > 45.0 && angle <= 75.0) {
                    Direction = 8;
                } else {
                    Direction = 9;
                }
            } else if ((x - CentreX) < 0 && (y - CentreY) < 0) {     //4象限
                angle = Math.toDegrees(Math.atan(((double) (CentreY - y)) / ((double) (CentreX - x))));
                if (angle <= 15.0) {
                    Direction = 9;
                } else if (angle > 15.0 && angle <= 45.0) {
                    Direction = 10;
                } else if (angle > 45.0 && angle <= 75.0) {
                    Direction = 11;
                } else {
                    Direction = 12;
                }
            }
        } else {
            if ((x - CentreX) == 0 && (y - CentreY) != 0) {       //Y轴
                if ((y - CentreY) > 0) {      //y轴-
                    Direction = 6;
                } else {                       //y轴+
                    Direction = 12;
                }
            } else if ((x - CentreX) != 0 && (y - CentreY) == 0) {  //X轴
                if ((x - CentreX) > 0) {      //x轴+
                    Direction = 3;
                } else {                       //x轴-
                    Direction = 9;
                }
            }
        }
        return Direction;
    }

    private double getComparisonValue(int v) {
        double k = (double) v;
        if (IMGHeight > IMGWith) {
            k = (double) ((((double) v) / 1600.0) * IMGWith);
        } else if (IMGWith > IMGHeight) {
            k = (double) ((((double) v) / 1600.0) * IMGHeight);
        }

        return k;
    }

    public int getCentreX() {
        return CentreX;
    }

    public int getCentreY() {
        return CentreY;
    }

    public double getEveryPxEqMillimeter() {
        return EveryPxEqMillimeter;
    }

    public static float getRandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }
}
