package de.adv.guimaster.logic;

import android.widget.TextView;

import java.util.TimerTask;

import de.adv.guimaster.R;

public class TimerLoading extends TimerTask {

    TextView tv;
    int count;

    public TimerLoading(TextView tv){
        this.tv = tv;
        count = 0;
    }
    @Override
    public void run() {
        switch (count){
            case 0:
                tv.setText(R.string.loading__);
                count++;
                break;
            case 1:
                tv.setText(R.string.loading___);
                count++;
                break;
            case 2:
                tv.setText(R.string.loading_);
                count = 0;
                break;
        }
    }
}
