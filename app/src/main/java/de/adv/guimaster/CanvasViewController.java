package de.adv.guimaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CanvasViewController extends View {

    public Bitmap bitmap;


    public CanvasViewController(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public CanvasViewController(Context context,@Nullable AttributeSet attrs){
        super(context, attrs);
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


    @Override
    public void onDraw(Canvas canvas){
        if(this.bitmap != null){
            canvas.drawBitmap(bitmap,0,0,null);
        }
        super.onDraw(canvas);
    }

}

