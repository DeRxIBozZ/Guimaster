package de.adv.guimaster.frontend.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawingView extends View {
    private Paint paint = new Paint();
    private float x;
    private float y;

    public DrawingView(Context context) {
        super(context);
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(x, y, 200, paint);
    }
}

