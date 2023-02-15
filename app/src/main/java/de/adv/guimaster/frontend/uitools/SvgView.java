package de.adv.guimaster.frontend.uitools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.caverock.androidsvg.SVGImageView;

public class SvgView extends SVGImageView {

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public SvgView(Context context) {
        super(context);
    }

    public SvgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SvgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(this.bitmap != null){
            canvas.setBitmap(this.bitmap);
        }
    }
}

