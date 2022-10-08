package com.sand.apm.multidragrcv.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

public class TouchRecylerView extends RecyclerView {

    private String title;//分组信息
    private boolean isMark = false;//是否标记为拖放目标
    private Paint markPanit, normalPaint;
    private TestBean testBean;

    public TouchRecylerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        markPanit = new Paint();
        markPanit.setAntiAlias(true);
        markPanit.setStyle(Paint.Style.STROKE);
        markPanit.setStrokeWidth(5);
        markPanit.setColor(Color.GREEN);


        normalPaint = new Paint();
        normalPaint.setAntiAlias(true);
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(5);
        normalPaint.setColor(Color.BLUE);

    }

    /**
     * 把数据转移到拖放目标中去
     *
     * @param tarGettouchRecylerView
     */
    public void moveDataTo(TouchRecylerView tarGettouchRecylerView) {

        //从原来的TouchRecylerView重删除数据
        CommonAdapter<TestBean> sourceAdapter = ((CommonAdapter<TestBean>) getAdapter());
        List<TestBean> sourceDatas = sourceAdapter.getDatas();
        sourceDatas.remove(testBean);
//        Collections.sort(sourceDatas,GroupTeahBeanComparator.getInstance()); //再次排名
        sourceAdapter.notifyDataSetChanged();

        //把删除的数据添加到目标TouchRecylerView
        CommonAdapter<TestBean> targetAdapter = (CommonAdapter<TestBean>) tarGettouchRecylerView.getAdapter();
        List<TestBean> targetDatas = targetAdapter.getDatas();
        targetDatas.add(testBean);//默认放到最后了，可以根据具体业务设置放置的位置
        targetAdapter.notifyDataSetChanged();

    }


    public void markDragIn() {
        isMark = true;
        markPanit.setColor(Color.GREEN);
        postInvalidate();
    }

    public void markStart() {
        isMark = true;
        markPanit.setColor(Color.RED);
        postInvalidate();
    }

    public void unMark() {
        isMark = false;
        postInvalidate();
    }


    Rect r = null;

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (null == r) {
            r = new Rect();
            r.left = 0;
            r.top = 0;
            r.right = width;
            r.bottom = height;
        }
        if (isMark) {
            canvas.drawRect(r, markPanit);
        } else {
            canvas.drawRect(r, normalPaint);
        }

    }

    /**
     * 判断是否处于触摸状态
     *
     * @param x
     * @return
     */
    public boolean containsPointer(int x) {
        int[] locations = new int[2];
        getLocationOnScreen(locations);
        int left = locations[0];
        int right = left + getWidth();
        if (left <= x && right >= x) {
            return true;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }
}