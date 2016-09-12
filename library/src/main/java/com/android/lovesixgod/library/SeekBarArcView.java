package com.android.lovesixgod.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义弧形SeekBar 弧线部分
 * Created by Jaceli on 2016-04-28.
 */
public class SeekBarArcView extends View {

    private Paint paint;
    private Path path;
    private PointF pointF1;     // 起始点
    private PointF pointF2;     // 控制点
    private PointF pointF3;     // 终止点
    private String arcColor;    // 弧的颜色
    private int arcWidth;       // 弧的宽度

    public SeekBarArcView(Context context, String arcColor, int arcWidth) {
        super(context);
        this.arcColor = arcColor;
        this.arcWidth = arcWidth;
        init();
    }

    public SeekBarArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.arcColor = "#000000";
        this.arcWidth = 90;
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        pointF1 = new PointF();
        pointF2 = new PointF();
        pointF3 = new PointF();
        // 初始化画笔
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor(arcColor));
        paint.setStrokeWidth(arcWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left - 10, top, right + 10, bottom);
        pointF1.set(0, bottom - top - 30);
        pointF2.set((right - left) / 2, -(bottom - top) / 4);
        pointF3.set(right, bottom - top - 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画2阶贝塞尔曲线
        path.moveTo(pointF1.x, pointF1.y);
        path.quadTo(pointF2.x, pointF2.y, pointF3.x, pointF3.y);
        canvas.drawPath(path, paint);
    }
}
