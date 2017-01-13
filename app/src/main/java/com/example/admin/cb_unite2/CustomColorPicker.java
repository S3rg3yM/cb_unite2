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
    private int mInitialColor = -1;
    private int[] mColors;
    private float _trackerPos = 0f;
    private int _trackerWidth;
    private int _trackerColor;
    private int _trackerPadding;
    private int _alpha;
    Paint mLinearPaint;
    Shader lShader;

    public CustomColorPicker(Context context)
    {
        super(context);
        initColorModel(context);
    }

    public CustomColorPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initColorModel(context);
    }

    private void initColorModel(Context context)
    {
        mColors = buildHueColorArray();
        _trackerWidth = 5;
        _trackerColor = Color.WHITE;
        _trackerPadding = 5;
    }

    private int[] buildHueColorArray()
    {
        int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--, count++)
        {
            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return hue;
    }

    public void initColorPicker(OnColorChangedListener l, int initColor)
    {
        mListener = l;
        setColor(initColor);
    }

    public int getmInitialColor() {
        return mInitialColor;
    }

    private void setColor(int initColor)
    {
        mInitialColor = initColor;
        _alpha = Color.alpha(mInitialColor);
    }

    public interface OnColorChangedListener
    {
        void colorChanged(int color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLinearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lShader = new LinearGradient(getHeight()/2, 0, this.getWidth()-getHeight()/2, 0, mColors, null, Shader.TileMode.REPEAT);
        mLinearPaint.setShader(lShader);
        mLinearPaint.setStyle(Paint.Style.FILL);
        mLinearPaint.setStrokeWidth(32);
        canvas.drawRect(getHeight()/2, this.getHeight() / 4, this.getWidth()-getHeight()/2, 3 * this.getHeight() / 4, mLinearPaint);
        drawTracker(canvas);
    }

    private void calculateTrackerPosition()
    {
        if (mInitialColor != -1)
        {
            float[] hsv = new float[3];
            Color.colorToHSV(mInitialColor, hsv);
            float unit = (360 - hsv[0]) / 360;
            _trackerPos = (float) this.getWidth() * unit;
        }
    }

    public void navigateSlidertoSelColor(int color)
    {
        setColor(color);
        calculateTrackerPosition();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTrackerPosition();
    }

    private void drawTracker(Canvas canvas) {
        Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(mInitialColor);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setStrokeWidth(_trackerPadding);
        canvas.drawCircle(_trackerPos, this.getHeight() / 2, getHeight() / 2 - 2, whitePaint);
        whitePaint.setColor(_trackerColor);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeWidth(_trackerPadding);
        canvas.drawCircle(_trackerPos, this.getHeight() / 2, getHeight() / 2 - 2, whitePaint);
    }

    private int ave(int s, int d, float p)
    {
        return (int) (s + (p * (d - s)));
    }

    private int interpColor(int colors[], float unit)
    {
        if (unit > 1 || unit <0)
        {
            return Color.argb(_alpha, Color.red(colors[colors.length - 1]), Color.green(colors[colors.length - 1]),
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
        return Color.argb(_alpha, r, g, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                float unit = event.getX() / (float) this.getWidth();
                int color = interpColor(mColors, unit);
                mInitialColor = color;
                if (event.getX() < getHeight()/2) {
                    _trackerPos = getHeight()/2;
                } else if (event.getX() > this.getWidth()-getHeight()/2) {
                    _trackerPos = this.getWidth()-getHeight()/2;
                } else {
                    _trackerPos = event.getX();
                }
                mListener.colorChanged(color);
                invalidate();
            }
            break;
        }
        return true;
    }

}
