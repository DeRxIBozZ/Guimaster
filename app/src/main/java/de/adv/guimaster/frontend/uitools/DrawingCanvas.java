package de.adv.guimaster.frontend.uitools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawingCanvas extends View {

    public int width;
    public int height;
    public Bitmap bitmap;
    //public Canvas canvas;
    public Path path;
    public Paint paint;
    public Paint bitmapPaint;
    public Context context;
    public Paint circlePaint;
    public Path circlePath;
    public float mX, mY;
    public static final float TOUCH_TOLERANCE = 4;


    public DrawingCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
    }

    public DrawingCanvas(Context context,@Nullable AttributeSet attrs){
        super(context, attrs);
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        paint = new Paint();
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        //canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,0,0,bitmapPaint);
        canvas.drawPath(path,paint);
        canvas.drawPath(circlePath,circlePaint);
    }

    private void touch_start(float x, float y){
        path.reset();
        path.moveTo(x,y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            path.quadTo(mX,mY,(x+mX)/2,(y+mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX,mY,30,Path.Direction.CW);
        }
    }

    private void touch_up(){
        path.lineTo(mX,mY);
        circlePath.reset();
        //canvas.drawPath(path,paint);
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touch_start(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

}
