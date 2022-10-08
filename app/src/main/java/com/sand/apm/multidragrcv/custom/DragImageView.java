package com.sand.apm.multidragrcv.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DragImageView extends androidx.appcompat.widget.AppCompatImageView {

    public DragImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public int getLocationOnScreenX(){
        int[] drageLocation=new int[2];
        getLocationOnScreen(drageLocation);
        return drageLocation[0];
    }


}