package com.example.baseapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BallView extends View {
    private Paint ballPaint;
    private Paint targetPaint;
    private float ballX, ballY;
    private float targetX, targetY;
    private float ballRadius = 50;
    private float targetRadius = 75;
    private int viewWidth, viewHeight;

    public BallView(Context context) {
        super(context);
        ballPaint = new Paint();
        ballPaint.setColor(Color.BLUE);
        targetPaint = new Paint();
        targetPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(ballY, ballX, ballRadius, ballPaint);
        canvas.drawCircle(targetX, targetY, targetRadius, targetPaint);
    }

    public void updateBall(float deltaX, float deltaY) {
        ballX += deltaX * 25;
        ballY += deltaY * 25;

        // Ensure the ball stays within the view boundaries
        if (ballY - ballRadius < 0) {
            ballY = ballRadius;
        } else if (ballY + ballRadius > viewWidth) {
            ballY = viewWidth - ballRadius;
        }

        if (ballX - ballRadius < 0) {
            ballX = ballRadius;
        } else if (ballX + ballRadius > viewHeight) {
            ballX = viewHeight - ballRadius;
        }

        invalidate();
    }

    public void setTargetPosition(float x, float y) {
        targetX = x;
        targetY = y;
        invalidate();
    }

    public boolean isBallOnTarget() {
        float distance = (float) Math.sqrt(Math.pow(ballY - targetX, 2) + Math.pow(ballX - targetY, 2));
        return distance <= targetRadius;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        ballX = w / 2;
        ballY = h / 2;
    }
}
