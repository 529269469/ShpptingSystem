package com.fhxh.shpptingsystem.utils.usb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.RelativeLayout;

import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.room.entity.BulletBean;

import java.util.List;

public class USBView extends RelativeLayout {
    Context context;
    List<BulletBean> list;
    public USBView(Context context, List<BulletBean> list) {
        super(context);
        this.context = context;
        this.list = list;
    }

    public USBView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 142.170 为中点
     *
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.d1);
        Bitmap bitmaptarget3002 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.target3002);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(bitmaptarget3002, 0, 0, paint);
        for (int i = 0; i < list.size(); i++) {
            float x = ((list.get(i).getBullet_point_x()) * 350)-(bitmap.getWidth()/2);
            float y = ((list.get(i).getBullet_point_y()) * 350)-(bitmap.getHeight()/2);
            canvas.drawBitmap(bitmap, x, y, paint);
        }



    }
}
