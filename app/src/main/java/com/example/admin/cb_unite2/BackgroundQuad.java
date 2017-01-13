package com.example.admin.cb_unite2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.rgb;

public class BackgroundQuad extends View{

    private OnColorImageChangedListener xz;

    Paint paint;
    Shader luar;
    int _circlePadding = 5;
    //    final float[] color = { 301.3125f, 0.43854168f, 0.49374998f };
    final float[] color = { 1.f, 1.f, 1.f };

    float circleRadius = 16;
    float circleX = circleRadius;
    float circleY = circleRadius;
    int width, height;
    Shader dalam;
    ComposeShader shader;
    private Bitmap bitmap;
    int pixel;

    public BackgroundQuad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundQuad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int rgbB = Color.BLUE;

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(this.toString(), "OnDraw");
        if (paint == null) {
            paint = new Paint();
            luar = new LinearGradient(0.f, 0.f, 0.f, this.getMeasuredHeight(), 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        }
//        rgb = Color.HSVToColor(color);
        dalam = new LinearGradient(0.f+circleRadius, 0.f+circleRadius, this.getMeasuredWidth()-circleRadius, 0.f-circleRadius, 0xffffffff, rgbB, Shader.TileMode.CLAMP);
        shader = new ComposeShader(luar, dalam, PorterDuff.Mode.OVERLAY );
        paint.setShader(shader);
        canvas.drawRect(0.f+circleRadius, 0.f+circleRadius, this.getMeasuredWidth()-circleRadius,
                this.getMeasuredHeight()-circleRadius, paint);
        Log.d("Draw", "w: "+getMeasuredWidth()+ " h: "+getMeasuredHeight());

        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(_circlePadding);
        canvas.drawCircle(circleX,circleY,circleRadius,circlePaint);

        bitmap = this.getDrawingCache(true);
        pixel = bitmap.getPixel((int)circleX,(int)circleY);
//        int redValue = Color.red(pixel);
//        int blueValue = Color.blue(pixel);
//        int greenValue = Color.green(pixel);
//        LogUtil.info(this, "redValue - " + redValue + " greenValue - " + greenValue + " blueValue - " + blueValue);
//        col = Color.rgb(redValue, greenValue, blueValue);
//        LogUtil.info(this,"pixel " + pixel + "color " + col);
        xz.colorChanged(pixel);
    }

    @Override
    public void setLayerPaint(Paint paint) {
        super.setLayerPaint(paint);
    }

    void setHue(float hue){
        color[0] = hue;
        invalidate();
    }

    void  setMyColor(int _color){
        rgbB = _color;
        invalidate();
    }
    //////////////////////////////////////////////////
    public interface OnColorImageChangedListener
    {
        void colorChanged(int color);
    }

    void setImageColor(OnColorImageChangedListener i, int c){
        xz = i;
        pixel = c;
        invalidate();
    }



/////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
            }
            case MotionEvent.ACTION_MOVE: {
                circleX = event.getX();
                circleY = event.getY();

                if(circleX<circleRadius){                              //лево верх
                    circleX = circleRadius;
                    if(circleY<circleRadius){
                        circleY = circleRadius;
                    }
                    else if(circleY>height-circleRadius){
                        circleY = height-circleRadius;
                    }
                }
                else if(circleY<circleRadius){                         //право верх
                    circleY = circleRadius;
                    if(circleX>width-circleRadius){
                        circleX = width - circleRadius;
                    }
                }

                else if(circleX>width-circleRadius) {              //право низ
                    circleX = width - circleRadius;
                    if (circleY > height-circleRadius) {
                        circleY = height - circleRadius;
                    }
                }

                else if(circleY>height-circleRadius){              //лево низ
                    circleY = height-circleRadius;
                    if(circleX<circleRadius){
                        circleX = circleRadius;
                        circleY = height-circleRadius;
                    }
                }
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
            width = getWidth();
            height = getHeight();
        }
    }
}
