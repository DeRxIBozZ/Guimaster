package de.adv.guimaster.frontend.uitools;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.adv.guimaster.R;
import de.adv.guimaster.frontend.activity.MainActivity;

public class ProgressBarAnimation extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    int casenum;
    int count;


    public ProgressBarAnimation(Context context, ProgressBar progressBar, TextView textView){
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.count = 0;
        this.casenum = 1;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = 100 * interpolatedTime;
        progressBar.setProgress((int) value);
        if(count % 50 == 0) {
            switch (casenum) {
                case 1:
                    textView.setText(R.string.loading__);
                    casenum++;
                    break;
                case 2:
                    textView.setText(R.string.loading___);
                    casenum++;
                    break;
                case 3:
                    textView.setText(R.string.loading_);
                    casenum = 1;
                    break;
            }
        }
        count++;
        if(value == 100){
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
