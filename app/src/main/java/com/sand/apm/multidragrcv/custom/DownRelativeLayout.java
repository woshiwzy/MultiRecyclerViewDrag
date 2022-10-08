package com.sand.apm.multidragrcv.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DownRelativeLayout extends RelativeLayout {

    private float longClickX, longClickY;

    public DownRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                int downX= (int) event.getX();
                int downY= (int) event.getY();
                longClickX=downX;
                longClickY=downY;
                break;
        }
        return super.onTouchEvent(event);
    }


    public float getLongClickX() {
        return longClickX;
    }

    public void setLongClickX(float longClickX) {
        this.longClickX = longClickX;
    }

    public float getLongClickY() {
        return longClickY;
    }

    public void setLongClickY(float longClickY) {
        this.longClickY = longClickY;
    }
}