package com.example.eoffapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedView extends View {

    private Paint circlePaint;
    private Paint squarePaint;

    private float circleRadius = 100f;
    private float circleScale = 1f;
    private float squareY = 0f;

    private ValueAnimator pulsingAnimator;
    private ValueAnimator movingAnimator;

    public AnimatedView(Context context) {
        super(context);
        init();
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);

        squarePaint = new Paint();
        squarePaint.setColor(Color.BLUE);

        setupAnimations();
    }

    private void setupAnimations() {
        // Pulzálás (kör skálázása)
        pulsingAnimator = ValueAnimator.ofFloat(1f, 1.5f);
        pulsingAnimator.setDuration(1000);
        pulsingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulsingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulsingAnimator.addUpdateListener(animation -> {
            circleScale = (float) animation.getAnimatedValue();
            invalidate(); // újrarajzolás
        });

        // Mozgó négyzet animáció
        movingAnimator = ValueAnimator.ofFloat(0f, -200f);
        movingAnimator.setDuration(1500);
        movingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        movingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        movingAnimator.addUpdateListener(animation -> {
            squareY = (float) animation.getAnimatedValue();
            invalidate();
        });

        pulsingAnimator.start();
        movingAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Rajzoljuk a pulzáló kört
        canvas.drawCircle(centerX, centerY, circleRadius * circleScale, circlePaint);

        // Rajzoljuk a mozgó négyzetet a kör felett
        float squareSize = 80f;
        float squareLeft = centerX - squareSize / 2f;
        float squareTop = centerY - circleRadius * circleScale - 100f + squareY;
        canvas.drawRect(squareLeft, squareTop, squareLeft + squareSize, squareTop + squareSize, squarePaint);
    }
}
