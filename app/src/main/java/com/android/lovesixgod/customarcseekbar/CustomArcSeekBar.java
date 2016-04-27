package com.android.lovesixgod.customarcseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.math.BigDecimal;

/**
 * 自定义弧形SeekBar
 * Created by Jaceli on 2016-04-26.
 */
public class CustomArcSeekBar extends View {

    private Scroller scroller;
    private Paint paint;
    private Path path;
    private PointF pointF1; // 起始点
    private PointF pointF2; // 控制点
    private PointF pointF3; // 终止点
    private PointF circleCenter; // 圆心的坐标
    private int top;
    private int right;
    private int bottom;
    private int left;
    private float currentX; // 当前x坐标，用于控制圆球位置
    private int currentLevel; // 当前档次
    private OnProgressChangedListener listener;

    private final static float RADIUS = 30f; // 圆球半径
    private final static float LEVEL = 6f; // 设置档次

    public CustomArcSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomArcSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomArcSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        path = new Path();
        pointF1 = new PointF();
        pointF2 = new PointF();
        pointF3 = new PointF();
        circleCenter = new PointF();
        scroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            currentX = scroller.getCurrX();
            postInvalidate();
        }
    }

    public void setListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        pointF1.set(0, bottom - top);
        pointF2.set((right - left) / 2, -(bottom - top) / 2);
        pointF3.set(right, bottom - top);
        currentX = (right - left) / LEVEL;
        currentLevel = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画2阶贝塞尔曲线
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(pointF1.x, pointF1.y);
        path.quadTo(pointF2.x, pointF2.y, pointF3.x, pointF3.y);
        canvas.drawPath(path, paint);

        // 通过x坐标，计算圆心的坐标，画圆
        float t = (currentX / (right - left));
        float x = (1 - t) * (1 - t) * pointF1.x + 2 * (t) * (1 - t) * pointF2.x + t * t * pointF3.x;
        float y = (1 - t) * (1 - t) * pointF1.y + 2 * (t) * (1 - t) * pointF2.y + t * t * pointF3.y;
        circleCenter.set(x, y);
        paint.reset();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, RADIUS, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                float distance = (downX - circleCenter.x) * (downX - circleCenter.x) + (downY - circleCenter.y) * (downY - circleCenter.y);
                // 计算到圆球中心的距离，考虑20的误差
                return !(distance - (RADIUS + 20) * (RADIUS + 20) > 0);
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                currentX = moveX; // 通过x坐标重绘圆球
                invalidate();
                currentLevel = getLevel(moveX);
                if (listener != null) {
                    listener.OnProgressChanged(currentLevel);
                }
                break;
            default:
                // 当手指移出或者离开View时，圆球平滑滑到最近的档次
                scroller.startScroll((int) currentX, 0, (int) ((right - left) / LEVEL * currentLevel - currentX), 0, 200);
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算档次
     *
     * @param x 横坐标
     * @return 档次
     */
    private int getLevel(float x) {
        float ratio = (x / (right - left)) * LEVEL;
        // 计算距离哪个档次最近
        int result = new BigDecimal(ratio).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        if (result < 1) {
            result = 1;
        } else if (result > (LEVEL - 1)) {
            result = (int) (LEVEL - 1);
        }
        return result;
    }

    /**
     * 滑动接口
     */
    public interface OnProgressChangedListener {
        void OnProgressChanged(int level);
    }
}
