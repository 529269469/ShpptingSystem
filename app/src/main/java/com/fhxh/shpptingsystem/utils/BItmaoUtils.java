package com.fhxh.shpptingsystem.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

public class BItmaoUtils {
    public static Bitmap getBitmapByScorw(View viewLayout) {
        Bitmap bmp = Bitmap.createBitmap(384, 384, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.WHITE);
        viewLayout.draw(canvas);
        return bmp;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param
     */
    public static Bitmap compressPic(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth;
        // 指定调整后的宽度和高度
        if (width >= 384) {
            newWidth = 384;
        } else {
            newWidth = width;
        }
        int newHeight = newWidth * height / width;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }
}
