package com.fhxh.shpptingsystem.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.ui.bean.PaintXY;
import com.fhxh.shpptingsystem.utils.ImgXYCalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TargetView extends AppCompatImageView {
    private Context context;

    public TargetView(Context context) {
        this(context, null);
    }

    public TargetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TargetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private Paint paint;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Path path;
    private Path path1;
    private Path path2;
    private Path path3;
    private Bitmap bitmap;
    private Bitmap bitmap1;

    private void init() {
        paint = new Paint();
        paint1 = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        path = new Path();
        path1 = new Path();
        path2 = new Path();
        path3 = new Path();

        bitmap = big(BitmapFactory.decodeResource(this.getResources(), R.mipmap.d1));
        bitmap1 = big(BitmapFactory.decodeResource(this.getResources(), R.mipmap.d2));
    }

    int widthMode;
    int widthSize;
    int heightMode;
    int heightSize;
    public ImgXYCalc imgXYCalc;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度测量模式
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //宽度测量大小
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //高度测量模式
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //高度测量大小
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        imgXYCalc = new ImgXYCalc(widthSize, heightSize, getResources());

        if (MeasureSpec.AT_MOST == widthMode || MeasureSpec.AT_MOST == heightMode) {
            int size = Math.min(widthSize, heightSize);
            setMeasuredDimension(size, size);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 轨迹集合
     */
    private List<PaintXY> listxy = new ArrayList<>();

    /**
     * 直接绘制
     *
     * @param list
     */
    public void setListxy(PaintXY list) {
        try {
            listxy.add(list);
        } catch (Exception e) {
            listxy.clear();
        }

        invalidate();
    }

    private boolean isPlayback = false;
    private int listSize = 0;

    public void setPlayback(boolean playback) {
        isPlayback = playback;
    }

    public boolean isPlayback() {
        return isPlayback;
    }

    public void setListxy(PaintXY list, int listSize) {
        this.listSize = listSize;
        isPlayback = true;
        listxy.add(list);
        invalidate();
    }

    public List<PaintXY> getListxy() {
        return listxy;
    }

    /**
     * 回放
     *
     * @param list
     */
    public void setPlayback(PaintXY list) {
        listxy.add(list);
        danxy.add(list);
        invalidate();
    }

    /**
     * 重置后绘制
     */
    public void reset() {
        listxy.clear();
        danxy.clear();
        path.reset();
        path1.reset();
        path2.reset();
        path3.reset();
        invalidate();
    }


    /**
     * 弹点集合
     */
    private List<PaintXY> danxy = new ArrayList<>();

    /**
     * 绘制弹点
     *
     * @param paintXY
     */
    public void setDanListxy(PaintXY paintXY) {
        isMoveTo = true;
        danxy.add(paintXY);
        invalidate();
    }

    public void setDanRest() {
        listxy.clear();
        path.reset();
        invalidate();
    }


    Canvas canvas;
    private boolean isMoveTo = false;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        paint.setStrokeWidth(3.0f);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(context.getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        paint1.setStrokeWidth(3.0f);
        paint1.setAntiAlias(true);
        paint1.setDither(true);
        paint1.setColor(context.getResources().getColor(R.color.colorHeGe));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeJoin(Paint.Join.ROUND);

        paint2.setStrokeWidth(3.0f);
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        paint2.setColor(context.getResources().getColor(R.color.colorBuHeGe));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);

        paint3.setStrokeWidth(3.0f);
        paint3.setAntiAlias(true);
        paint3.setDither(true);
        paint3.setColor(context.getResources().getColor(R.color.dove_gray));
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeJoin(Paint.Join.ROUND);


        int width = this.bitmap.getWidth() / 2;
        int height = this.bitmap.getHeight() / 2;
        int width1 = this.bitmap1.getWidth() / 2;
        int height1 = this.bitmap1.getHeight() / 2;

        int randomX = new Random().nextInt(5);
        int randomY = new Random().nextInt(5);
        if (isPlayback) {
            if (listxy.size() == 1) {
                if (listxy.get(0).getX() > 0 && listxy.get(0).getY() > 0) {
                    path.moveTo(listxy.get(0).getX() * widthSize, listxy.get(0).getY() * heightSize);
                }
            } else if (listxy.size() > 1) {
                float x = listxy.get(listxy.size() - 1).getX() * widthSize;
                float y = listxy.get(listxy.size() - 1).getY() * heightSize;
                if (listxy.get(listxy.size() - 2).getX() == 0) {
                    if (x != 0&&y!=0) {
                        path.moveTo(x, y);
                    }
                } else {
                    if (x > 0&&y!=0) {
                        if (listxy.size() - 1 > listSize - 10 && listxy.size() - 1 <= listSize - 4) {
                            //贝塞尔曲线
                            float startX = listxy.get(listxy.size() - 2).getX() * widthSize;
                            float startY = listxy.get(listxy.size() - 2).getY() * heightSize;
                            if (path1.isEmpty()) {
                                path1.moveTo(startX, startY);
                            } else {
                                path1.cubicTo(startX, startY,
                                        listxy.size() % 2 == 0 ? ((startX + x) / 2) + randomX : ((startX + x) / 2) - randomX,
                                        listxy.size() % 2 == 0 ? ((startY + y) / 2) + randomY : ((startY + y) / 2) - randomY,
                                        x, y);
                            }
                        } else if (listxy.size() - 2 >= listSize - 4 && listxy.size() - 1 <= listSize) {
                            //贝塞尔曲线
                            float startX = listxy.get(listxy.size() - 2).getX() * widthSize;
                            float startY = listxy.get(listxy.size() - 2).getY() * heightSize;
                            if (path2.isEmpty()) {
                                path2.moveTo(startX, startY);
                            } else {
                                path2.cubicTo(startX, startY,
                                        listxy.size() % 2 == 0 ? ((startX + x) / 2) + randomX : ((startX + x) / 2) - randomX,
                                        listxy.size() % 2 == 0 ? ((startY + y) / 2) + randomY : ((startY + y) / 2) - randomY,
                                        x, y);
                            }
                        } else if (listxy.size() - 2 >= listSize) {
                            //贝塞尔曲线
                            float startX = listxy.get(listxy.size() - 2).getX() * widthSize;
                            float startY = listxy.get(listxy.size() - 2).getY() * heightSize;
                            if (path3.isEmpty()) {
                                path3.moveTo(startX, startY);
                            } else {
                                path3.cubicTo(startX, startY,
                                        listxy.size() % 2 == 0 ? ((startX + x) / 2) + randomX : ((startX + x) / 2) - randomX,
                                        listxy.size() % 2 == 0 ? ((startY + y) / 2) + randomY : ((startY + y) / 2) - randomY,
                                        x, y);
                            }
                        } else {
                            //贝塞尔曲线
                            float startX = listxy.get(listxy.size() - 2).getX() * widthSize;
                            float startY = listxy.get(listxy.size() - 2).getY() * heightSize;
                            if (path.isEmpty()) {
                                path.moveTo(startX, startY);
                            } else {
                                path.cubicTo(startX, startY,
                                        listxy.size() % 2 == 0 ? ((startX + x) / 2) + randomX : ((startX + x) / 2) - randomX,
                                        listxy.size() % 2 == 0 ? ((startY + y) / 2) + randomY : ((startY + y) / 2) - randomY,
                                        x, y);
                            }
                        }
                    }
                    canvas.drawPath(path, paint);
                    canvas.drawPath(path1, paint1);
                    canvas.drawPath(path2, paint2);
                    canvas.drawPath(path3, paint3);
                }
            }
        } else {
            if (listxy.size() == 1) {
                float x = listxy.get(listxy.size() - 1).getX() * widthSize;
                float y = listxy.get(listxy.size() - 1).getY() * heightSize;
                path.moveTo(x, y);
            } else if (listxy.size() > 1) {
                float x = listxy.get(listxy.size() - 1).getX() * widthSize;
                float y = listxy.get(listxy.size() - 1).getY() * heightSize;
                if (listxy.get(listxy.size() - 2).getX() == 0) {
                    if (x != 0&&y!=0) {
                        isMoveTo = true;
                        path.moveTo(x, y);
                    }
                } else {
                    if (x != 0&&y!=0) {
                        //贝塞尔曲线
                        float startX = listxy.get(listxy.size() - 2).getX() * widthSize;
                        float startY = listxy.get(listxy.size() - 2).getY() * heightSize;
                        if (isMoveTo) {
                            if (path.isEmpty()) {
                                isMoveTo = true;
                                path.moveTo(x, y);
                            } else {
                                path.cubicTo(startX, startY,
                                        listxy.size() % 2 == 0 ? ((startX + x) / 2) + randomX : ((startX + x) / 2) - randomX,
                                        listxy.size() % 2 == 0 ? ((startY + y) / 2) + randomY : ((startY + y) / 2) - randomY,
                                        x, y);
                            }
                        } else {
                            isMoveTo = true;
                            path.moveTo(x, y);
                        }
                    }
                }
            }
            canvas.drawPath(path, paint);

        }


        for (int i = 0; i < danxy.size(); i++) {
            isPlayback = false;
            if (i == danxy.size() - 1) {
                canvas.drawBitmap(bitmap1, (danxy.get(i).getX() * widthSize) - width, (danxy.get(i).getY() * heightSize) - height, paint);
            } else {
                canvas.drawBitmap(bitmap, (danxy.get(i).getX() * widthSize) - width1, (danxy.get(i).getY() * heightSize) - height1, paint);
            }
        }

    }

    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.5f, 1.5f);  //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public double[] getViewRingNumber(float x, float y) {
        if (x <= 0f && y <= 0) {
            return new double[]{0.0, 0.0, 0.0, 0.0};
        } else {
            int xx= (int) (x*532);
            int yy= (int) (y*532);
            if (isInside(0,0,148,0,148,161,0,261,xx,yy)){
                return new double[]{0.0, 0.0, 0.0, 0.0};
            }
            if (isInside(351,0,532,0,532,261,351,161,xx,yy)){
                return new double[]{0.0, 0.0, 0.0, 0.0};
            }
            return imgXYCalc.getRingNumber(x, y);
        }

    }

    /**
     * 判断点是否在矩形区域内
     */
    public  boolean isInside(double x1, double y1, double x2, double y2,
                                   double x3, double y3, double x4, double y4,
                                   double x, double y) {
        if (y1 == y2) {
            return isInside(x1, y1, x4, y4, x, y);
        }
        double l = Math.abs(y4 - y3);
        double k = Math.abs(x4 - x3);
        double s = Math.sqrt(k * k + l * l);
        double sin = l / s;
        double cos = k / s;
        double x1R = cos * x1 + sin * y1;
        double y1R = -x1 * sin + y1 * cos;
        double x4R = cos * x4 + sin * y4;
        double y4R = -x4 * sin + y4 * cos;
        double xR = cos * x + sin * y;
        double yR = -x * sin + y * cos;
        return isInside(x1R, y1R, x4R, y4R, xR, yR);
    }
    public boolean isInside(double x1, double y1, double x4, double y4, double x, double y) {
        if (x <= x1) {
            return false;
        }
        if (x >= x4) {
            return false;
        }
        if (y >= y1) {
            return false;
        }
        if (y <= y4) {
            return false;
        }
        return true;
    }



    public int getViewDirection(float x, float y) {
        if (x > 0f && y > 0) {
            return imgXYCalc.getDirection(x, y);
        } else {
            return 0;
        }
    }

}
