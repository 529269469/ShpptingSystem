package com.fhxh.shpptingsystem.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.fhxh.shpptingsystem.ui.bean.PaintXY;
import com.fhxh.shpptingsystem.view.TargetView;

import java.util.ArrayList;
import java.util.List;

public class AnalysisAchievementUtils2 {

    private static String TAG = "AnalysisAchievementUtils";

    @SuppressLint("NewApi")
    public static int[] setEvement(List<PaintXY> analysis, double ringNumber, TargetView mainTargetView) {
        int[] achieves = new int[5];
        if (ringNumber == 0) {
            achieves[0] = 0;
            achieves[1] = 0;
            achieves[2] = 0;
            achieves[3] = 0;
            achieves[4] = 0;
            return achieves;
        }

        /**
         * 据枪 获取击发前2秒40个点的位置，当前点减去前一个点，获取到行程，根据行程来判断成绩
         */
        if (analysis.size() > 25) {
            List<PaintXY> miaozhuList = new ArrayList<>();
            for (int i = analysis.size() - 25; i < analysis.size() - 6; i++) {
                miaozhuList.add(analysis.get(i));
            }
            float xThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).max().getAsDouble();

            float v = ((xThemMax - xThemMin) + (yThemMax - yThemMin)) / 2;
            achieves[0] = (int) ((1 - v) * 100);

        } else if (analysis.size() > 6 && analysis.size() < 25) {
            List<PaintXY> miaozhuList = new ArrayList<>();
            for (int i = 0; i < analysis.size() - 6; i++) {
                miaozhuList.add(analysis.get(i));
            }
            float xThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).max().getAsDouble();

            float v = ((xThemMax - xThemMin) + (yThemMax - yThemMin)) / 2;
            achieves[0] = (int) ((1 - v) * 100);
        } else {
            float xThemMin = (float) analysis.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) analysis.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) analysis.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) analysis.stream().mapToDouble(PaintXY::getY).max().getAsDouble();

            float v = ((xThemMax - xThemMin) + (yThemMax - yThemMin)) / 2;
            achieves[0] = (int) ((1 - v) * 100);
        }

        /**
         * 瞄准
         * 击发前2秒  以10环为中心，算成绩 瞄准离中心越远分数越低，脱靶位0
         */
        if (analysis.size() > 25) {
            List<PaintXY> miaozhuList = new ArrayList<>();
            for (int i = analysis.size() - 25; i < analysis.size() - 6; i++) {
                miaozhuList.add(analysis.get(i));
            }
            float xThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).max().getAsDouble();

            float xx = (xThemMax - xThemMin) / 2 + xThemMin;
            float yy = (yThemMax - yThemMin) / 2 + yThemMax;
            double[] viewRingNumber = mainTargetView.getViewRingNumber(xx, yy);

            achieves[1] = (int) (viewRingNumber[0] / 10.9 * 100);

        } else {
            float xThemMin = (float) analysis.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) analysis.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) analysis.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) analysis.stream().mapToDouble(PaintXY::getY).max().getAsDouble();
            float xx = (xThemMax - xThemMin) / 2 + xThemMin;
            float yy = (yThemMax - yThemMin) / 2 + yThemMax;
            double[] viewRingNumber = mainTargetView.getViewRingNumber(xx, yy);
            achieves[1] = (int) (viewRingNumber[0] / 10.9 * 100);

        }


        /**
         * 击发
         *
         * 只要上靶为60
         *
         *
         */
        if (analysis.size() > 6) {
            List<PaintXY> miaozhuList = new ArrayList<>();
            for (int i = analysis.size() - 6; i < analysis.size(); i++) {
                miaozhuList.add(analysis.get(i));
            }
            float xThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) miaozhuList.stream().mapToDouble(PaintXY::getY).max().getAsDouble();
            float v = ((xThemMax - xThemMin) + (yThemMax - yThemMin)) / 2;
            achieves[2] = (int) ((1 - v) * 100);

        } else {
            float xThemMin = (float) analysis.stream().mapToDouble(PaintXY::getX).min().getAsDouble();
            float yThemMin = (float) analysis.stream().mapToDouble(PaintXY::getY).min().getAsDouble();
            float xThemMax = (float) analysis.stream().mapToDouble(PaintXY::getX).max().getAsDouble();
            float yThemMax = (float) analysis.stream().mapToDouble(PaintXY::getY).max().getAsDouble();
            float v = ((xThemMax - xThemMin) + (yThemMax - yThemMin)) / 2;
            achieves[2] = (int) ((1 - v) * 100);
        }

        /**
         * 成绩
         */

        achieves[3] = (int) (ringNumber / 10.9 * 100);

        /**
         * 总体
         */
        achieves[4] = (int) ((achieves[0] * 0.15 + achieves[1] * 0.2 + achieves[2] * 0.15 + achieves[3] * 0.5));

        Log.e(TAG, "setEvement: " + achieves[0] + "  " + achieves[1] + "  " + achieves[2] + "  " + achieves[3] + "  " + achieves[4]);

        return achieves;
    }

}
