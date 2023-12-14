package com.example.multimediaapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.TimeZone;

public class SaatView extends View {
    private Paint mCirclePaint;
    private Paint mHourHandPaint;
    private Paint mMinuteHandPaint;
    private Paint mTextPaint;

    public SaatView(Context context) {
        super(context);
        init();
    }

    public SaatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int teal_700 = ContextCompat.getColor(getContext(), R.color.teal_700);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(teal_700); // Setting the frame color to teal_700
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(10f);


        int purpleColor = ContextCompat.getColor(getContext(), R.color.a);


        mHourHandPaint = new Paint();
        mHourHandPaint.setColor(purpleColor);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mHourHandPaint.setStrokeWidth(15f);


        int purpleColor700 = ContextCompat.getColor(getContext(), R.color.a);

        mMinuteHandPaint = new Paint();
        mMinuteHandPaint.setColor(purpleColor700);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mMinuteHandPaint.setStrokeWidth(10f);



        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(40f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        startClockUpdater();
    }

    private void startClockUpdater() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                startClockUpdater();
            }
        }, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 4;

        canvas.drawCircle(width / 2, height / 2, radius, mCirclePaint);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3")); // GMT+3 saat dilimini ayarla

        float hourHandAngle = (360f / 12) * (calendar.get(Calendar.HOUR) % 12) + (360f / 12) * (calendar.get(Calendar.MINUTE) / 60f);
        float minuteHandAngle = (360f / 60) * calendar.get(Calendar.MINUTE);

        canvas.save();
        canvas.rotate(hourHandAngle, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - radius / 2, mHourHandPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(minuteHandAngle, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - radius + 50, mMinuteHandPaint);
        canvas.restore();

        int innerCircleRadius = radius - 40;

        canvas.drawCircle(width / 2, height / 2, radius, mCirclePaint);

        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI / 2 - 2 * Math.PI / 12 * (i - 12);
            float x = (float) (width / 1.94 + innerCircleRadius * Math.cos(angle));
            float y = (float) (height / 2 - innerCircleRadius * Math.sin(angle));

            String number = String.valueOf(i);
            float textWidth = mTextPaint.measureText(number);
            float textHeight = mTextPaint.descent() + mTextPaint.ascent();

            canvas.drawText(number, x - textWidth / 2, y - textHeight / 2, mTextPaint);
        }
    }
}
