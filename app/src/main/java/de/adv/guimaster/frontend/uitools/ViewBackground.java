package de.adv.guimaster.frontend.uitools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import de.adv.guimaster.R;

public class ViewBackground extends View {

    Paint paint = new Paint();

    public ViewBackground(Context context) {
        super(context);
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255,38,38,38);
    }

    public ViewBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255,38,38,38);
    }

    public ViewBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255,38,38,38);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }
}
