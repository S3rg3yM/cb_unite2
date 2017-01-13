package com.example.admin.cb_unite2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    BackgroundQuad backgroundQuad;
    CustomColorPicker customColorPicker;
    ImageView imageView;

    final float[] currentColorHsv = new float[3];
    int color = 0xffffff00;
    int alpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundQuad = (BackgroundQuad) findViewById(R.id.bcQuad);
        customColorPicker = (CustomColorPicker) findViewById(R.id.colorslider);
        imageView = (ImageView) findViewById(R.id.imageColor);

        alpha = Color.alpha(color);
        Color.colorToHSV(color, currentColorHsv);

        float hue = 360.f - 360.f / backgroundQuad.getMeasuredHeight() * 100;
        if (hue == 360.f) hue = 0.f;
        setHue(hue);

        backgroundQuad.setHue(getHue());

        customColorPicker.initColorPicker(new CustomColorPicker.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                backgroundQuad.setMyColor(color);
            }
        }, Color.BLUE);

        backgroundQuad.setImageColor(new BackgroundQuad.OnColorImageChangedListener() {
            @Override
            public void colorChanged(int color) {
                imageView.setBackgroundColor(color);
            }
        },backgroundQuad.pixel);
    }

    private float getHue() {
        return currentColorHsv[0];
    }

    private void setHue(float hue) {
        currentColorHsv[0] = hue;
    }
}
