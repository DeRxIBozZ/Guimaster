package de.adv.guimaster;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    int count;


    public ProgressBarAnimation(Context context, ProgressBar progressBar, TextView textView){
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.count = 1;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = 100 * interpolatedTime;
        progressBar.setProgress((int) value);
        switch (count){
            case 1 :
                textView.setText(R.string.loading__);
                count++;
                break;
            case 2 :
                textView.setText(R.string.loading___);
                count++;
                break;
            case 3 :
                textView.setText(R.string.loading_);
                count = 1;
                break;
        }

        if(value == 100){
            context.startActivity(new Intent(context,MainActivity.class));
        }
    }
}
