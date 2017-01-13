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

    private Bitmap bitmap;
    private Paint paint;
    private Shader luar;
    private Shader dalam;
    private ComposeShader shader;

    int circlePadding = 5;
    final float[] color = { 1.f, 1.f, 1.f };
    int rgbB = Color.BLUE;
    float circleRadius = 16;
    float circleX = circleRadius;
    float circleY = circleRadius;
    int width, height;
    int pixel;

    public BackgroundQuad(Context context) {
        super(context);
        init();
    }

    public BackgroundQuad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackgroundQuad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init(){

    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null) {
            paint = new Paint();
            luar = new LinearGradient(0.f, 0.f, 0.f, this.getMeasuredHeight(), 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        }

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
        circlePaint.setStrokeWidth(circlePadding);
        canvas.drawCircle(circleX,circleY,circleRadius,circlePaint);

        bitmap = this.getDrawingCache(true);
        pixel = bitmap.getPixel((int)circleX,(int)circleY);
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
    public interface OnColorImageChangedListener {
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

                if(circleX<circleRadius){                              //top_left
                    circleX = circleRadius;
                    if(circleY<circleRadius){
                        circleY = circleRadius;
                    }
                    else if(circleY>height-circleRadius){
                        circleY = height-circleRadius;
                    }
                }
                else if(circleY<circleRadius){                         //top_right
                    circleY = circleRadius;
                    if(circleX>width-circleRadius){
                        circleX = width - circleRadius;
                    }
                }

                else if(circleX>width-circleRadius) {              //bottom_right
                    circleX = width - circleRadius;
                    if (circleY > height-circleRadius) {
                        circleY = height - circleRadius;
                    }
                }

                else if(circleY>height-circleRadius){              //bottom_left
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
