package com.android.lovesixgod.customarcseekbar.seekbar;

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
    private PointF pointF1; // 起始点
    private PointF pointF2; // 控制点
    private PointF pointF3; // 终止点

    public SeekBarArcView(Context context) {
        super(context);
        init();
    }

    public SeekBarArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeekBarArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        pointF1 = new PointF();
        pointF2 = new PointF();
        pointF3 = new PointF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        pointF1.set(0, bottom - top - 30);
        pointF2.set((right - left) / 2, -(bottom - top) / 4);
        pointF3.set(right, bottom - top - 30);
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
    }
}
