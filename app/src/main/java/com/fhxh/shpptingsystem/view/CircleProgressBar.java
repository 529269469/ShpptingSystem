package com.fhxh.shpptingsystem.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.utils.AnalysisAchievement;


/**
 * Created by  on 2021/7/9 12:14.
 */
public class CircleProgressBar extends View {
    private Context mContext;
    private Paint mPaint;
    private int mProgress = 0;
    private static int MAX_PROGRESS = 100;
    /**
     * 弧度
     */
    private int mAngle;
    /**
     * 中间的文字
     */
    private String mText;
    /**
     * 外圆颜色
     */
    private int outRoundColor;
    /**
     * 内圆的颜色
     */
    private int inRoundColor;
    /**
     * 线的宽度
     */
    private int roundWidth;
    private int style;
    /**
     * 字体颜色
     */
    private int textColor;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 字体是否加粗
     */
    private boolean isBold;

    /**
     * 进度条颜色
     */
    private int progressBarColor;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    @TargetApi(21)
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    /**
     * 解析自定义属性
     *
     * @param attrs
     */
    public void init(AttributeSet attrs) {
        mPaint = new Paint();
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        outRoundColor = typedArray.getColor(R.styleable.CircleProgressBar_outCircleColor, getResources().getColor(R.color.outRoundColor));
        inRoundColor = typedArray.getColor(R.styleable.CircleProgressBar_inCircleColor, getResources().getColor(R.color.inRoundColor));
        progressBarColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, getResources().getColor(R.color.colorAccent));
        isBold = typedArray.getBoolean(R.styleable.CircleProgressBar_textBold, false);
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK);
        roundWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBar_lineWidth, 20);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 画外圆
         */
        super.onDraw(canvas);
        int center = getWidth() / 2;            //圆心
        int radius = (center - roundWidth / 2); //半径
        mPaint.setColor(outRoundColor);         //外圆颜色
        mPaint.setStrokeWidth(roundWidth);      //线的宽度
        mPaint.setStyle(Paint.Style.STROKE);    //空心圆
        mPaint.setAntiAlias(true);              //消除锯齿
        canvas.drawCircle(center, center, radius, mPaint);
        //内圆
        mPaint.setColor(inRoundColor);
        radius = radius - roundWidth;
        canvas.drawCircle(center, center, radius, mPaint);

        mPaint.setStrokeWidth(7);
        //画进度是一个弧线
        if (mProgress >= (int) AnalysisAchievement.TotalFractions_YOUXIU) {
            mPaint.setColor(getResources().getColor(R.color.colorYouXiu));
        } else if (mProgress >= (int) AnalysisAchievement.TotalFractions_LIANGHAO) {
            mPaint.setColor(getResources().getColor(R.color.colorLiangHao));
        } else if (mProgress >= (int) AnalysisAchievement.TotalFractions_HEGE) {
            mPaint.setColor(getResources().getColor(R.color.colorHeGe));
        } else {
            mPaint.setColor(getResources().getColor(R.color.colorBuHeGe));
        }

        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);//圆弧范围的外接矩形
        canvas.drawArc(rectF, -90, mAngle, false, mPaint);
        canvas.save(); //平移画布之前保存之前画的

        //画进度终点的小球,旋转画布的方式实现
        mPaint.setStyle(Paint.Style.FILL);
        //将画布坐标原点移动至圆心
        canvas.translate(center, center);
        //旋转和进度相同的角度，因为进度是从-90度开始的所以-90度
        canvas.rotate(mAngle - 90);
        //同理从圆心出发直接将原点平移至要画小球的位置
        canvas.translate(radius, 0);
        canvas.drawCircle(0, 0, roundWidth, mPaint);
        //画完之后恢复画布坐标
        canvas.restore();

        //画文字将坐标平移至圆心
        canvas.translate(center, center);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);
        if (isBold) {
            //字体加粗
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if (TextUtils.isEmpty(mText)) {
            mText = mProgress + "%";
        }
        //动态设置文字长为圆半径，计算字体大小
        float textLength = mText.length();
        textSize = (radius / textLength) * 3.0f;
        mPaint.setTextSize(textSize);
        //将文字画到中间
//        float textWidth = mPaint.measureText(mText);
        TextPaint textPaint = new TextPaint();
        if (mProgress >= (int) AnalysisAchievement.TotalFractions_YOUXIU) {
            textPaint.setColor(getResources().getColor(R.color.colorYouXiu));
        } else if (mProgress >= (int) AnalysisAchievement.TotalFractions_LIANGHAO) {
            textPaint.setColor(getResources().getColor(R.color.colorLiangHao));
        } else if (mProgress >= (int) AnalysisAchievement.TotalFractions_HEGE) {
            textPaint.setColor(getResources().getColor(R.color.colorHeGe));
        } else {
            textPaint.setColor(getResources().getColor(R.color.colorBuHeGe));
        }
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        float textWidth = textPaint.measureText(mText);
        StaticLayout layout = new StaticLayout(mText, textPaint, (int) textWidth,
                Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate(-textWidth / 2, -textSize - textSize / 4.5F);
        layout.draw(canvas);
        canvas.restore();
//        canvas.drawText(mText, -textWidth / 2, textSize / 2, mPaint);
    }


    public int getmProgress() {
        return mProgress;
    }

    /**
     * 设置进度
     *
     * @return
     */
    public void setmProgress(int p) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
            mAngle = 360;
        } else {
            mProgress = p;
            mAngle = 360 * p / MAX_PROGRESS;
        }
    }


    public String getmText() {
        return mText;
    }

    /**
     * 设置文本
     *
     * @param mText
     */
    public void setmText(String mText) {
        this.mText = mText;
    }

    /**
     * 设置带动画的进度
     *
     * @param p
     */
    public void setAnimProgress(int p, String tital) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
        } else {
            mProgress = p;
        }
        //设置属性动画
        ValueAnimator valueAnimator = new ValueAnimator().ofInt(0, mProgress);
        //动画从快到慢
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);
        //监听值的变化
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentV = (Integer) animation.getAnimatedValue();
//                RxLogTool.e("fwc", "current" + currentV);
                mAngle = 360 * currentV / MAX_PROGRESS;
                mText = tital + currentV + "%";
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
