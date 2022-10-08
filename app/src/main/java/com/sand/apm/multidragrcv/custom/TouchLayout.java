package com.sand.apm.multidragrcv.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class TouchLayout extends AbsoluteLayout {


    private OnTouchUpAndScrollListener onTouchUpListener;
    private int downLocationX, downLocationY;//rcv被拖拽的View长按的锚点
    private View itemViewRoot;//rcv的单个按下的Ietm根View
    private TouchRecylerView sourceTouchRecylerView;//拖拽起始点TouchRecylerView
    private int currentMoveX;//当前的X轴拖拽

    public TouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置被拖拽的ImageVIew在abslayout中的位置参数
     * @param x
     * @param y
     */
    public void setImageViewParamXY(int x, int y) {
        ImageView imageView = (ImageView) getChildAt(0);//仅此一个子View
        LayoutParams lpp = (LayoutParams) imageView.getLayoutParams();
        lpp.x = x;
        lpp.y = y;
        imageView.setLayoutParams(lpp);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.d("TouchLayout.onTouchEvent----->" + event.toString());
        ImageView dragImaView = (ImageView) getChildAt(0);
        LayoutParams lpp = (LayoutParams) dragImaView.getLayoutParams();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                dragImaView.setVisibility(View.INVISIBLE);
                if (null != onTouchUpListener) {
                    onTouchUpListener.onTouchUp(currentMoveX,downLocationX, itemViewRoot,sourceTouchRecylerView);
                    itemViewRoot = null;
                    sourceTouchRecylerView=null;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                dragImaView.setVisibility(View.VISIBLE);
                if (null != itemViewRoot) {
                    currentMoveX=(int) event.getX();
                    int x = (int) event.getX() - downLocationX;
                    int y = (int) event.getY() - downLocationY;
                    setImageViewParamXY(x, y);
                    if (null != onTouchUpListener) {
                        onTouchUpListener.onScrollMotion(currentMoveX,downLocationX, this, itemViewRoot,dragImaView, sourceTouchRecylerView);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (null != itemViewRoot) {
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 拖拽回调
     */
    public static interface OnTouchUpAndScrollListener {
        public void onTouchUp(int currentMoveX,int downLocationX,View downView,TouchRecylerView sourceTouchRecylerView);
        public void onScrollMotion(int currentMoveX,int downLocationX, TouchLayout touchLayout,View downView,ImageView dragImageView,TouchRecylerView sourceTouchRecylerView);
    }

    public OnTouchUpAndScrollListener getOnTouchUpListener() {
        return onTouchUpListener;
    }

    public void setOnTouchUpListener(OnTouchUpAndScrollListener onTouchUpListener) {
        this.onTouchUpListener = onTouchUpListener;
    }

    public int[] setDownLocation(float downLocationX, float downLocationY, View itemViewRoot,TouchRecylerView sourceTouchRecylerView) {
        this.downLocationX = (int) downLocationX;
        this.downLocationY = (int) downLocationY;
        this.itemViewRoot = itemViewRoot;
        this.sourceTouchRecylerView=sourceTouchRecylerView;
        //以下的注释便于分析原理
//       LogUtils.d(downView.getParent().toString());//rcv
//       LogUtils.d(downView.getParent().getParent().toString());//relativelayout
//       LogUtils.d(downView.getParent().getParent().getParent().toString());//linearlayoutChids:hscrooView的唯一子view
//       LogUtils.d(downView.getParent().getParent().getParent().getParent().toString());//hscrollView 就是和本View一样大的View
//        TouchHorizontalScrollView horizontalScrollView = (TouchHorizontalScrollView) downView.getParent().getParent().getParent().getParent();
//        int[] touchLocations = new int[2];//被长按的View在屏幕位置
//        downView.getLocationOnScreen(touchLocations);

        int inppppView[] = getLocationInPPview(itemViewRoot);
//将位置放到放到指针下面
//        int resultX=(int)(inppppView[0]+downLocationX);
//        int resultY=(int)(inppppView[1]+downLocationY);
//放到原位置
        int resultX = (int) (inppppView[0]);
        int resultY = (int) (inppppView[1]);

        setImageViewParamXY(resultX, resultY);

        return inppppView;

    }

    /**
     * 获取右侧长按的Item在右侧relativeLayout的准确位置
     * @param downView
     * @return
     */
    public int[] getLocationInPPview(View downView) {

        int left0 = downView.getLeft();
        int top0 = downView.getTop();

        TouchRecylerView p1 = (TouchRecylerView) downView.getParent();


        int left1 = p1.getLeft();//rcv
        int top1 = p1.getTop();

        RelativeLayout p2 = (RelativeLayout) (downView.getParent().getParent());

        int left2 = p2.getLeft();//relativelayout
        int top2 = p2.getTop();

        LinearLayout p3 = (LinearLayout) (downView.getParent().getParent().getParent());

        int left3 = p3.getLeft();//linearlayoutChids
        int top3 = p3.getTop();


        TouchHorizontalScrollView p4 = (TouchHorizontalScrollView) (downView.getParent().getParent().getParent().getParent());

        int left4 = p4.getLeft()-p4.getScrollX();//考虑到滚动效果必须把这个加上
        int top4 = p4.getTop();
        return new int[]{left0 + left1 + left2 + left3 + left4, top0 + top1 + top2 + top3 + top4};
    }


}