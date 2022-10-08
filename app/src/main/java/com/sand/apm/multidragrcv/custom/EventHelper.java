package com.sand.apm.multidragrcv.custom;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressLint("SoonBlockedPrivateApi") //反射警告
public class EventHelper {


    /**
     * 把group的首个事件接受者修改为target，达到按需设置事件的接受者
     * (因為需要反射，所以需要先解決反射问题：https://github.com/OBaKai/JJReflection)
     * @param target
     * @param group
     */
    public static void giveFistTargetForViewGroup(View target, ViewGroup group) {
        try {
            Field firstTarget = ViewGroup.class.getDeclaredField("mFirstTouchTarget");
            firstTarget.setAccessible(true);
//            Object firstTouchtarget=firstTarget.get(group);
            Class innerClass = Class.forName("android.view.ViewGroup$TouchTarget");
            //obtain(@NonNull View child, int pointerIdBits)
            Method obtainMethod = innerClass.getMethod("obtain", View.class, int.class);
            obtainMethod.setAccessible(true);
            Object newTart=obtainMethod.invoke(null,target,1);
            firstTarget.set(group,newTart);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 反射方式清除ViewGroup上的事件
     * @param group
     * @param event
     */

    public static void cancelAndClearTouchTargetsMethodFroViewGroup(ViewGroup group, MotionEvent event) {
        try {
           Method cancelAndClearTouchTargetsMethod = ViewGroup.class.getDeclaredMethod("cancelAndClearTouchTargets", MotionEvent.class);
            cancelAndClearTouchTargetsMethod.setAccessible(true);
            cancelAndClearTouchTargetsMethod.invoke(group, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射充值ViewGroup的触摸状态
     * @param group
     */
    public static void resetTouchStateMethodForViewGroup(ViewGroup group) {
        try {
            Method resetTouchStateMethod = ViewGroup.class.getDeclaredMethod("resetTouchState");
            resetTouchStateMethod.setAccessible(true);
            resetTouchStateMethod.invoke(group);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}