package de.adv.guimaster.frontend.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import de.adv.guimaster.frontend.uitools.Forms;

public class DrawingView extends View {
    private Paint paint = new Paint();
    private float x;
    private float y;
    private float endX;
    private float endY;
    private Forms forms;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void setStartPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void setEndPos(float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
    }

    public float calcRadius(float x, float y, float endX, float endY) {
        float length = Math.abs(x-endX);
        float width = Math.abs(y-endY);
        return (float) Math.sqrt((length*length)+(width*width));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        switch (this.forms) {
            case LINE:
                canvas.drawLine(x,y,endX,endY, paint);
                break;
            case RECT:
                canvas.drawRect(x,y,endX,endY,paint);
                break;
            case CIRCLE:
                canvas.drawCircle(x, y, calcRadius(x,y,endX,endY), paint);
                break;
        }
    }


}

