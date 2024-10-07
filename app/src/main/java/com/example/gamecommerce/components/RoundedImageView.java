package com.example.gamecommerce.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImageView extends AppCompatImageView {

    private Path path;
    private float radius = 40.0f;

    public RoundedImageView(Context context) {
        super(context);
        path = new Path();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        path = new Path();
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//         Update the Path when the size of the view changes
//        path.reset();  // Clear previous path
//        path.addRoundRect(0, 0, w, h, radius, radius, Path.Direction.CCW);  // Create rounded path
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.addRoundRect(0, 0, getWidth(), getHeight(), radius, radius, Path.Direction.CCW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
