package com.wangyi.news.splash.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wangyi.news.splash.listener.OnRingClickListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 熊亦涛
 * @time 16/7/27  13:33
 * @desc ${TODD}
 */
public class RingTextView extends View {
    private Paint ringPaint;
    private RectF mRectF;
    private Paint circlePaint;
    private TextPaint mTextPaint;
    private String content = "跳过";
    private int padding = 5;
    private int radius;
    private int sweepAngle;
    private Timer mTimer;
    private int period = 20;//每次执行的间隔,单位毫秒

    /**
     * 添加点击监听器
     */
    private OnRingClickListener onRingClickListener;

    public void setOnRingClickListener(OnRingClickListener onRingClickListener) {
        this.onRingClickListener = onRingClickListener;
    }

    public RingTextView(Context context) {
        super(context);
    }

    public RingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //文字的Paint
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(18);
        mTextPaint.setColor(Color.WHITE);
        //测量文字的宽度
        float textWidth = mTextPaint.measureText(content);
        //获取圆的半径,直径等于文字的宽度加padding
        radius = (int) ((textWidth + padding * 2 * 3) / 2);
        //圆的paint
        circlePaint = new Paint();
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL);
        //圆弧的区域
        mRectF = new RectF(3, 3, radius * 2 - 3, radius * 2 - 3);
        //圆弧的Paint
        ringPaint = new Paint();
        ringPaint.setColor(Color.RED);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(5f);
        ringPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(radius * 2, radius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        canvas.drawCircle(radius, radius, radius, circlePaint);
        //画文字 y轴坐标计算公式radius-(mTextPaint.ascent()+mTextPaint.density)/2
        canvas.drawText(content, padding * 3, radius - (mTextPaint.ascent() + mTextPaint.density) / 2, mTextPaint);
        //画圆弧
        canvas.drawArc(mRectF, -90, sweepAngle, false, ringPaint);
    }

    public void start(final int milli) {
        post(new Runnable() {
                 @Override
                 public void run() {
                     mTimer = new Timer();
                     mTimer.schedule(new RingTask(milli), 0, period);
                 }
             }

        );
    }

    private class RingTask extends TimerTask {
        private int milli;

        public RingTask(int milli) {
            this.milli = milli;
        }

        @Override
        public void run() {
            int perAngle = 360 * period / milli;//每秒转这么多的角度
            sweepAngle += perAngle;
            post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
            if (sweepAngle >= 360) {
                mTimer.cancel();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if(onRingClickListener!=null) {
                    onRingClickListener.onClick(this);
                }
                break;
        }
        return true;
    }
}
