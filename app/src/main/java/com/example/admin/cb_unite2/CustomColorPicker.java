package com.example.admin.cb_unite2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomColorPicker extends FrameLayout {

    private OnColorChangedListener mListener;

    private Paint mLinearPaint;
    private Paint whitePaint;
    private Shader lShader;

    private int width;
    private int height;
    private int mInitialColor;
    private int[] mColors;
    private float trackerPos;
    private int trackerWidth;
    private int trackerColor;
    private int trackerPadding;
    private int alpha;
    private int circlePadding;

    public CustomColorPicker(Context context){
        super(context);
        init();
    }

    public CustomColorPicker(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRect(height/2, height/ 4, width-height/2, 3 * height / 4, mLinearPaint);
        drawTracker(canvas);
    }

    private void init(){
        mColors = buildHueColorArray();
        mInitialColor = -1;
        circlePadding = 2;
        trackerPos = 0f;
        trackerWidth = 5;
        trackerColor = Color.WHITE;
        trackerPadding = 5;
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private int[] buildHueColorArray() {
        int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--, count++){
            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return hue;
    }

    public void initColorPicker(OnColorChangedListener l, int initColor){
        mListener = l;
        setColor(initColor);
    }

    public int getmInitialColor(){
        return mInitialColor;
    }

    private void setColor(int initColor){
        mInitialColor = initColor;
        alpha = Color.alpha(mInitialColor);
    }

    public interface OnColorChangedListener{
        void colorChanged(int color);
    }

    private void initPaint(){
        lShader = new LinearGradient(getHeight()/2, 0, getWidth()-getHeight()/2, 0, mColors, null, Shader.TileMode.REPEAT);
        mLinearPaint.setShader(lShader);
        mLinearPaint.setStyle(Paint.Style.FILL);
        mLinearPaint.setStrokeWidth(32);
    }

    private void calculateTrackerPosition(){
        if (mInitialColor != -1) {
            float[] hsv = new float[3];
            Color.colorToHSV(mInitialColor, hsv);
            float unit = (360 - hsv[0]) / 360;
            trackerPos = (float) getWidth() * unit;
        }
    }

    public void navigateSlidertoSelColor(int color) {
        setColor(color);
        calculateTrackerPosition();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTrackerPosition();
    }

    private void drawTracker(Canvas canvas) {
        whitePaint.setColor(mInitialColor);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setStrokeWidth(trackerPadding);
        canvas.drawCircle(trackerPos, height / 2, height / 2 - circlePadding, whitePaint);
        whitePaint.setColor(trackerColor);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeWidth(trackerPadding);
        canvas.drawCircle(trackerPos, height / 2, height / 2 - circlePadding, whitePaint);
    }

    private int ave(int s, int d, float p) {
        return (int) (s + (p * (d - s)));
    }

    private int interpColor(int colors[], float unit) {

        if (unit > 1 || unit <0) {
            return Color.argb(alpha, Color.red(colors[colors.length - 1]), Color.green(colors[colors.length - 1]),
                    Color.blue(colors[colors.length - 1]));
        }

        float p = unit * (colors.length - 1);
        int i = (int) p;
        p -= i;
        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i + 1];
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        return Color.argb(alpha, r, g, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                float unit = event.getX() / (float)width;
                int color = interpColor(mColors, unit);
                mInitialColor = color;
                if (event.getX() < height/2) {
                    trackerPos = height/2;
                } else if (event.getX() > width-height/2) {
                    trackerPos = width-height/2;
                } else {
                    trackerPos = event.getX();
                }
                mListener.colorChanged(color);
                invalidate();
            }
            break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initPaint();
            width = getWidth();
            height = getHeight();
        }
    }
}
