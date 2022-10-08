package com.sand.apm.multidragrcv;

import android.app.Application;

import com.llk.reflection.JJReflection;

/**
 * @ProjectName: MultiDragRcv
 * @Date: 2022/10/8
 * @Desc:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JJReflection.apiExemptions();
    }
}