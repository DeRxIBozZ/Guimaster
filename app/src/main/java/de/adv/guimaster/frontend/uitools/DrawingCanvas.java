package de.adv.guimaster.frontend.uitools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingCanvas extends View {

    static Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    static int paintColor = 0xFFFFFFFF;
    //stroke width
    private  float STROKE_WIDTH = 5f;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //eraser mode
    private boolean erase=false;
    private Forms forms = Forms.FREE_FORM;
    float startX;
    float startY;

    //constructor
    public DrawingCanvas(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        setErase(erase);
    }


    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(STROKE_WIDTH);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    //*************************************** View assigned size  ****************************************************

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    public void setErase(boolean isErase){
        erase=isErase;
        drawPaint = new Paint();
        if(erase) {
            setupDrawing();
            int srcColor= 0xFF000000;

            PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(srcColor, mode);

            drawPaint.setColorFilter(porterDuffColorFilter);

            drawPaint.setColor(srcColor);
            drawPaint.setXfermode(new PorterDuffXfermode(mode));

        }
        else {

            setupDrawing();

        }
    }

    //************************************   draw view  *************************************************************

    @Override
    protected void onDraw(Canvas canvas) {
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(Color.WHITE);
        drawPaint.setStrokeWidth(5);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //***************************   respond to touch interaction   **************************************************

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        canvasPaint.setColor(paintColor);
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                startX = touchX;
                startY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                switch (forms) {
                    case FREE_FORM:
                        drawCanvas.drawPath(drawPath, drawPaint);
                        drawPath.lineTo(touchX, touchY);
                        break;
                    case LINE:
                        drawPath.reset();
                        drawPath.moveTo(startX, startY);
                        drawPath.lineTo(touchX, touchY);
                        break;
                    case RECT:
                        drawPath.reset();
                        drawPath.moveTo(startX, startY);
                        if (startX < touchX && startY < touchY) {
                            drawPath.addRect(startX, startY, touchX, touchY, Path.Direction.CW);
                        } else if (startX < touchX && startY > touchY) {
                            drawPath.addRect(startX, touchY, touchX, startY, Path.Direction.CW);
                        } else if (startX > touchX && startY < touchY) {
                            drawPath.addRect(touchX, startY, startX, touchY, Path.Direction.CW);
                        } else if (startX > touchX && startY > touchY) {
                            drawPath.addRect(touchX, touchY, startX, startY, Path.Direction.CW);
                        }
                        break;
                    case CIRCLE:
                        drawPath.reset();
                        drawPath.moveTo(startX, startY);
                        drawPath.addCircle(startX, startY, calcRadius(startX, startY, touchX,touchY), Path.Direction.CW);
                        break;
                    default:
                        return false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if(forms.equals(Forms.FREE_FORM)) {
                    drawPath.lineTo(touchX, touchY);
                }
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;
    }

    public float calcRadius(float x, float y, float endX, float endY) {
        float length = Math.abs(x-endX);
        float width = Math.abs(y-endY);
        return (float) Math.sqrt((length*length)+(width*width));
    }
    public void setForms(Forms forms) {
        this.forms = forms;
    }

    //***********************************   return current alpha   ***********************************************
    public int getPaintAlpha(){
        return Math.round((float)STROKE_WIDTH/255*100);
    }

    //**************************************  set alpha   ******************************************************
    public void setPaintAlpha(int newAlpha){
        STROKE_WIDTH=Math.round((float)newAlpha/100*255);
        drawPaint.setStrokeWidth(newAlpha);
    }
}