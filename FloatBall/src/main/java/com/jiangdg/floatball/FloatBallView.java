package com.jiangdg.floatball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *   自定义悬浮球
 *
 * @author Jiangdg
 * @since  2019-08-07  15:29:00
 * */
public class FloatBallView extends View {
    private static final int WIDTH_OUT_CIRCLE = 2;
    private int mBallStyle;
    private int mWidth;
    private int mHeight;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mIconPaint;
    private boolean isDragger = false;
    private String mContentText = "100Kbps";


    public FloatBallView(Context context) {
        super(context);
        init();
    }

    public FloatBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidth = 120;
        mHeight = 120;
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.GRAY);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setFakeBoldText(true);
    }

    @Override
    protected void onMeasure(int mWidthMeasureSpec, int mHeightMeasureSpec) {
        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec);
        // 处理wrap_content情况
        int widthMode = MeasureSpec.getMode(mWidthMeasureSpec);
        int widthSize = MeasureSpec.getSize(mWidthMeasureSpec);
        int heightMode = MeasureSpec.getMode(mHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(mHeightMeasureSpec);
        if(widthMode==MeasureSpec.AT_MOST && heightMode==MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if(widthMode==MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if( heightMode==MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        } else {
            setMeasuredDimension(mWidth, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int radius = Math.max(viewWidth, viewHeight) / 2;
        if(isDragger) {
//            canvas.drawBitmap();
        } else {
            // 处理padding情况

            // 绘制图形
            mCirclePaint.setColor(Color.BLUE);
            mCirclePaint.setStrokeWidth(WIDTH_OUT_CIRCLE);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(viewWidth/2, viewWidth/2, radius-WIDTH_OUT_CIRCLE/2, mCirclePaint);

            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(Color.GRAY);
            canvas.drawCircle(viewWidth/2, viewWidth/2,radius-WIDTH_OUT_CIRCLE, mCirclePaint);

            float textWidth = mTextPaint.measureText(mContentText);
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float dy = -(fontMetrics.ascent + fontMetrics.descent) / 2;
            canvas.drawText(mContentText, viewWidth/2-textWidth/2, viewHeight/2+dy, mTextPaint);
        }
    }

    public void setDraggerFlag(boolean isDragger) {
        this.isDragger = isDragger;
        invalidate();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 开启动画
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止动画
    }
}
