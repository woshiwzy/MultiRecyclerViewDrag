package com.sand.apm.multidragrcv.custom;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class TouchHorizontalScrollView extends HorizontalScrollView {

    private final int MANU_SCROLL_STATE_LEFT=-1,MANU_SCROLL_STATE_STOP=0,MANU_SCROLL_STATE_RIGHT=1;
    private volatile int currentManuScrollState=MANU_SCROLL_STATE_STOP;

    private Thread scrollThread=null;

    private final int SCROLL_STEP=3;

    public TouchHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        scrollThread=new Thread(new Runnable() {
            @Override
            public void run() {

                while (!scrollThread.isInterrupted()){

                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {


                            switch (currentManuScrollState){
                                case MANU_SCROLL_STATE_LEFT:
                                    scrollBy(SCROLL_STEP,0);
                                    break;
                                case MANU_SCROLL_STATE_STOP:
                                    break;
                                case MANU_SCROLL_STATE_RIGHT:
                                    scrollBy(-SCROLL_STEP,0);
                                    break;

                            }

                        }
                    });

                }
            }
        });
        scrollThread.start();
    }


    public void scroll2Left(){
        this.currentManuScrollState=MANU_SCROLL_STATE_LEFT;
    }

    public void scroll2Right(){
        this.currentManuScrollState=MANU_SCROLL_STATE_RIGHT;
    }

    public void stopScroll(){
        this.currentManuScrollState=MANU_SCROLL_STATE_STOP;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(null!=scrollThread){
            scrollThread.interrupt();
            scrollThread=null;
        }
    }



    /**
     * 手动触发一个cancel事件
     * 后来发现沒有太大的用途，但是是一個很好的学习样本代码值得留下
     */
    @Deprecated
    public void triggerCancelEvent(){
        //手动触发一个cancel事件
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        int metaState = 0;
        int x = 0, y = 0;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_CANCEL, x, y, metaState);
        dispatchTouchEvent(motionEvent);
    }

}