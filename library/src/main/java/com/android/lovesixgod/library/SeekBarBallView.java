package com.android.lovesixgod.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

/**
 * 自定义弧形SeekBar 圆球部分
 * Created by Jaceli on 2016-04-28.
 */
public class SeekBarBallView extends View {

    private Paint paint;
    private Scroller scroller;
    private int ballSize;
    private String ballColor;
    private OnSmoothScrollListener listener;

    public SeekBarBallView(Context context, String ballColor, int ballSize) {
        super(context);
        this.ballColor = ballColor;
        this.ballSize = ballSize;
        init(context);
    }

    public SeekBarBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ballColor = "#FFFFFF";
        this.ballSize = 10;
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor(ballColor));
        paint.setStyle(Paint.Style.FILL);
    }

    public void setListener(OnSmoothScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spec = MeasureSpec.makeMeasureSpec(ballSize, MeasureSpec.EXACTLY);
        setMeasuredDimension(spec, spec);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (listener != null) {
                listener.onSmoothScroll(scroller.getCurrX());
                postInvalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2, paint);
    }

    /**
     * 平滑滑动
     *
     * @param start    起始值
     * @param distance 滑动距离
     */
    public void smoothScrollLevel(int start, int distance) {
        scroller.startScroll(start, 0, distance, 0, 200);
        postInvalidate();
    }

    public interface OnSmoothScrollListener {
        void onSmoothScroll(int currentX);
    }
}

