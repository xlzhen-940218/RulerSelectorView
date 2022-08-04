package com.xlzhen.rulerselectorview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RulerSelectorView extends View implements GestureDetector.OnGestureListener {
    private Path trianglePath;
    private Paint trianglePaint, linePaint, textPaint;
    private int dp1, dp6, dp8, dp9, dp10, dp12, dp17, dp20, dp25, dp40;
    private int min, max,current;
    private Map<Integer, Float> map;
    private int width;
    private GestureDetector gestureDetector;

    private NumberSelectListener listener;

    public RulerSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RulerSelectorView);
        min = array.getInt(R.styleable.RulerSelectorView_select_min, 0);
        max = array.getInt(R.styleable.RulerSelectorView_select_max, 100);
        current = array.getInt(R.styleable.RulerSelectorView_select_current, 50);
        init(context);
    }

    private void init(Context context) {
        dp1 = dp2px(1);
        dp6 = dp2px(6);
        dp8 = dp2px(8);
        dp9 = dp2px(9);
        dp10 = dp2px(10);
        dp12 = dp2px(12);
        dp17 = dp2px(17);
        dp20 = dp2px(20);
        dp25 = dp2px(25);
        dp40 = dp2px(40);

        trianglePaint = new Paint();
        trianglePaint.setAntiAlias(true);
        trianglePaint.setColor(getResources().getColor(R.color.purple_500));

        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.color_b2));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dp1);
        linePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.color_666));
        textPaint.setTextSize(dp10);
        textPaint.setTextAlign(Paint.Align.CENTER);

        trianglePath = new Path();
        trianglePath.moveTo(0, 0);
        trianglePath.lineTo(dp6, dp8);
        trianglePath.lineTo(dp12, 0);

        gestureDetector = new GestureDetector(context, this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float differenceX = 0f;
            for (int key : map.keySet()) {
                float x = map.get(key);
                if (Math.abs(width / 2f - x) <= dp6) {
                    differenceX = width / 2f - x;
                    if(listener!=null){
                        listener.numberSelect(key);
                    }
                    break;
                }
            }

            for (int key : map.keySet()) {
                map.put(key, map.get(key) + differenceX);
            }
            invalidate();
            return false;
        }
        return gestureDetector.onTouchEvent(event);
    }

    private void setMinMax(int min, int max) {
        this.min = min;
        this.max = max;
        map = new HashMap<>();
        for (int i = min; i <= max; i++) {
            map.put(i, (i - min) * dp9 + 0f);
        }

        float differenceX = width / 2f - map.get(current);
        for (int key : map.keySet()) {
            map.put(key, map.get(key) + differenceX);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (int key : map.keySet()) {
            float x = map.get(key);
            if (x < 0) {
                continue;
            }
            if (x > width)
                continue;//超出不渲染
            float y = 30-Math.abs(width/2f-x)/2f;
            if(y<0){
                y=0;
            }
            canvas.drawLine(x, y, x, key % 5 == 0 ? y+dp25 : y+dp17, linePaint);
            if (key % 5 == 0) {
                canvas.drawText(String.valueOf(key), x, y+dp40, textPaint);
            }
        }

        canvas.drawPath(trianglePath, trianglePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        trianglePath.offset(w / 2f - dp12 / 2f, 0);
        setMinMax(min, max);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (map.get(min) > 0 && distanceX < 0) {
            return true;
        }
        if (map.get(max) < width && distanceX > 0) {
            return true;
        }
        for (int key : map.keySet()) {
            map.put(key, map.get(key) - distanceX);
            if (Math.abs(width / 2f - map.get(key)) <= dp6) {
                if(listener!=null){
                    listener.numberSelect(key);
                }
            }
        }
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public int dp2px(float dpVal) {
        try {
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem()
                            .getDisplayMetrics());
        } catch (Exception ex) {
            return 0;
        }

    }

    public void setListener(NumberSelectListener listener) {
        this.listener = listener;
    }

    public interface NumberSelectListener{
        void numberSelect(int number);
    }
}
