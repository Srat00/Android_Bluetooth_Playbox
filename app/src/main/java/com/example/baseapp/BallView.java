package com.example.baseapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BallView extends View {
    private static final int RADIUS = 50;
    protected Paint paint;
    private float x = 100;
    private float y = 100;
    private float vx = 0;
    private float vy = 0;
    private float viewWidth;
    private float viewHeight;
    public BallView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public void updateBall(float ax, float ay) {
        vx += ax;
        vy += ay;
        x += vx;
        y += vy;
        if (x <= RADIUS) {
            x = RADIUS;
            vx = 0; // 벽에 닿으면 속도를 0으로 설정
        } else if (x >= viewWidth - RADIUS) {
            x = viewWidth - RADIUS;
            vx = 0;
        }
        if (y <= RADIUS) {
            y = RADIUS;
            vy = 0;
        } else if (y >= viewHeight - RADIUS) {
            y = viewHeight - RADIUS;
            vy = 0;
        }
        invalidate(); // 다시 그리기 위해 호출
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, RADIUS, paint);
    }
}
